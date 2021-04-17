package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.LinkedList;

public class Ghost implements Collidable {

    private Rectangle _ghost;
    private Color _color;
    private Pane _boardPane;
    private MazeSquare[][] _map;
    private boolean _collided;


    public Ghost(String ghostName, Pane boardPane, MazeSquare[][] map) {
        _collided = false;
        _boardPane = boardPane;
        _map = map;

        switch (ghostName) {
            case "pinky":
                _color = Color.PINK;
                break;
            case "inky":
                _color = Color.LIGHTBLUE;
                break;
            case "blinky":
                _color = Color.RED;
                break;
            case "clyde":
                _color = Color.ORANGE;
                break;
        }

        _ghost = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH, _color);

    }


    public void setLocation(int row, int col) {

        _ghost.setY(row * Constants.SQUARE_WIDTH);
        _ghost.setX(col * Constants.SQUARE_WIDTH);
    }



    public boolean isCollided() {
        return _collided;
    }

    @Override
    public void collide() {
        this.removeFromPane(_boardPane);
        _collided = true;
    }

    @Override
    public String getType() {
        return "ghost";
    }

    public int getColLocation() {
        return (int) (_ghost.getX() / Constants.SQUARE_WIDTH);
    }

    /**
     * This method allows the y-position of the tetris square to
     * be retrieved (as an integer).
     */
    public int getRowLocation() {
        return (int) (_ghost.getY() / Constants.SQUARE_WIDTH);
    }


    public void addToPane(Pane root) {
        root.getChildren().add(_ghost);
    }

    public void removeFromPane(Pane root) {
        root.getChildren().remove(_ghost);
    }

    public void move(Direction direction) {
        int rowOffset = 0;
        int colOffset = 0;

        switch(direction){
            case UP: rowOffset = -1; colOffset = 0;
                break;
            case DOWN: rowOffset = 1; colOffset = 0;
                break;
            case LEFT: rowOffset = 0; colOffset = -1;
                break;
            case RIGHT: rowOffset = 0; colOffset = 1;
                break;
            default: rowOffset = 0; colOffset = 0;
                break;
        }

        if(this.getColLocation() + colOffset >= 0 && this.getColLocation() + colOffset <= 22) {
            if (moveIsValid(direction)) {
                _map[this.getRowLocation()][this.getColLocation()].getSquareElements().remove(this);
                _map[this.getRowLocation() + rowOffset][this.getColLocation() + colOffset].getSquareElements().add(this);
                this.setLocation(this.getRowLocation() + rowOffset, this.getColLocation() + colOffset);
            }

        } else {

            if(this.getColLocation() + colOffset < 0){
                this.setLocation(11,22);

            } else if(this.getColLocation() + colOffset > 22){
                this.setLocation(11,0);
            }
        }
    }

    public boolean moveIsValid(Direction direction) {
        int rowOffset = 0;
        int colOffset = 0;

        switch(direction){
            case UP: rowOffset = -1; colOffset = 0;
                break;
            case DOWN: rowOffset = 1; colOffset = 0;
                break;
            case LEFT: rowOffset = 0; colOffset = -1;
                break;
            case RIGHT: rowOffset = 0; colOffset = 1;
                break;
            default: rowOffset = 0; colOffset = 0;
                break;
        }

        int yLocation = (this.getRowLocation() + rowOffset) * Constants.SQUARE_WIDTH;
        int xLocation = (this.getColLocation() + colOffset) * Constants.SQUARE_WIDTH;


        if (_map[yLocation / Constants.SQUARE_WIDTH][xLocation / Constants.SQUARE_WIDTH].isWall()) {
            return false;
        }
        return true;
    }

    public Direction ghostBFS(BoardCoordinate ghostCoordinate, Direction ghostDirection, BoardCoordinate targetCoordinate){
        LinkedList<BoardCoordinate> Q = new LinkedList<>();

        BoardCoordinate closestCoordinate = null;
        Direction[][] directions = new Direction[Constants.MAZE_DIMENSION][Constants.MAZE_DIMENSION];

        double smallestDistance = Double.POSITIVE_INFINITY;

        int ghostRow = ghostCoordinate.getRow();
        int ghostColumn = ghostCoordinate.getColumn();

        if(!_map[ghostRow+1][ghostColumn].isWall() && ghostDirection.opposite() != Direction.DOWN){
            System.out.println("in iffy1");
            Q.addLast(new BoardCoordinate(ghostRow+1,ghostColumn, false));
            directions[ghostRow+1][ghostColumn] = Direction.DOWN;
            directions[ghostRow][ghostColumn] = directions[ghostRow+1][ghostColumn];
        }

        if(!_map[ghostRow-1][ghostColumn].isWall() && ghostDirection.opposite() != Direction.UP){
            System.out.println("in iffy2");

            Q.addLast(new BoardCoordinate(ghostRow-1,ghostColumn, false));
            directions[ghostRow-1][ghostColumn] = Direction.UP;
            directions[ghostRow][ghostColumn] = directions[ghostRow-1][ghostColumn] ;
        }

        if(ghostColumn+1 < 22 && !(_map[ghostRow][ghostColumn+1].isWall()) && ghostDirection.opposite() != Direction.RIGHT){
            System.out.println("in iffy3");

              Q.addLast(new BoardCoordinate(ghostRow,ghostColumn+1, false));
              directions[ghostRow][ghostColumn+1] = Direction.RIGHT;
              directions[ghostRow][ghostColumn] = Direction.RIGHT;
        }

        if(ghostColumn+1 == 22 && !(_map[ghostRow][ghostColumn+1].isWall()) && ghostDirection.opposite() != Direction.RIGHT){
            System.out.println("in iffy3-");
            Q.addLast(new BoardCoordinate(ghostRow,0, false));
            directions[ghostRow][0] = Direction.RIGHT;
            directions[ghostRow][ghostColumn] = Direction.RIGHT;
        }


        if(ghostColumn-1 > 0 && !_map[ghostRow][ghostColumn-1].isWall() && ghostDirection.opposite() != Direction.LEFT){
            System.out.println("in iffy4");
            Q.addLast(new BoardCoordinate(ghostRow,ghostColumn-1, false));
             directions[ghostRow][ghostColumn-1] = Direction.LEFT;

            directions[ghostRow][ghostColumn] = Direction.LEFT;
        }

        if(ghostColumn-1 == 0 && !_map[ghostRow][ghostColumn-1].isWall() && ghostDirection.opposite() != Direction.LEFT){
            System.out.println("in iffy4");

            Q.addLast(new BoardCoordinate(ghostRow,22, false));
            directions[ghostRow][22] = Direction.LEFT;
            directions[ghostRow][ghostColumn] = Direction.LEFT;
        }

        while(!Q.isEmpty()){
            System.out.println("in while loop");
            BoardCoordinate currentCoordinate = Q.removeFirst();

            int xTarget = targetCoordinate.getColumn() * Constants.SQUARE_WIDTH;
            int yTarget = targetCoordinate.getRow() * Constants.SQUARE_WIDTH;
            int xCurrent = currentCoordinate .getColumn() * Constants.SQUARE_WIDTH;
            int yCurrent = currentCoordinate .getRow() * Constants.SQUARE_WIDTH;

            double distance = Math.sqrt(Math.pow(xTarget - xCurrent, 2)+ Math.pow(yTarget - yCurrent, 2));
            if(distance < smallestDistance){
                closestCoordinate = currentCoordinate ;
                smallestDistance = distance;
            }

            int currentRow = currentCoordinate.getRow();
            int currentColumn = currentCoordinate.getColumn();

            if(currentColumn < 22 && currentColumn > 0 && currentRow+1 < 22 && currentRow+1 > 0 && !(_map[currentRow+1][currentColumn].isWall()) && directions[currentRow+1][currentColumn]== null ){
                System.out.println("in if1");
                Q.addLast(new BoardCoordinate(currentRow+1,currentColumn, false));

                directions[currentRow+1][currentColumn] = directions[currentRow][currentColumn];
            }

            if(currentColumn < 22 && currentColumn > 0 && currentRow-1 < 22 && currentRow-1 > 0 && !(_map[currentRow-1][currentColumn].isWall()) && directions[currentRow-1][currentColumn] == null){
                System.out.println("in if2");
                Q.addLast(new BoardCoordinate(currentRow-1,currentColumn, false));

                directions[currentRow-1][currentColumn] =  directions[currentRow][currentColumn];
            }

            if( currentColumn+1 < 22 && !(_map[currentRow][currentColumn+1].isWall()) && directions[currentRow][currentColumn+1] == null){
                System.out.println("in if3");
                Q.addLast(new BoardCoordinate(currentRow,currentColumn+1, false));

                directions[currentRow][currentColumn+1] =  directions[currentRow][currentColumn];
            }

            if( currentColumn+1 == 22 && !(_map[currentRow][currentColumn+1].isWall()) && directions[currentRow][currentColumn+1] == null){
                System.out.println("in if34");
                Q.addLast(new BoardCoordinate(currentRow,0, false));

                directions[currentRow][0] =  directions[currentRow][currentColumn];
            }

            if(currentColumn-1 > 0  && !(_map[currentRow][currentColumn-1].isWall()) && directions[currentRow][currentColumn-1] == null){
                System.out.println("in if4");
                Q.addLast(new BoardCoordinate(currentRow,currentColumn-1, false));

                directions[currentRow][currentColumn-1] = directions[currentRow][currentColumn];
            }

            if(currentColumn-1 == 0 && !(_map[currentRow][currentColumn-1].isWall()) && directions[currentRow][currentColumn-1] == null){
                System.out.println("in if4");
                Q.addLast(new BoardCoordinate(currentRow,22, false));

                directions[currentRow][22] =  directions[currentRow][currentColumn];
            }

        }
        assert closestCoordinate != null;
        return directions[closestCoordinate.getRow()][closestCoordinate.getColumn()];
    }
}
