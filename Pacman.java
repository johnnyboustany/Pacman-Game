package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pacman {
    private Circle _pacman;
    private MazeSquare[][] _map;

    public Pacman(MazeSquare[][] _map2D){
        _map = _map2D;
        _pacman = new Circle();
        _pacman.setRadius(Constants.PACMAN_RADIUS);
        _pacman.setFill(Color.YELLOW);
    }

    public void setLocation(int row, int col){
        _pacman.setCenterY(row*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
        _pacman.setCenterX(col*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
    }

    public void move(Direction direction){
        int rowOffset = direction.getRowOffset();
        int colOffset = direction.getColOffset();

        if(this.getColLocation() + colOffset >= 0 && this.getColLocation() + colOffset <= Constants.MAZE_UPPER_BOUND){

            if(moveIsValid(rowOffset,colOffset)){
            _pacman.setCenterY((this.getRowLocation() + rowOffset)*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
            _pacman.setCenterX((this.getColLocation() + colOffset)*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
            }

        } else {

            if(this.getColLocation() + colOffset < 0){
                this.setLocation(Constants.ROW_OF_TUNNELS,Constants.MAZE_UPPER_BOUND);

            } else if(this.getColLocation() + colOffset > Constants.MAZE_UPPER_BOUND){
                this.setLocation(Constants.ROW_OF_TUNNELS,0);
            }
        }
        }

    public boolean moveIsValid(int rowOffset, int colOffset){
        int yLocation = (this.getRowLocation() + rowOffset)*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2);
        int xLocation = (this.getColLocation() + colOffset)*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2);

        if(_map[yLocation/Constants.SQUARE_WIDTH][xLocation/Constants.SQUARE_WIDTH].isWall()){
            return false;
        }
      return true;
    }

    public int getColLocation(){
        return (int) (_pacman.getCenterX()/Constants.SQUARE_WIDTH);
    }

    /**
     * This method allows the y-position of the tetris square to
     * be retrieved (as an integer).
     */
    public int getRowLocation(){
        return (int) (_pacman.getCenterY()/Constants.SQUARE_WIDTH);
    }

    public void addToPane(Pane root){
        root.getChildren().add(_pacman);
    }


}
