package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

public class Ghost implements Collidable {
    private Rectangle _ghost;
    private Color _color;
    private MazeSquare[][] _map;
    private Game _game;
    private String _ghostName;

    public Ghost(String ghostName, MazeSquare[][] map, Game game) {
        _map = map;
        _game = game;
        _ghostName = ghostName;

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

    @Override
    public void collide() {
        if(_game.isInFrightenedMode()){
            _game.addToScore(Constants.SCORE_INCREMENT_GHOST);
            _game.getPen().addToPen(this);
        } else {
            _game.killPacman();
        }
    }

    @Override
    public String getType() {
        return _ghostName;
    }

    public void move(Direction direction) {
        _map[this.getRowLocation()][this.getColLocation()].getSquareElements().remove(this);

        int rowOffset = direction.getRowOffset();
        int colOffset = direction.getColOffset();

        if(this.getColLocation() + colOffset >= 0 && this.getColLocation() + colOffset <= Constants.MAZE_UPPER_BOUND) {
            if (moveIsValid(rowOffset, colOffset)) {
                _map[this.getRowLocation() + rowOffset][this.getColLocation() + colOffset].getSquareElements().add(this);
                this.setLocation(this.getRowLocation() + rowOffset, this.getColLocation() + colOffset);
            }
        } else {
            if(this.getColLocation() + colOffset < 0){
                this.setLocation(Constants.ROW_OF_TUNNELS, Constants.MAZE_UPPER_BOUND);
                _map[Constants.ROW_OF_TUNNELS][Constants.MAZE_UPPER_BOUND].getSquareElements().add(this);

            } else if(this.getColLocation() + colOffset > Constants.MAZE_UPPER_BOUND){
                this.setLocation(Constants.ROW_OF_TUNNELS,0);
                _map[Constants.ROW_OF_TUNNELS][Constants.MAZE_UPPER_BOUND].getSquareElements().add(this);
            }
        }
    }

    public boolean moveIsValid(int rowOffset, int colOffset) {
        int row = this.getRowLocation() + rowOffset;
        int col = this.getColLocation() + colOffset;

        if (_map[row][col].isWall()) {
            return false;
        }
        return true;
    }

    public Direction randomDirection(BoardCoordinate ghostCoordinate, Direction currentDirection){
        ArrayList<Direction> _validDirections = new ArrayList<>();
        Direction randomDirection;

        int ghostRow = ghostCoordinate.getRow();
        int ghostColumn = ghostCoordinate.getColumn();

        if(!_map[ghostRow+1][ghostColumn].isWall() && currentDirection.opposite() != Direction.DOWN){
            _validDirections.add(Direction.DOWN);
        }

        if(!_map[ghostRow-1][ghostColumn].isWall() && currentDirection.opposite() != Direction.UP){
            _validDirections.add(Direction.UP);
        }

        if(ghostColumn+1 < Constants.MAZE_UPPER_BOUND && !(_map[ghostRow][ghostColumn+1].isWall()) && currentDirection.opposite() != Direction.RIGHT){
            _validDirections.add(Direction.RIGHT);
        }

        if(ghostColumn+1 >= Constants.MAZE_UPPER_BOUND && !(_map[ghostRow][0].isWall()) && currentDirection.opposite() != Direction.RIGHT){
            _validDirections.add(Direction.RIGHT);
        }

        if(ghostColumn-1 > 0 && !_map[ghostRow][ghostColumn-1].isWall() && currentDirection.opposite() != Direction.LEFT){
            _validDirections.add(Direction.LEFT);
        }

        if(ghostColumn-1 <= 0 && !_map[ghostRow][Constants.MAZE_UPPER_BOUND].isWall() && currentDirection.opposite() != Direction.LEFT){
            _validDirections.add(Direction.LEFT);
        }

        int randInt = (int) (Math.random() * _validDirections.size());

        randomDirection = _validDirections.get(randInt);
        _validDirections.clear();

        return randomDirection;
    }

    public Direction ghostBFS(BoardCoordinate ghostCoordinate, Direction ghostDirection, BoardCoordinate targetCoordinate){
        LinkedList<BoardCoordinate> Q = new LinkedList<>();

        BoardCoordinate closestCoordinate = null;
        Direction[][] directions = new Direction[Constants.MAZE_DIMENSION][Constants.MAZE_DIMENSION];

        double smallestDistance = Double.POSITIVE_INFINITY;

        int ghostRow = ghostCoordinate.getRow();
        int ghostColumn = ghostCoordinate.getColumn();

        if(!_map[ghostRow+1][ghostColumn].isWall() && ghostDirection.opposite() != Direction.DOWN){
            Q.addLast(new BoardCoordinate(ghostRow+1,ghostColumn, false));
            directions[ghostRow+1][ghostColumn] = Direction.DOWN;
            directions[ghostRow][ghostColumn] = directions[ghostRow+1][ghostColumn];
        }

        if(!_map[ghostRow-1][ghostColumn].isWall() && ghostDirection.opposite() != Direction.UP){
            Q.addLast(new BoardCoordinate(ghostRow-1,ghostColumn, false));
            directions[ghostRow-1][ghostColumn] = Direction.UP;
            directions[ghostRow][ghostColumn] = directions[ghostRow-1][ghostColumn] ;
        }

        if(ghostColumn+1 < Constants.MAZE_UPPER_BOUND && !(_map[ghostRow][ghostColumn+1].isWall()) && ghostDirection.opposite() != Direction.RIGHT){
            Q.addLast(new BoardCoordinate(ghostRow,ghostColumn+1, false));
            directions[ghostRow][ghostColumn+1] = Direction.RIGHT;
            directions[ghostRow][ghostColumn] = Direction.RIGHT;
        }

        if(ghostColumn+1 >= Constants.MAZE_UPPER_BOUND && !(_map[ghostRow][0].isWall()) && ghostDirection.opposite() != Direction.RIGHT){
            Q.addLast(new BoardCoordinate(ghostRow,0, false));
            directions[ghostRow][0] = Direction.RIGHT;
            directions[ghostRow][ghostColumn] = Direction.RIGHT;
        }

        if(ghostColumn-1 > 0 && !_map[ghostRow][ghostColumn-1].isWall() && ghostDirection.opposite() != Direction.LEFT){
            Q.addLast(new BoardCoordinate(ghostRow,ghostColumn-1, false));
            directions[ghostRow][ghostColumn-1] = Direction.LEFT;
            directions[ghostRow][ghostColumn] = Direction.LEFT;
        }

        if(ghostColumn-1 <= 0 && !_map[ghostRow][Constants.MAZE_UPPER_BOUND].isWall() && ghostDirection.opposite() != Direction.LEFT){
            Q.addLast(new BoardCoordinate(ghostRow,Constants.MAZE_UPPER_BOUND, false));
            directions[ghostRow][Constants.MAZE_UPPER_BOUND] = Direction.LEFT;
            directions[ghostRow][ghostColumn] = Direction.LEFT;
        }

        while(!Q.isEmpty()){
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

            if(currentColumn < Constants.MAZE_UPPER_BOUND && currentColumn > 0 && currentRow+1 <Constants.MAZE_UPPER_BOUND && currentRow+1 > 0 && !(_map[currentRow+1][currentColumn].isWall()) && directions[currentRow+1][currentColumn]== null ){
                Q.addLast(new BoardCoordinate(currentRow+1,currentColumn, false));
                directions[currentRow+1][currentColumn] = directions[currentRow][currentColumn];
            }

            if(currentColumn < Constants.MAZE_UPPER_BOUND && currentColumn > 0 && currentRow-1 < Constants.MAZE_UPPER_BOUND && currentRow-1 > 0 && !(_map[currentRow-1][currentColumn].isWall()) && directions[currentRow-1][currentColumn] == null){
                Q.addLast(new BoardCoordinate(currentRow-1,currentColumn, false));
                directions[currentRow-1][currentColumn] =  directions[currentRow][currentColumn];
            }

            if( currentColumn+1 < Constants.MAZE_UPPER_BOUND && !(_map[currentRow][currentColumn+1].isWall()) && directions[currentRow][currentColumn+1] == null){
                Q.addLast(new BoardCoordinate(currentRow,currentColumn+1, false));
                directions[currentRow][currentColumn+1] =  directions[currentRow][currentColumn];
            }

            if( currentColumn+1 >= Constants.MAZE_UPPER_BOUND && !(_map[currentRow][0].isWall()) && directions[currentRow][0] == null){
                Q.addLast(new BoardCoordinate(currentRow,0, false));
                directions[currentRow][0] =  directions[currentRow][currentColumn];
            }

            if(currentColumn-1 > 0  && !(_map[currentRow][currentColumn-1].isWall()) && directions[currentRow][currentColumn-1] == null){
                Q.addLast(new BoardCoordinate(currentRow,currentColumn-1, false));
                directions[currentRow][currentColumn-1] = directions[currentRow][currentColumn];
            }

            if(currentColumn-1 <= 0 && !(_map[currentRow][Constants.MAZE_UPPER_BOUND].isWall()) && directions[currentRow][Constants.MAZE_UPPER_BOUND] == null){
                Q.addLast(new BoardCoordinate(currentRow,Constants.MAZE_UPPER_BOUND, false));
                directions[currentRow][Constants.MAZE_UPPER_BOUND] =  directions[currentRow][currentColumn];
            }

        }
        return directions[closestCoordinate.getRow()][closestCoordinate.getColumn()];
    }

    public void setLocation(int row, int col) {
        _ghost.setY(row * Constants.SQUARE_WIDTH);
        _ghost.setX(col * Constants.SQUARE_WIDTH);
    }

    /**
     * This method allows the y-position of the tetris square to
     * be retrieved (as an integer).
     */
    public int getRowLocation() {
        return (int) (_ghost.getY() / Constants.SQUARE_WIDTH);
    }

    public int getColLocation() {
        return (int) (_ghost.getX() / Constants.SQUARE_WIDTH);
    }


    public void changeColor(Color color){
        _ghost.setFill(color);
    }

    public void changeBackColor(){
        _ghost.setFill(_color);
    }

    public void addToPane(Pane root) {
        root.getChildren().add(_ghost);
    }
}
