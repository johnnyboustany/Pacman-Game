package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
/**
 * The Pacman class creates the shape of the pacman.
 * This is a wrapper class that contains
 * a circle.
 */
public class Pacman {
    private Circle _pacman;
    private MazeSquare[][] _map;

    /**
     * The Pacman constructor stores the 2D map array from the Game class in an
     * instance variable. It is used in the pacman's
     * move and moveIsValid methods.
     */
    public Pacman(MazeSquare[][] _map2D){
        _map = _map2D;
        _pacman = new Circle();
        _pacman.setRadius(Constants.PACMAN_RADIUS);
        _pacman.setFill(Color.YELLOW);
    }

    /**
     * This method makes the pacman move to a new, valid location based on
     * a passed in direction.
     */
    public void move(Direction direction){
        // row and column offsets are determined based on the direction using a method
        // from the enum Direction class
        int rowOffset = direction.getRowOffset();
        int colOffset = direction.getColOffset();

        // if the new location is on the board/map, set the pacman to that location if it is not occupied by a wall
        if(this.getColLocation() + colOffset >= 0 && this.getColLocation() + colOffset <= Constants.MAZE_UPPER_BOUND){

            if(moveIsValid(rowOffset,colOffset)){
            this.setLocation(this.getRowLocation() + rowOffset, this.getColLocation() + colOffset);
            }
        } else {
            // if the new location is off the board/map, set the pacman's location to the tunnel square on the opposite side
            // from its off-board current location

            if(this.getColLocation() + colOffset < 0){
                this.setLocation(Constants.ROW_OF_TUNNELS,Constants.MAZE_UPPER_BOUND);

            } else if(this.getColLocation() + colOffset > Constants.MAZE_UPPER_BOUND){
                this.setLocation(Constants.ROW_OF_TUNNELS,0);
            }
        }
        }

    /**
     * This method checks whether the move will result in
     * a new position that is valid (is not occupied by a wall).
     */
    public boolean moveIsValid(int rowOffset, int colOffset){
        int row = this.getRowLocation() + rowOffset;
        int col = this.getColLocation() + colOffset;

        if (_map[row][col].isWall()) {
            return false;
        }
        return true;
    }

    /**
     * This method sets the location of the pacman
     * based on row and column values by converting between
     * row/column values and pixels.
     */
    public void setLocation(int row, int col){
        _pacman.setCenterY(row*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
        _pacman.setCenterX(col*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
    }

    /**
     * This method allows the row number of the pacman's location to
     * be retrieved.
     */
    public int getRowLocation(){
        return (int) (_pacman.getCenterY()/Constants.SQUARE_WIDTH);
    }

    /**
     * This method allows the column number of the pacman's location to
     * be retrieved.
     */
    public int getColLocation(){
        return (int) (_pacman.getCenterX()/Constants.SQUARE_WIDTH);
    }

    /**
     *  This public method adds pacman to a root pane.
     */
    public void addToPane(Pane root){
        root.getChildren().add(_pacman);
    }
}
