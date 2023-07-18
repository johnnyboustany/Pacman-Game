package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The Ghost class creates the shape of the ghost.
 * This is a wrapper class that contains
 * a rectangle. It implements the Collidable interface as it
 * defines the collide method.
 */

public class Ghost implements Collidable {
    private Rectangle _ghost;
    private Color _color;
    private MazeSquare[][] _map;
    private Game _game;
    private String _ghostName;

    /**
     * The Ghost constructor stores the 2D map array from the Game class in an
     * instance variable. It is used in the ghost's
     * move and moveIsValid methods. The ghost is associated with the Game
     * class to allow it to increment the score, access the pen and cause
     * Pacman to lose a life when it collides with a non-frightened ghost.
     */
    public Ghost(String ghostName, MazeSquare[][] map, Game game) {
        _map = map;
        _game = game;
        _ghostName = ghostName;

        // color is set based on the passed in name of the ghost
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

    /**
     * If the game is in frightened mode, the collide method increments the score
     * and returns the ghost to the pen. If it is not, then the method kills the pacman.
     *
     */
    @Override
    public void collide() {
        if(_game.isInFrightenedMode()){
            _game.addToScore(Constants.SCORE_INCREMENT_GHOST);
            _game.getPen().addToPen(this);
        } else {
            _game.killPacman();
        }
    }

    /**
     * This method is used to return the type, hence, the name, of the ghost.
     * This allows the pen to distinguish between the different ghosts.
     *
     */
    @Override
    public String getType() {
        return _ghostName;
    }

    /**
     * This method makes the ghost move to a new, valid location based on
     * a passed in direction. It makes sure to always remove the ghost from
     * the arraylist of the old square and add it to the arraylist of the new one.
     */
    public void move(Direction direction) {
        // the ghost is first removed from the arraylist of the current square that it occupies
        _map[this.getRowLocation()][this.getColLocation()].getSquareElements().remove(this);

        // row and column offsets are determined based on the direction using a method
        // from the enum Direction class
        int rowOffset = direction.getRowOffset();
        int colOffset = direction.getColOffset();

        // if the new location is on the board/map, set the ghost to that location if it is not occupied by a wall
        if(this.getColLocation() + colOffset >= 0 && this.getColLocation() + colOffset <= Constants.MAZE_UPPER_BOUND) {
            if (moveIsValid(rowOffset, colOffset)) {

                // the ghost is added to the arraylist of the new square
                _map[this.getRowLocation() + rowOffset][this.getColLocation() + colOffset].getSquareElements().add(this);
                this.setLocation(this.getRowLocation() + rowOffset, this.getColLocation() + colOffset);
            }
        } else {
            // if the new location is off the board/map, set the ghost's location to the tunnel square on the opposite side
            // from its off-board current location

            if(this.getColLocation() + colOffset < 0){
                this.setLocation(Constants.ROW_OF_TUNNELS, Constants.MAZE_UPPER_BOUND);

                // ghost is added to arraylist of new square
                _map[Constants.ROW_OF_TUNNELS][Constants.MAZE_UPPER_BOUND].getSquareElements().add(this);

            } else if(this.getColLocation() + colOffset > Constants.MAZE_UPPER_BOUND){
                this.setLocation(Constants.ROW_OF_TUNNELS,0);

                // ghost is added to the arraylist of new square
                _map[Constants.ROW_OF_TUNNELS][Constants.MAZE_UPPER_BOUND].getSquareElements().add(this);
            }
        }
    }

    /**
     * This method checks whether the move will result in
     * a new position that is valid (is not occupied by a wall).
     */
    public boolean moveIsValid(int rowOffset, int colOffset) {
        int row = this.getRowLocation() + rowOffset;
        int col = this.getColLocation() + colOffset;

        if (_map[row][col].isWall()) {
            return false;
        }
        return true;
    }

    /**
     * This method returns a random direction that is valid, to be used
     * for the movement of the ghosts in frightened mode.
     */
    public Direction ghostFrightened(BoardCoordinate ghostCoordinate, Direction currentDirection){

        // color of ghost is set to show that it's in frightened mode
        _ghost.setFill(Color.CYAN);

        ArrayList<Direction> _validDirections = new ArrayList<>();

        int ghostRow = ghostCoordinate.getRow();
        int ghostColumn = ghostCoordinate.getColumn();

        // checking if each ghost neighbour is valid and adding the directions that correspond with each valid neighbour
        // to the arraylist of valid directions:

        if(!_map[ghostRow+1][ghostColumn].isWall() && currentDirection.opposite() != Direction.DOWN){
            _validDirections.add(Direction.DOWN);
        }

        if(!_map[ghostRow-1][ghostColumn].isWall() && currentDirection.opposite() != Direction.UP){
            _validDirections.add(Direction.UP);
        }

        if(ghostColumn+1 < Constants.MAZE_UPPER_BOUND && !(_map[ghostRow][ghostColumn+1].isWall()) && currentDirection.opposite() != Direction.RIGHT){
            _validDirections.add(Direction.RIGHT);
        }

        // accounting for the wrapping on the right side
        if(ghostColumn+1 >= Constants.MAZE_UPPER_BOUND && !(_map[ghostRow][0].isWall()) && currentDirection.opposite() != Direction.RIGHT){
            _validDirections.add(Direction.RIGHT);
        }

        if(ghostColumn-1 > 0 && !_map[ghostRow][ghostColumn-1].isWall() && currentDirection.opposite() != Direction.LEFT){
            _validDirections.add(Direction.LEFT);
        }

        // accounting for the wrapping on the left side
        if(ghostColumn-1 <= 0 && !_map[ghostRow][Constants.MAZE_UPPER_BOUND].isWall() && currentDirection.opposite() != Direction.LEFT){
            _validDirections.add(Direction.LEFT);
        }

        // generating a random integer between 0 and the size of the arraylist of valid directions
        int randInt = (int) (Math.random() * _validDirections.size());

        // storing the valid direction in a local variable before clearing the arraylist
        Direction randomDirection = _validDirections.get(randInt);
        _validDirections.clear();

        return randomDirection;
    }

    /**
     * This method uses a breadth-first search algorithm to return a direction
     * that allows the ghost to move towards a target location.
     */
    public Direction ghostBFS(BoardCoordinate ghostCoordinate, Direction ghostDirection, BoardCoordinate targetCoordinate){

        // color of ghost is set to show that it's not in frightened mode
        this.setToOriginalColor();

        LinkedList<BoardCoordinate> Q = new LinkedList<>();

        BoardCoordinate closestCoordinate = null;
        Direction[][] directions = new Direction[Constants.MAZE_DIMENSION][Constants.MAZE_DIMENSION];

        // initializing the smallestDistance in a way that the first neighbouring square will be regarded
        // as the closestSquare at first
        double smallestDistance = Double.POSITIVE_INFINITY;

        int ghostRow = ghostCoordinate.getRow();
        int ghostColumn = ghostCoordinate.getColumn();

        // checking if each ghost neighbour is valid and adding it to the queue and marking its direction
        // in the 2D Directions array if it is:

        if(!_map[ghostRow+1][ghostColumn].isWall() && ghostDirection.opposite() != Direction.DOWN){
            Q.addLast(new BoardCoordinate(ghostRow+1,ghostColumn, false));
            directions[ghostRow+1][ghostColumn] = Direction.DOWN;
            directions[ghostRow][ghostColumn] = directions[ghostRow+1][ghostColumn]; // marking ghost's direction so it isn't visited again
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

        // accounting for the wrapping on the right side
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

        // accounting for the wrapping on the left side
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

            // calculating the distance between the current and target locations using the Pythagorean
            // distance formula
            double distance = Math.sqrt(Math.pow(xTarget - xCurrent, 2)+ Math.pow(yTarget - yCurrent, 2));
            if(distance < smallestDistance){
                closestCoordinate = currentCoordinate ;
                smallestDistance = distance;
            }

            int currentRow = currentCoordinate.getRow();
            int currentColumn = currentCoordinate.getColumn();

            // checking if each neighbour of the current square is valid and adding it to the queue and marking its direction
            // in the 2D Directions array if it is:

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

            // accounting for the wrapping on the right side
            if( currentColumn+1 >= Constants.MAZE_UPPER_BOUND && !(_map[currentRow][0].isWall()) && directions[currentRow][0] == null){
                Q.addLast(new BoardCoordinate(currentRow,0, false));
                directions[currentRow][0] =  directions[currentRow][currentColumn];
            }

            if(currentColumn-1 > 0  && !(_map[currentRow][currentColumn-1].isWall()) && directions[currentRow][currentColumn-1] == null){
                Q.addLast(new BoardCoordinate(currentRow,currentColumn-1, false));
                directions[currentRow][currentColumn-1] = directions[currentRow][currentColumn];
            }

            // accounting for the wrapping on the left side
            if(currentColumn-1 <= 0 && !(_map[currentRow][Constants.MAZE_UPPER_BOUND].isWall()) && directions[currentRow][Constants.MAZE_UPPER_BOUND] == null){
                Q.addLast(new BoardCoordinate(currentRow,Constants.MAZE_UPPER_BOUND, false));
                directions[currentRow][Constants.MAZE_UPPER_BOUND] =  directions[currentRow][currentColumn];
            }
        }
        return directions[closestCoordinate.getRow()][closestCoordinate.getColumn()];
    }

    /**
     * This method sets the location of the ghost
     * based on row and column values by converting between
     * row/column values and pixels.
     */
    public void setLocation(int row, int col) {
        _ghost.setY(row * Constants.SQUARE_WIDTH);
        _ghost.setX(col * Constants.SQUARE_WIDTH);
    }

    /**
     * This method allows the row number of the ghost's location to
     * be retrieved.
     */
    public int getRowLocation() {
        return (int) (_ghost.getY() / Constants.SQUARE_WIDTH);
    }

    /**
     * This method allows the column number of the ghost's location to
     * be retrieved.
     */
    public int getColLocation() {
        return (int) (_ghost.getX() / Constants.SQUARE_WIDTH);
    }

    /**
     * This public method returns the ghost to its original color, and is used
     * in the BFS method and also in the Game class when the game is ended.
     */
    public void setToOriginalColor(){
        _ghost.setFill(_color);
    }

    /**
     *  This public method adds the ghost to a root pane.
     */
    public void addToPane(Pane root) {
        root.getChildren().add(_ghost);
    }
}
