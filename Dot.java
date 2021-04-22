package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot implements Collidable{
    private Circle _dot;
    private Pane _boardPane;
    private Game _game;

    public Dot(Pane boardPane, Game game){
        _boardPane = boardPane;
        _game = game;

        _dot = new Circle();
        _dot.setRadius(Constants.DOT_RADIUS);
        _dot.setFill(Color.WHITE);
    }

    @Override
    public void collide() {
        _game.addToScore(Constants.SCORE_INCREMENT_DOT);
        this.removeFromPane(_boardPane);
    }

    @Override
    public String getType() {
        return "dot";
    }

    public void setLocation(int row, int col){
        _dot.setCenterY(row*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));
        _dot.setCenterX(col*Constants.SQUARE_WIDTH+(Constants.SQUARE_WIDTH/2));

    }

    public void addToPane(Pane root){
        root.getChildren().add(_dot);
    }

    public void removeFromPane(Pane root){
        root.getChildren().remove(_dot);
    }


}
