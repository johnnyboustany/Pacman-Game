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
     * The collide method increments the score
     * and graphically removes the dot.
     *
     */
    @Override
    public void collide() {
        _game.addToScore(Constants.SCORE_INCREMENT_ENERGIZER);
        _game.setFrightenedMode();
        _boardPane.getChildren().remove(_energizer);
    }

    @Override
    public String getType() {
        return "energizer";
    }

    public void setLocation(int row, int col){
        _energizer.setCenterY(row*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
        _energizer.setCenterX(col*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
    }

    public void addToPane(Pane root){
        root.getChildren().add(_energizer);
    }


}
