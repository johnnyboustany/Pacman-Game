package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Energizer implements Collidable{
    private Circle _energizer;
    private Pane _boardPane;
    private Game _game;

    public Energizer(Pane boardPane, Game game){
        _game = game;
        _boardPane = boardPane;
        _energizer = new Circle();
        _energizer.setRadius(8);
        _energizer.setFill(Color.WHITE);
    }

    @Override
    public void collide() {
        _game.addToScore(100);
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
