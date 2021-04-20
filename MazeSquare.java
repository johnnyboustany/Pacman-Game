package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MazeSquare {
    private Rectangle _rect;
    private ArrayList<Collidable> _squareElements;

    public MazeSquare() {
        _rect = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        _rect.setStroke(Color.BLACK);
        _rect.setStrokeWidth(2);
        _squareElements = new ArrayList<>();
    }

    public boolean isWall() {
        if (_rect.getFill() == Color.DARKBLUE) {
            return true;
        }
        return false;
    }

    public boolean containsDot(int i) {
        if (_squareElements.get(i).getType() == "dot") {
            return true;
        } else {
            return false;
        }
    }

    public boolean containsEnergizer(int i) {
        if (_squareElements.get(i).getType() == "energizer") {
            return true;
        } else {
            return false;
        }
    }

    public boolean containsGhost(int i) {
        if (_squareElements.get(i).getType().equals("ghost")) {
            return true;
        } else {
            return false;
        }
    }

    public Collidable getCollidable(int i) {
        return _squareElements.get(i);
    }

    public ArrayList getSquareElements() {
        return _squareElements;
    }

    public void setXLocation(int x) {
        _rect.setX(x);
    }

    /**
     * This method allows the y-position of the maze square to be set.
     * The passed in y value becomes the y coordinate of the
     * upper left corner of the rectangle.
     */
    public void setYLocation(int y) {
        _rect.setY(y);
    }

    public void setColor(Color color) {
        _rect.setFill(color);
    }

    public void addToPane(Pane root) {
        root.getChildren().add(_rect);
    }


}
