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
    private Ghost _blinky;
    private Ghost _pinky;
    private Ghost _clyde;
    private Ghost _inky;
    private LinkedList _ghostPen;
    private Timeline _timeline;
    private boolean _gameIsOver;
    private Game _game;

    public GhostPen(Ghost blinky, Ghost pinky, Ghost clyde, Ghost inky, Game game){
        _game = game;
        _gameIsOver = false;
        _blinky = blinky;
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
            }
                if(!_ghostPen.isEmpty()){
                    _penCounter++;
                }

                if(_blinky.isCollided()){
                    _ghostPen.addLast(_blinky);
                    _blinky.setLocation(10,10);
                    _blinky.noLongerCollided();
                }

                if(_clyde.isCollided()){
                    _ghostPen.addLast(_clyde);
                    _clyde.setLocation(10,10);
                    _clyde.noLongerCollided();
                }

                if(_pinky.isCollided()){
                    _ghostPen.addLast(_pinky);
                    _pinky.setLocation(10,10);
                    _pinky.noLongerCollided();
                }

                if(_inky.isCollided()){
                    _ghostPen.addLast(_inky);
                    _inky.setLocation(10,10);
                    _inky.noLongerCollided();
                }

                if(!_gameIsOver){
                    if(_penCounter==16){
                        _penCounter = 0;
                        Ghost currentGhost = (Ghost) _ghostPen.removeFirst();
                        currentGhost.setLocation(8,10);
                    }
                }

        }
    }
}







