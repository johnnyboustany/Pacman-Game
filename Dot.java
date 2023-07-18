package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The Dot class creates the shape of the dot.
 * This is a wrapper class that contains
 * a circle. It implements the Collidable interface as it
 * defines the collide method.
 */
public class Dot implements Collidable{
    private Circle _dot;
    private Pane _boardPane;
    private Game _game;

    /**
     * The Dot constructor stores the boardPane in an instance variable to allow the class
     * graphically add its circle to it. It is associated with the Game
     * class to allow it to increment the score.
     */
    
    public Dot(Pane boardPane, Game game){
        _boardPane = boardPane;
        _game = game;

        _dot = new Circle();
        _dot.setRadius(Constants.DOT_RADIUS);
        _dot.setFill(Color.WHITE);
    }

    /**
     * The collide method increments the score
     * and graphically removes the dot.
     *
     */
    @Override
    public void collide() {
        _game.addToScore(Constants.SCORE_INCREMENT_DOT);
        this.removeFromPane(_boardPane);
    }

    /**
     * This method is used to return the type of this Collidable, which is dot.
     * This allows the Game to know if a square contains a dot
     * (when checking if game is over).
     *
     */
    @Override
    public String getType() {
        return "dot";
    }

    /**
     * This method sets the location of the dot
     * based on row and column values by converting between
     * row/column values and pixels.
     */
    public void setLocation(int row, int col){
        _dot.setCenterY(row*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
        _dot.setCenterX(col*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));

    }
    /**
     *  This public method adds the dot to a root pane.
     */
    public void addToPane(Pane root){
        root.getChildren().add(_dot);
    }

    /**
     *  This public method removes the dot from a root pane.
     */
    public void removeFromPane(Pane root){
        root.getChildren().remove(_dot);
    }


}
