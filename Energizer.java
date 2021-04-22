package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The Energizer class creates the shape of the dot.
 * This is a wrapper class that contains
 * a circle. It implements the Collidable interface as it
 * defines the collide method.
 */
public class Energizer implements Collidable{
    private Circle _energizer;
    private Pane _boardPane;
    private Game _game;

    /**
     * The Energizer constructor stores the boardPane in an instance variable to allow the class
     * graphically add its circle to it. It is associated with the Game
     * class to allow it to increment the score and activate frightened mode.
     */
    public Energizer(Pane boardPane, Game game){
        _game = game;
        _boardPane = boardPane;
        _energizer = new Circle();
        _energizer.setRadius(Constants.ENERGIZER_RADIUS);
        _energizer.setFill(Color.WHITE);
    }

    /**
     * The collide method increments the score, sets game mode
     * to frightened and graphically removes the energizer.
     *
     */
    @Override
    public void collide() {
        _game.addToScore(Constants.SCORE_INCREMENT_ENERGIZER);
        _game.setFrightenedMode();
        this.removeFromPane(_boardPane);
    }

    /**
     * This method is used to return the type of this Collidable, which is energizer.
     * This allows the Game to know if a square contains an energizer
     * (when checking if game is over).
     *
     */
    @Override
    public String getType() {
        return "energizer";
    }

    /**
     * This method sets the location of the energizer
     * based on row and column values by converting between
     * row/column values and pixels.
     */
    public void setLocation(int row, int col){
        _energizer.setCenterY(row*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
        _energizer.setCenterX(col*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
    }

    /**
     *  This public method adds the energizer to a root pane.
     */
    public void addToPane(Pane root){
        root.getChildren().add(_energizer);
    }

    /**
     *  This public method removes the energizer from a root pane.
     */
    public void removeFromPane(Pane root){
        root.getChildren().remove(_energizer);
    }
}
