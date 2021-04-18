package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.LinkedList;

public class GhostPen {

    int _penCounter;
    private Ghost _blinky;
    private Ghost _pinky;
    private Ghost _clyde;
    private Ghost _inky;
    private LinkedList _ghostPen;

    public GhostPen(Ghost blinky, Ghost pinky, Ghost clyde, Ghost inky){
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
        Timeline timeline = new Timeline(kf);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private class TimeHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
           if(!_ghostPen.isEmpty()){
               _penCounter++;
           }

           if(_penCounter==16){
               _penCounter = 0;
                Ghost currentGhost = (Ghost) _ghostPen.removeFirst();
                currentGhost.setLocation(8,11);
            }

          // if(_ghostPen.contains(_pinky)){
          //     _pinkyCoordinate = new BoardCoordinate(_pinky.getRowLocation(), _pinky.getColLocation(), false);
          //     _pinkyDirection = _pinky.ghostBFS(_pinkyCoordinate, _pinkyDirection, new BoardCoordinate(17,10, true));
          //     _pinky.move(_pinkyDirection);
          // }

           // _clydeCoordinate = new BoardCoordinate(_clyde.getRowLocation(), _clyde.getColLocation(), false);
          //  _clydeDirection = _clyde.ghostBFS(_clydeCoordinate, _clydeDirection, new BoardCoordinate(9,3, true));
          //  _clyde.move(_clydeDirection);

          //  _inkyCoordinate = new BoardCoordinate(_inky.getRowLocation(), _inky.getColLocation(), false);
          //  _inkyDirection = _inky.ghostBFS(_inkyCoordinate, _inkyDirection, new BoardCoordinate(5,4, true));
          //  _inky.move(_inkyDirection);

        }

    }




}







