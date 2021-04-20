package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot implements Collidable{
    private Circle _dot;
    private Pane _boardPane;

    public Dot(Pane boardPane){
        _boardPane = boardPane;
        _dot = new Circle();
        _dot.setRadius(4);
        _dot.setFill(Color.WHITE);
    }

    @Override
    public void collide() {
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
