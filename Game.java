package pacman;

import cs15.fnl.pacmanSupport.SquareType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Game {
    private MazeSquare[][] _map;
    private Pane _boardPane;
    private Pacman _pacman;
    private Timeline _timeline;
    private Direction _pacmanDirection;
    private BoardCoordinate _pacmanCoordinate;
    private Direction _blinkyDirection;
    private BoardCoordinate _blinkyCoordinate;
    private Direction _pinkyDirection;
    private BoardCoordinate _pinkyCoordinate;
    private Direction _clydeDirection;
    private BoardCoordinate _clydeCoordinate;
    private Direction _inkyDirection;
    private BoardCoordinate _inkyCoordinate;
    private int _score;
    private Sidebar _sidebar;
    private Ghost _blinky;
    private Ghost _inky;
    private Ghost _pinky;
    private Ghost _clyde;
    private Mode _mode;
    private int _modeCounter;
    private int _scatterCounter;
    private int _chaseCounter;
    private int _frightenedCounter;

    public Game(Pane boardPane, Sidebar sidebar) {
        _modeCounter = 0;
        //_mode = Mode.SCATTER;
        _mode = Mode.FRIGHTENED;
        _boardPane = boardPane;
        _sidebar = sidebar;
        this.setUpBoard();

        new GhostPen(_blinky, _pinky, _clyde, _inky);

        _pacmanDirection = Direction.INITIAL;
        _blinkyDirection = Direction.RIGHT;
        _pinkyDirection = Direction.RIGHT;
        _clydeDirection = Direction.RIGHT;
        _inkyDirection = Direction.RIGHT;


        this.setUpTimeline();

        _boardPane.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler());
        _boardPane.setFocusTraversable(true);

        _score = 0;
    }

    public void setUpBoard() {

        SquareType[][] supportMap = cs15.fnl.pacmanSupport.SupportMap.getSupportMap();
        _map = new MazeSquare[Constants.MAZE_DIMENSION][Constants.MAZE_DIMENSION];

        for (int row = 0; row < Constants.MAZE_DIMENSION; row++) {
            for (int col = 0; col < Constants.MAZE_DIMENSION; col++) {

                MazeSquare square = new MazeSquare();

                square.setXLocation(col * Constants.SQUARE_WIDTH);
                square.setYLocation(row * Constants.SQUARE_WIDTH);

                _map[row][col] = square;
            }
        }

        for (int row = 0; row < Constants.MAZE_DIMENSION; row++) {
            for (int col = 0; col < Constants.MAZE_DIMENSION; col++) {
                switch (supportMap[row][col]) {
                    case DOT:
                        Dot dot = new Dot(_boardPane);
                        dot.setLocation(row, col);
                        dot.addToPane(_boardPane);
                        _map[row][col].getSquareElements().add(dot);
                        break;
                    case ENERGIZER:
                        Energizer energizer = new Energizer(_boardPane);
                        energizer.setLocation(row, col);
                        energizer.addToPane(_boardPane);
                        _map[row][col].getSquareElements().add(energizer);
                        break;
                }
            }
        }

                for (int row = 0; row < Constants.MAZE_DIMENSION; row++) {
            for (int col = 0; col < Constants.MAZE_DIMENSION; col++) {

                switch (supportMap[row][col]) {
                    case WALL:
                        _map[row][col].setColor(Color.DARKBLUE);
                        _map[row][col].addToPane(_boardPane);
                        break;
                    case GHOST_START_LOCATION:
                        _inky = new Ghost("inky", _boardPane, _map);
                        _inky.setLocation(row, col);
                        _inky.addToPane(_boardPane);
                        _map[row][col].getSquareElements().add(_inky);

                        _clyde = new Ghost("clyde", _boardPane, _map);
                        _clyde.setLocation(row, col + 1);
                        _clyde.addToPane(_boardPane);
                        _map[row][col+1].getSquareElements().add(_clyde);

                        _pinky = new Ghost("pinky", _boardPane, _map);
                        _pinky.setLocation(row, col - 1);
                        _pinky.addToPane(_boardPane);
                        _map[row][col-1].getSquareElements().add(_pinky);

                        _blinky = new Ghost("blinky", _boardPane, _map);
                        _blinky.setLocation(row -2, col);
                        _blinky.addToPane(_boardPane);
                        _map[row-2][col].getSquareElements().add(_blinky);
                        break;
                    case PACMAN_START_LOCATION:
                        _pacman = new Pacman(_map);
                        _pacman.setLocation(row, col);
                        _pacman.addToPane(_boardPane);
                        break;
                }
            }
        }
    }

    private class KeyHandler implements EventHandler<KeyEvent> {

        public KeyHandler() {

        }

        public void handle(KeyEvent keyEvent) {
            switch (keyEvent.getCode()) {
                case LEFT:
                    _pacmanDirection = Direction.LEFT;
                    break;
                case RIGHT:
                    _pacmanDirection = Direction.RIGHT;
                    break;
                case DOWN:
                    _pacmanDirection = Direction.DOWN;
                    break;
                case UP:
                    _pacmanDirection = Direction.UP;
                    break;
            }
            keyEvent.consume();
        }
    }

    /**
     * This method is used to setup and play the Timeline so the game can start.
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

        /**
         * The handle method controls the y-movement/falling
         * of the piece, adds landed squares to the board, creates new pieces,
         * clears full lines, and checks if the game is over.
         */
        public void handle(ActionEvent event) {

           // _modeCounter++;

            //different counters for each mode

          //  if(_modeCounter == 32){
            //    _mode = Mode.CHASE;
           // }

           // if(_modeCounter == 32+80){
           //     _mode = Mode.SCATTER;
          //      _modeCounter = 0;
          //  }

            _blinkyCoordinate = new BoardCoordinate(_blinky.getRowLocation(), _blinky.getColLocation(), false);
            _pacmanCoordinate = new BoardCoordinate(_pacman.getRowLocation(), _pacman.getColLocation(), false);
            _pinkyCoordinate = new BoardCoordinate(_pinky.getRowLocation(), _pinky.getColLocation(), false);
            _clydeCoordinate = new BoardCoordinate(_clyde.getRowLocation(), _clyde.getColLocation(), false);
            _inkyCoordinate = new BoardCoordinate(_inky.getRowLocation(), _inky.getColLocation(), false);

            _pacman.move(_pacmanDirection);
            this.checkForCollision();

                if(_mode==Mode.CHASE){
                    if(!_blinky.isCollided()){
                        _blinkyDirection = _blinky.ghostBFS(_blinkyCoordinate, _blinkyDirection,_pacmanCoordinate);
                        _blinky.move(_blinkyDirection);
                    }

                    if(!_pinky.isCollided()){
                        _pinkyDirection = _pinky.ghostBFS(_pinkyCoordinate, _pinkyDirection, new BoardCoordinate(_pacman.getRowLocation()+1,_pacman.getColLocation()-3, true));
                        _pinky.move(_pinkyDirection);
                    }

                    if(!_clyde.isCollided()){
                        _clydeDirection = _clyde.ghostBFS(_clydeCoordinate, _clydeDirection, new BoardCoordinate(_pacman.getRowLocation()-4,_pacman.getColLocation(), true));
                        _clyde.move(_clydeDirection);
                    }

                    if(!_inky.isCollided()){
                        _inkyDirection = _inky.ghostBFS(_inkyCoordinate, _inkyDirection, new BoardCoordinate(_pacman.getRowLocation(),_pacman.getColLocation()+2,true));
                        _inky.move(_inkyDirection);
                    }

                } else if (_mode==Mode.SCATTER){

                    if(!_blinky.isCollided()){
                        _blinkyDirection = _blinky.ghostBFS(_blinkyCoordinate, _blinkyDirection, new BoardCoordinate(0,0, true));
                        _blinky.move(_blinkyDirection);
                    }
                    if(!_pinky.isCollided()){
                        _pinkyDirection = _pinky.ghostBFS(_pinkyCoordinate, _pinkyDirection, new BoardCoordinate(0,23, true));
                        _pinky.move(_pinkyDirection);
                    }
                    if(!_clyde.isCollided()){
                        _clydeDirection = _clyde.ghostBFS(_clydeCoordinate, _clydeDirection, new BoardCoordinate(23,0, true));
                        _clyde.move(_clydeDirection);
                    }
                    if(!_inky.isCollided()){
                        _inkyDirection = _inky.ghostBFS(_inkyCoordinate, _inkyDirection, new BoardCoordinate(23,23, true));
                        _inky.move(_inkyDirection);
                    }

                } else if (_mode == Mode.FRIGHTENED){

                    if(!_blinky.isCollided()){
                        _blinkyDirection = _blinky.randomDirection(_blinkyCoordinate, _blinkyDirection);
                        _blinky.move(_blinkyDirection);
                    }

                    if(!_pinky.isCollided()){
                        _pinkyDirection = _pinky.randomDirection(_pinkyCoordinate, _pinkyDirection);
                        _pinky.move(_pinkyDirection);
                    }

                    if(!_clyde.isCollided()){
                        _clydeDirection = _clyde.randomDirection(_clydeCoordinate, _clydeDirection);
                        _clyde.move(_clydeDirection);
                    }

                    if(!_inky.isCollided()){
                        _inkyDirection = _inky.randomDirection(_inkyCoordinate, _inkyDirection);
                        _inky.move(_inkyDirection);
                    }
                }

            this.checkForCollision();
            _sidebar.updateScoreLabel(_score);
        }

        public void checkForCollision() {
            int pacmanRow = _pacmanCoordinate.getRow();
            int pacmanColumn = _pacmanCoordinate.getColumn();

            MazeSquare currentSquare = _map[pacmanRow][pacmanColumn];

            for (int i = 0; i < currentSquare.getSquareElements().size(); i++) {
                if (currentSquare.containsDot(i) || currentSquare.containsEnergizer(i) || currentSquare.containsGhost(i)) {
                    currentSquare.getCollidable(i).collide();
                    this.incrementScore(currentSquare,i);
                }
             }

            currentSquare.getSquareElements().clear();
        }

        public void incrementScore(MazeSquare currentSquare, int i ){

            if(currentSquare.containsDot(i)){
                _score = _score+10;
            }

            if(currentSquare.containsEnergizer(i)){
                _score = _score+100;
            }

            if(currentSquare.containsGhost(i)){
                _score = _score+200;
            }
        }
    }
}






