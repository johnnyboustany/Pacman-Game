package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import java.util.LinkedList;

public class GhostPen {
    private int _penCounter;
    private Ghost _pinky;
    private Ghost _clyde;
    private Ghost _inky;
    private LinkedList _ghostPen;
    private Timeline _timeline;
    private Game _game;
    private MazeSquare[][] _map;

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

        _penCounter = 0;
        this.setUpTimeline();
    }

    private void setUpTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION), new TimeHandler());
        _timeline = new Timeline(kf);
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
    }

    private class TimeHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            if(_game.gameIsOver()){
                _timeline.stop();
            } else {

                if(!_ghostPen.isEmpty()){
                    _penCounter++;

                    if(_penCounter==Constants.GHOST_PEN_RELEASE_DURATION/Constants.DURATION){
                        _penCounter = 0;
                        Ghost currentGhost = (Ghost) _ghostPen.removeFirst();

                        _map[currentGhost.getRowLocation()][currentGhost.getColLocation()].getSquareElements().remove(currentGhost);

                        currentGhost.setLocation(Constants.BLINKY_STARTING_ROW,Constants.BLINKY_STARTING_COL);

                        _map[currentGhost.getRowLocation()][currentGhost.getColLocation()].getSquareElements().add(currentGhost);
                    }
                }
            }
        }
    }

    public void setPenCounter(int value){
        _penCounter = value;
    }

    public void addToPen(Ghost ghost){

        _ghostPen.addLast(ghost);

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

        _map[ghost.getRowLocation()][ghost.getColLocation()].getSquareElements().add(ghost);
    }
}







