package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import java.util.LinkedList;

/**
 * The GhostPen class handles the pen's timeline, in which ghosts are gradually
 * released from the pen at the start of the game and ghosts are returned to (and later removed from)
 * the pen when they are eaten during frightened mode.
 */

public class GhostPen {
    private int _penCounter;
    private Ghost _pinky;
    private Ghost _clyde;
    private Ghost _inky;
    private LinkedList _ghostPen;
    private Timeline _timeline;
    private Game _game;
    private MazeSquare[][] _map;

    /**
     * The GhostPen constructor takes in the 3 ghosts that start inside the pen
     * and adds them to the queue that it instantiates.
     */
    public GhostPen(Ghost pinky, Ghost clyde, Ghost inky, Game game, MazeSquare[][] map){
        _map = map;
        _game = game;
        _pinky = pinky;
        _clyde = clyde;
        _inky = inky;

        _ghostPen = new LinkedList<Ghost>();

        _ghostPen.addLast(_pinky);
        _ghostPen.addLast(_inky);
        _ghostPen.addLast(_clyde);

        _penCounter = 0; // pen counter is initialized
        this.setUpTimeline();
    }

    /**
     * This method is used to setup and play the Timeline so the GhostPen can start working.
     */
    private void setUpTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION), new TimeHandler());
        _timeline = new Timeline(kf);
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
    }

    /**
     * The Time Handler private class implements an event handler
     * and handles events of type ActionEvent.
     */
    private class TimeHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {

            if(_game.gameIsOver()){
                // the timeline stops when the game is declared over, so that the pen stops working
                _timeline.stop();
            } else {

                if(!_ghostPen.isEmpty()){
                    // if the queue is not empty, the pen counter increments
                    _penCounter++;

                    // when the counter hits a certain mark, a ghost is removed from the queue and sent outside the pen
                    if(_penCounter==Constants.GHOST_PEN_RELEASE_DURATION/Constants.DURATION){
                        _penCounter = 0;
                        Ghost currentGhost = (Ghost) _ghostPen.removeFirst();

                        // the ghost is removed from the arraylist of the square in its old location
                        _map[currentGhost.getRowLocation()][currentGhost.getColLocation()].getSquareElements().remove(currentGhost);

                        currentGhost.setLocation(Constants.BLINKY_STARTING_ROW,Constants.BLINKY_STARTING_COL);

                        // the ghost is added to the arraylist of the square in its new location
                        _map[currentGhost.getRowLocation()][currentGhost.getColLocation()].getSquareElements().add(currentGhost);
                    }
                }
            }
        }
    }

    /**
     * This public method sets the pen counter to a value.
     */
    public void setPenCounter(int value){
        _penCounter = value;
    }

    /**
     * This public method takes in a ghost and adds it to the back of the queue.
     * It sets the ghost location inside the pen, according to the ghost's type.
     */
    public void addToPen(Ghost ghost){

        _ghostPen.addLast(ghost);

        // the ghost is removed from the arraylist of the square in its old location
        _map[ghost.getRowLocation()][ghost.getColLocation()].getSquareElements().remove(ghost);

        switch (ghost.getType()) {
            case "blinky":
                ghost.setLocation(Constants.BLINKY_STARTING_ROW+Constants.BLINKY_SHIFTED,Constants.BLINKY_STARTING_COL);
                break;
            case "inky":
                ghost.setLocation(Constants.INKY_STARTING_ROW,Constants.INKY_STARTING_COL);
                break;
            case "pinky":
                ghost.setLocation(Constants.PINKY_STARTING_ROW,Constants.PINKY_STARTING_COL);
                break;
            case "clyde":
                ghost.setLocation(Constants.CLYDE_STARTING_ROW,Constants.CLYDE_STARTING_COL);
                break;
        }
        // the ghost is added to the arraylist of the square in its new location
        _map[ghost.getRowLocation()][ghost.getColLocation()].getSquareElements().add(ghost);
    }
}







