package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

/**
 * The MazeSquare class creates the shape of the squares that
 * will be used to make up the map. This is a wrapper class that contains
 * a rectangle. It can check whether it is a wall or not. It can also
 * check for whether it contains a dot or energizer.
 */
public class MazeSquare {
    private Rectangle _rect;
    private ArrayList<Collidable> _squareElements;

    /**
     * The MazeSquare class contains an arraylist that holds
     * objects that implement the Collidable interface.
     */
    public MazeSquare() {
        _rect = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        _rect.setStroke(Color.BLACK);
        _rect.setStrokeWidth(Constants.MAZESQUARE_STROKE_WIDTH);
        _squareElements = new ArrayList<>();
    }

    /**
     * This public method checks the color of the MazeSquare
     * to determine whether it is a wall.
     */
    public boolean isWall() {
        if (_rect.getFill() == Color.DARKBLUE) {
            return true;
        }
        return false;
    }

    /**
     * This public method checks whether this MazeSquare holds a dot
     * or energizer. It is used in the Game class to check whether the game is over.
     *
     */
    public boolean containsDotOrEnergizer(int i) {
        if (_squareElements.get(i).getType()=="dot" || _squareElements.get(i).getType()=="energizer") {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This public method returns the arraylist of collidables contained
     * in this MazeSquare.
     *
     */
    public ArrayList getSquareElements() {
        return _squareElements;
    }


    /**
     * This public method sets the x location of the MazeSquare.
     *
     */
    public void setXLocation(int x) {
        _rect.setX(x);
    }

    /**
     *  This public method sets the y location of the MazeSquare.
     */
    public void setYLocation(int y) {
        _rect.setY(y);
    }

    /**
     *  This public method sets the color of the MazeSquare.
     */
    public void setColor(Color color) {
        _rect.setFill(color);
    }

    /**
     *  This public method adds the MazeSquare to a root pane.
     */
    public void addToPane(Pane root) {
        root.getChildren().add(_rect);
    }
}
