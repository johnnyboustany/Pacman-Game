package pacman;

import cs15.fnl.pacmanSupport.SquareType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private int _frightenedCounter;
    private boolean _frightenedMode;
    public GhostPen _pen;
    private  int _lives;
    private boolean _outOfLives;

    public Game(Pane boardPane, Sidebar sidebar) {
        _boardPane = boardPane;
        _sidebar = sidebar;
        this.setUpBoard();

        //initializing
        _pacmanDirection = Direction.INITIAL;
        _blinkyDirection = Direction.RIGHT;
        _pinkyDirection = Direction.RIGHT;
        _clydeDirection = Direction.RIGHT;
        _inkyDirection = Direction.RIGHT;
        _pacmanCoordinate = new BoardCoordinate(_pacman.getRowLocation(), _pacman.getColLocation(), false);
        _blinkyCoordinate = new BoardCoordinate(_blinky.getRowLocation(), _blinky.getColLocation(), false);
        _pinkyCoordinate = new BoardCoordinate(_pinky.getRowLocation(), _pinky.getColLocation(), false);
        _clydeCoordinate = new BoardCoordinate(_clyde.getRowLocation(), _clyde.getColLocation(), false);
        _inkyCoordinate = new BoardCoordinate(_inky.getRowLocation(), _inky.getColLocation(), false);

        _pen = new GhostPen(_blinky, _pinky, _clyde, _inky,this, _map);

        _mode = Mode.CHASE;
        _outOfLives = false;
        _frightenedMode = false;
        _modeCounter = 0;
        _lives = 3;
        _score = 0;

        this.setUpTimeline();
        _boardPane.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler());
        _boardPane.setFocusTraversable(true);
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

                switch (supportMap[row][col]) {
                    case DOT:
                        Dot dot = new Dot(_boardPane, this);
                        dot.setLocation(row, col);
                        dot.addToPane(_boardPane);
                        _map[row][col].getSquareElements().add(dot);
                        break;
                    case ENERGIZER:
                        Energizer energizer = new Energizer(_boardPane, this);
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
                        _inky = new Ghost("inky", _map, this);
                        _inky.setLocation(row, col);
                        _inky.addToPane(_boardPane);
                        _map[row][col].getSquareElements().add(_inky);

                        _clyde = new Ghost("clyde", _map, this);
                        _clyde.setLocation(row, col + 1);
                        _clyde.addToPane(_boardPane);
                        _map[row][col+1].getSquareElements().add(_clyde);

                        _pinky = new Ghost("pinky", _map, this);
                        _pinky.setLocation(row, col - 1);
                        _pinky.addToPane(_boardPane);
                        _map[row][col-1].getSquareElements().add(_pinky);

                        _blinky = new Ghost("blinky", _map, this);
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

            if(!gameIsOver()){
                switch (keyEvent.getCode()) {
                    case LEFT:
                        if(_pacmanCoordinate.getColumn() > 1 &&_pacman.moveIsValid(0,-1)){
                            _pacmanDirection = Direction.LEFT;
                        }
                        break;
                    case RIGHT:
                        if(_pacmanCoordinate.getColumn() < 21 && _pacman.moveIsValid(0,1)){
                            _pacmanDirection = Direction.RIGHT;
                        }
                        break;
                    case DOWN:
                        if(_pacman.moveIsValid(1,0)){
                            _pacmanDirection = Direction.DOWN;
                        }
                        break;
                    case UP:
                        if(_pacman.moveIsValid(-1,0)){
                            _pacmanDirection = Direction.UP;
                        }
                        break;
                }
                keyEvent.consume();
            }
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
            if(gameIsOver()){
                endGame();

                if(ifNoMoreDots()){
                    setUpGameIsOverLabel();
                }
                _timeline.stop();
            } else {
                this.setMode();

                _pacmanCoordinate = new BoardCoordinate(_pacman.getRowLocation(), _pacman.getColLocation(), false);
                _blinkyCoordinate = new BoardCoordinate(_blinky.getRowLocation(), _blinky.getColLocation(), false);
                _pinkyCoordinate = new BoardCoordinate(_pinky.getRowLocation(), _pinky.getColLocation(), false);
                _clydeCoordinate = new BoardCoordinate(_clyde.getRowLocation(), _clyde.getColLocation(), false);
                _inkyCoordinate = new BoardCoordinate(_inky.getRowLocation(), _inky.getColLocation(), false);

                _pacman.move(_pacmanDirection);
                this.checkForCollision();

                this.moveGhosts();
                this.checkForCollision();

                _sidebar.updateScoreLabel(_score);
                _sidebar.updateLivesLabel(_lives);
            }
        }

        public void setMode(){
            if(!_frightenedMode){
                _modeCounter++;

                if(_modeCounter==20/Constants.DURATION){
                    _mode = _mode.opposite();
                }

                if(_modeCounter==(20+7)/Constants.DURATION){
                    _mode = _mode.opposite();
                    _modeCounter = 0;
                }
            } else {
                _frightenedCounter++;
                _mode = Mode.FRIGHTENED;

                _blinky.changeColor(Color.CYAN);
                _clyde.changeColor(Color.CYAN);
                _pinky.changeColor(Color.CYAN);
                _inky.changeColor(Color.CYAN);

                if(_frightenedCounter == 8/Constants.DURATION){
                    _frightenedCounter = 0;
                    _modeCounter = 0;

                    _blinky.changeBackColor();
                    _clyde.changeBackColor();
                    _pinky.changeBackColor();
                    _inky.changeBackColor();

                    _mode = _mode.CHASE;
                    _frightenedMode = false;
                }
            }
        }
        public void moveGhosts(){
            switch(_mode){

                case CHASE:
                    _blinkyDirection = _blinky.ghostBFS(_blinkyCoordinate, _blinkyDirection,_pacmanCoordinate);
                    _blinky.move(_blinkyDirection);

                    _pinkyDirection = _pinky.ghostBFS(_pinkyCoordinate, _pinkyDirection, new BoardCoordinate(_pacman.getRowLocation()+1,_pacman.getColLocation()-3, true));
                    _pinky.move(_pinkyDirection);

                    _clydeDirection = _clyde.ghostBFS(_clydeCoordinate, _clydeDirection, new BoardCoordinate(_pacman.getRowLocation()-4,_pacman.getColLocation(), true));
                    _clyde.move(_clydeDirection);

                    _inkyDirection = _inky.ghostBFS(_inkyCoordinate, _inkyDirection, new BoardCoordinate(_pacman.getRowLocation(),_pacman.getColLocation()+2,true));
                    _inky.move(_inkyDirection);
                    break;
                case SCATTER:
                    _blinkyDirection = _blinky.ghostBFS(_blinkyCoordinate, _blinkyDirection, new BoardCoordinate(0,0, true));
                    _blinky.move(_blinkyDirection);

                    _pinkyDirection = _pinky.ghostBFS(_pinkyCoordinate, _pinkyDirection, new BoardCoordinate(0,Constants.MAZE_DIMENSION, true));
                    _pinky.move(_pinkyDirection);

                    _clydeDirection = _clyde.ghostBFS(_clydeCoordinate, _clydeDirection, new BoardCoordinate(Constants.MAZE_DIMENSION,0, true));
                    _clyde.move(_clydeDirection);

                    _inkyDirection = _inky.ghostBFS(_inkyCoordinate, _inkyDirection, new BoardCoordinate(Constants.MAZE_DIMENSION,Constants.MAZE_DIMENSION, true));
                    _inky.move(_inkyDirection);
                    break;
                case FRIGHTENED:
                    _blinkyDirection = _blinky.randomDirection(_blinkyCoordinate, _blinkyDirection);
                    _blinky.move(_blinkyDirection);

                    _pinkyDirection = _pinky.randomDirection(_pinkyCoordinate, _pinkyDirection);
                    _pinky.move(_pinkyDirection);

                    _clydeDirection = _clyde.randomDirection(_clydeCoordinate, _clydeDirection);
                    _clyde.move(_clydeDirection);

                    _inkyDirection = _inky.randomDirection(_inkyCoordinate, _inkyDirection);
                    _inky.move(_inkyDirection);
                    break;
            }
        }

        public void checkForCollision() {
            int pacmanRow = _pacmanCoordinate.getRow();
            int pacmanColumn = _pacmanCoordinate.getColumn();

            MazeSquare currentSquare = _map[pacmanRow][pacmanColumn];

            while(currentSquare.getSquareElements().size() != 0){
                Collidable collidable;
                collidable = (Collidable) currentSquare.getSquareElements().remove(currentSquare.getSquareElements().size()-1);
                collidable.collide();
            }
        }
    }

    public boolean ifNoMoreDots() {
        for (int row = 0; row < Constants.MAZE_DIMENSION; row++) {
            for (int col = 0; col < Constants.MAZE_DIMENSION; col++) {

                for(int i = 0; i < _map[row][col].getSquareElements().size(); i++){
                    if(_map[row][col].containsDot(i) || _map[row][col].containsEnergizer(i)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean gameIsOver(){
        if(ifNoMoreDots() || _outOfLives){
            return true;
        } else {
            return false;
        }
    }

    public boolean isInFrightenedMode() {
        return _frightenedMode;
    }

    public void setFrightenedMode(){
        _frightenedMode = true;
    }

    public void addToScore(int scoreIncrement){
        _score = _score + scoreIncrement;
    }

    public void killPacman(){
        if(_lives > 1){
            _lives--;
            this.resetGame();
        } else {
            _lives--;
            endGame();
            this.setUpGameIsOverLabel();

            _outOfLives = true;
        }
    }

    public void resetGame(){
        _map[_blinky.getRowLocation()][_blinky.getColLocation()].getSquareElements().remove(_blinky);
        _map[_pinky.getRowLocation()][_pinky.getColLocation()].getSquareElements().remove(_pinky);
        _map[_clyde.getRowLocation()][_clyde.getColLocation()].getSquareElements().remove(_clyde);
        _map[_inky.getRowLocation()][_inky.getColLocation()].getSquareElements().remove(_inky);

        _pacmanDirection = Direction.INITIAL;
        _blinkyDirection = Direction.RIGHT;
        _pinkyDirection = Direction.RIGHT;
        _clydeDirection = Direction.RIGHT;
        _inkyDirection = Direction.RIGHT;

        _pacman.setLocation(Constants.PACMAN_STARTING_ROW,Constants.PACMAN_STARTING_COL);
        _blinky.setLocation(Constants.BLINKY_STARTING_ROW,Constants.BLINKY_STARTING_COL);

        _map[_blinky.getRowLocation()][_blinky.getColLocation()].getSquareElements().add(_blinky);

        _pen.setPenCounter(0);
        _pen.addToPen(_pinky);
        _pen.addToPen(_inky);
        _pen.addToPen(_clyde);
    }

    public void endGame(){
        _blinky.changeBackColor();
        _clyde.changeBackColor();
        _pinky.changeBackColor();
        _inky.changeBackColor();

        _pacman.setLocation(Constants.PACMAN_STARTING_ROW,Constants.PACMAN_STARTING_COL);
        _blinky.setLocation(Constants.BLINKY_STARTING_ROW,Constants.BLINKY_STARTING_COL);
        _inky.setLocation(Constants.INKY_STARTING_ROW,Constants.INKY_STARTING_COL);
        _pinky.setLocation(Constants.PINKY_STARTING_ROW,Constants.PINKY_STARTING_COL);
        _clyde.setLocation(Constants.CLYDE_STARTING_ROW,Constants.CLYDE_STARTING_COL);
    }

    public GhostPen getPen(){
        return _pen;
    }

    private void setUpGameIsOverLabel(){
        Label label = new Label();
        label.setText("Game Over!");
        label.setLayoutX(235);
        label.setLayoutY(300);
        label.setTextFill(Color.rgb(255, 255, 0));
        label.setFont(new Font("Arial", Constants.GAME_IS_OVER_FONT_SIZE));
        _boardPane.getChildren().add(label);
    }
}








