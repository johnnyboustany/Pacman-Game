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

/**
 * The Game class is the top-level logic class. It stores
 * boardPane (which is passed in from the PaneOrganizer class)
 * in an instance variable. It also stores a 2D Array
 * of MazeSquares that is used to represent the board in an instance variable.
 * Also, it sets up the board with the 4 ghosts and adds 3 of them
 * to the ghost pen.
 */
public class Game {
    private MazeSquare[][] _map;
    private Pane _boardPane;
    private Sidebar _sidebar;
    private Timeline _timeline;
    private Pacman _pacman;
    private Direction _pacmanDirection;
    private BoardCoordinate _pacmanCoordinate;
    private Ghost _blinky;
    private Direction _blinkyDirection;
    private BoardCoordinate _blinkyCoordinate;
    private Ghost _pinky;
    private Direction _pinkyDirection;
    private BoardCoordinate _pinkyCoordinate;
    private Ghost _clyde;
    private Direction _clydeDirection;
    private BoardCoordinate _clydeCoordinate;
    private Ghost _inky;
    private Direction _inkyDirection;
    private BoardCoordinate _inkyCoordinate;
    public GhostPen _pen;
    private Mode _mode;
    private int _score;
    private int _lives;
    private int _modeCounter;
    private int _frightenedCounter;
    private boolean _frightenedMode;
    private boolean _outOfLives;

    /**
     * This is the Game class constructor, it takes in the boardPane
     * and assigns it to the declared instance variable. It also
     * instantiates the sidebar, which helps setup relevant labels. It also
     * instantiates the 2D Array for the board and sets up the board via
     * a helper method. It instantiates the pen and adds the 3 ghosts to it.
     * It also initializes the lives and score counter.
     */
    public Game(Pane boardPane, Sidebar sidebar) {
        _boardPane = boardPane;
        _sidebar = sidebar;
        this.setUpBoard();

        //initializing the direction & coordinates of pacman and the 4 ghosts
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

        _pen = new GhostPen(_pinky, _clyde, _inky,this, _map);

        _mode = Mode.CHASE; // the game starts out in chase mode
        _outOfLives = false; // pacman is not out of lives at the start of the game
        _frightenedMode = false; // frightened mode is only activated when this variable is set to true
        _modeCounter = 0; // initializing counter used to switch between chase & target mode

        _lives = Constants.INITIAL_LIVES;
        _score = Constants.INITIAL_SCORE;

        this.setUpTimeline();
        _boardPane.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler());
        _boardPane.setFocusTraversable(true);
    }

    /**
     * This method uses the supportMap to set all elements
     * in their appropriate positions on the map.
     */
    public void setUpBoard() {
        SquareType[][] supportMap = cs15.fnl.pacmanSupport.SupportMap.getSupportMap();
        _map = new MazeSquare[Constants.MAZE_DIMENSION][Constants.MAZE_DIMENSION];

        for (int row = 0; row < Constants.MAZE_DIMENSION; row++) {
            for (int col = 0; col < Constants.MAZE_DIMENSION; col++) {
                MazeSquare square = new MazeSquare();

                square.setXLocation(col * Constants.SQUARE_WIDTH);
                square.setYLocation(row * Constants.SQUARE_WIDTH);

                _map[row][col] = square; // creating new squares and placing each at a position on the map

                // placing dots and energizer first, so that they appear under the ghosts and pacman  on map
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

        // placing walls, ghosts and pacman on map
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
                        _clyde.setLocation(row, col + Constants.CLYDE_SHIFTED);
                        _clyde.addToPane(_boardPane);
                        _map[row][col+ Constants.CLYDE_SHIFTED].getSquareElements().add(_clyde);

                        _pinky = new Ghost("pinky", _map, this);
                        _pinky.setLocation(row, col - Constants.PINKY_SHIFTED);
                        _pinky.addToPane(_boardPane);
                        _map[row][col- Constants.PINKY_SHIFTED].getSquareElements().add(_pinky);

                        _blinky = new Ghost("blinky", _map, this);
                        _blinky.setLocation(row - Constants.BLINKY_SHIFTED, col);
                        _blinky.addToPane(_boardPane);
                        _map[row-Constants.BLINKY_SHIFTED][col].getSquareElements().add(_blinky);
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

    /**
     * The private class KeyHandler implements an event handler and knows how to handle
     * events of type KeyEvent. This assigns a direction to pacman's direction instance variable
     * that will be passed into pacman's move method at every time-step.
     *
     */
    private class KeyHandler implements EventHandler<KeyEvent> {
        /**
         * The handle method determines what should happen
         * when the user presses the different keys. It only
         * allows key interaction when the game is not over.
         *
         */
        public void handle(KeyEvent keyEvent) {
            if(!gameIsOver()){
                switch (keyEvent.getCode()) {
                    case LEFT:
                        // a condition is placed to prevent the user from pressing
                        // the left key when the pacman is already moving left and approaching the left tunnel square
                        if(_pacmanCoordinate.getColumn() > Constants.PACMAN_X_LOWER &&_pacman.moveIsValid(0,-Constants.MOVE_OFFSET)){
                            _pacmanDirection = Direction.LEFT;
                        }
                        break;
                    case RIGHT:
                        // a condition is placed to prevent the user from pressing
                        // the right key when the pacman is already moving right and approaching the right tunnel square
                        if(_pacmanCoordinate.getColumn() < Constants.PACMAN_X_UPPER && _pacman.moveIsValid(0,Constants.MOVE_OFFSET)){
                            _pacmanDirection = Direction.RIGHT;
                        }
                        break;
                    case DOWN:
                        if(_pacman.moveIsValid(Constants.MOVE_OFFSET,0)){
                            _pacmanDirection = Direction.DOWN;
                        }
                        break;
                    case UP:
                        if(_pacman.moveIsValid(-Constants.MOVE_OFFSET,0)){
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
         *
         */
        public void handle(ActionEvent event) {
            if(gameIsOver()){
                // if the dots/energizers are all collided with or pacman lost all its 3 lives

                Game.this.endGame(); // places all elements back in starting position

                // the game is over label is set up at the start of the handle method
                // if there are no more dots/energizers whereas, for the case when pacman loses
                // all its lives, the label is setup exactly when the pacman loses its last life
                if(noMoreDotsOrEnergizers()){
                    Game.this.setUpGameIsOverLabel();
                }
                _timeline.stop();
            } else {

                // sets the current mode by alternating between chase & target
                // mode and checking for frightened mode
                this.setMode();

                // updates the coordinates for pacman and the ghosts based on their current locations
                _pacmanCoordinate = new BoardCoordinate(_pacman.getRowLocation(), _pacman.getColLocation(), false);
                _blinkyCoordinate = new BoardCoordinate(_blinky.getRowLocation(), _blinky.getColLocation(), false);
                _pinkyCoordinate = new BoardCoordinate(_pinky.getRowLocation(), _pinky.getColLocation(), false);
                _clydeCoordinate = new BoardCoordinate(_clyde.getRowLocation(), _clyde.getColLocation(), false);
                _inkyCoordinate = new BoardCoordinate(_inky.getRowLocation(), _inky.getColLocation(), false);

                _pacman.move(_pacmanDirection); // pacman is moved based on direction set by the user Key interaction
                this.checkForCollision();
                this.moveGhosts();
                // edge case is avoided by checking for collision before and after the ghosts ae moved
                this.checkForCollision();

                // score and lives counts are updated at every time-step
                _sidebar.updateScoreLabel(_score);
                _sidebar.updateLivesLabel(_lives);
            }
        }

        /**
         * Mode is set based on the boolean variable for frightened Mode.
         * A mode counter is used to switch between chase and target modes.
         * A frightened mode counter is used to set how long the mode lasts.
         */
        private void setMode(){
            if(!_frightenedMode){
                // if the pacman doesn't eat the energizer, the mode counter increments itself
                _modeCounter++;

                // the game starts in chase mode and is changed to scatter mode after
                // a specific duration of time
                if(_modeCounter==Constants.CHASE_MODE_DURATION/Constants.DURATION){
                    _mode = _mode.opposite();
                }

                // the game is set back to chase mode
                if(_modeCounter==(Constants.CHASE_MODE_DURATION+Constants.SCATTER_MODE_DURATION)/Constants.DURATION){
                    _mode = _mode.opposite();
                    _modeCounter = 0;
                }
            } else {
                // if the pacman eats the energizer, the frightened mode counter increments itself
                _frightenedCounter++;
                _mode = Mode.FRIGHTENED;

                // after a specific time duration passes, the mode is set back to chase
                // and the alternating cycle between chase and scatter continues until another
                // energizer is eaten
                if(_frightenedCounter == (Constants.FRIGHTENED_MODE_DURATION)/Constants.DURATION){
                    _frightenedCounter = 0;
                    _modeCounter = 0;

                    _mode = _mode.CHASE;
                    _frightenedMode = false;
                }
            }
        }

        /**
         * Collisions are checked for by retrieving the arraylist of the
         * MazeSquare that pacman is currently located on and looping through it
         * to check for any object that implements the collidable interface.
         * The object is removed from the arraylist and is instructed to execute its
         * collide method.
         */
        private void checkForCollision() {
            int pacmanRow = _pacmanCoordinate.getRow();
            int pacmanColumn = _pacmanCoordinate.getColumn();

            MazeSquare currentSquare = _map[pacmanRow][pacmanColumn];

            while(currentSquare.getSquareElements().size() != 0){
                Collidable collidable = (Collidable) currentSquare.getSquareElements().remove(currentSquare.getSquareElements().size()-1);
                collidable.collide();
            }
        }

        /**
         * The directions that the ghosts are moved in are set
         * according to the mode that the game is currently in.
         * The set directions are then passed into the ghost move methods.
         */
        private void moveGhosts(){
            switch(_mode){
                case CHASE:
                    // target location is either pacman or a coordinate close to pacman
                    _blinkyDirection = _blinky.ghostBFS(_blinkyCoordinate, _blinkyDirection,_pacmanCoordinate);
                    _pinkyDirection = _pinky.ghostBFS(_pinkyCoordinate, _pinkyDirection, new BoardCoordinate(_pacman.getRowLocation()+Constants.PINKY_CHASE_OFFSET_ROW,_pacman.getColLocation()-Constants.PINKY_CHASE_OFFSET_COL, true));
                    _clydeDirection = _clyde.ghostBFS(_clydeCoordinate, _clydeDirection, new BoardCoordinate(_pacman.getRowLocation()-Constants.CLYDE_CHASE_OFFSET_ROW,_pacman.getColLocation(), true));
                    _inkyDirection = _inky.ghostBFS(_inkyCoordinate, _inkyDirection, new BoardCoordinate(_pacman.getRowLocation(),_pacman.getColLocation()+Constants.INKY_CHASE_OFFSET_COL,true));
                    break;
                case SCATTER:
                    // target location is a corner of the board
                    _blinkyDirection = _blinky.ghostBFS(_blinkyCoordinate, _blinkyDirection, new BoardCoordinate(0,0, true));
                    _pinkyDirection = _pinky.ghostBFS(_pinkyCoordinate, _pinkyDirection, new BoardCoordinate(0,Constants.MAZE_DIMENSION, true));
                    _clydeDirection = _clyde.ghostBFS(_clydeCoordinate, _clydeDirection, new BoardCoordinate(Constants.MAZE_DIMENSION,0, true));
                    _inkyDirection = _inky.ghostBFS(_inkyCoordinate, _inkyDirection, new BoardCoordinate(Constants.MAZE_DIMENSION,Constants.MAZE_DIMENSION, true));
                    break;
                case FRIGHTENED:
                    // target location is randomly-chosen based on the valid direction that is randomly-chosen
                    _blinkyDirection = _blinky.ghostFrightened(_blinkyCoordinate, _blinkyDirection);
                    _pinkyDirection = _pinky.ghostFrightened(_pinkyCoordinate, _pinkyDirection);
                    _clydeDirection = _clyde.ghostFrightened(_clydeCoordinate, _clydeDirection);
                    _inkyDirection = _inky.ghostFrightened(_inkyCoordinate, _inkyDirection);
                    break;
            }

            _blinky.move(_blinkyDirection);
            _pinky.move(_pinkyDirection);
            _clyde.move(_clydeDirection);
            _inky.move(_inkyDirection);
        }
    }
    
    /**
     * This private method loops through the 2D map array to check
     * the arraylist of each MazeSquare for the presence of
     * a dot or energizer at an index. It returns true
     * if no dots/energizers are found and is used to check
     * whether the game is over.
     */
    private boolean noMoreDotsOrEnergizers() {
        for (int row = 0; row < Constants.MAZE_DIMENSION; row++) {
            for (int col = 0; col < Constants.MAZE_DIMENSION; col++) {
                for(int i = 0; i < _map[row][col].getSquareElements().size(); i++){
                    if(_map[row][col].containsDotOrEnergizer(i)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * This method returns true when no more dots/energizers
     * remain on the map and when the pacman is out of lives.
     * It is public as it used in the GhostPen class.
     */
    public boolean gameIsOver(){
        if(noMoreDotsOrEnergizers() || _outOfLives){
            return true;
        } else {
            return false;
        }
    }

    /**
     * This public method checks whether the frightened Mode boolean
     * variable is set to true. It is used by the Ghost class in its
     * collide method.
     */
    public boolean isInFrightenedMode() {
        return _frightenedMode;
    }

    /**
     * This public method activates frightened mode.
     * It is used in the Energizer class in its
     * collide method.
     */
    public void setFrightenedMode(){
        _frightenedMode = true;
    }

    /**
     * This public method is used by dots, energizers and ghosts
     * to increment the score.
     */
    public void addToScore(int scoreIncrement){
        _score = _score + scoreIncrement;
    }

    /**
     * This public method is executed by the ghost's collide method
     * when pacman collides with a ghost and the ghost is not in
     * frightened mode.
     */
    public void killPacman(){
        // if the lives counter is not on
        // the pacman's last life, a life is deducted 
        // and the game is reset
        if(_lives > Constants.LAST_LIFE){
            _lives--;
            this.resetGame();

        // if the lives counter is on
        // the pacman's last life, a life is deducted
        // and the game is ended.
        } else {
            _lives--;
            endGame();

            // the label is set up now as opposed to in the TimeHandler handle method
            // as this ensures that the label is displayed exactly when the collision occurs
            this.setUpGameIsOverLabel();

            // allows gameIsOver method to recognize that pacman has no remaining lives
            _outOfLives = true;
        }
    }

    /**
     * This private method is executed to reset the game when
     * pacman loses a life (and when it is not out of lives yet).
     */
    private void resetGame(){
        // before the ghosts are set to new positions,
        // all ghosts get removed from the arraylists of their current squares
        _map[_blinky.getRowLocation()][_blinky.getColLocation()].getSquareElements().remove(_blinky);
        _map[_pinky.getRowLocation()][_pinky.getColLocation()].getSquareElements().remove(_pinky);
        _map[_clyde.getRowLocation()][_clyde.getColLocation()].getSquareElements().remove(_clyde);
        _map[_inky.getRowLocation()][_inky.getColLocation()].getSquareElements().remove(_inky);

        // all directions are re-initialized
        _pacmanDirection = Direction.INITIAL;
        _blinkyDirection = Direction.RIGHT;
        _pinkyDirection = Direction.RIGHT;
        _clydeDirection = Direction.RIGHT;
        _inkyDirection = Direction.RIGHT;

        _pacman.setLocation(Constants.PACMAN_STARTING_ROW,Constants.PACMAN_STARTING_COL);

        // blinky is set to its position outside of the pen and is added to the arraylist
        // of this position
        _blinky.setLocation(Constants.BLINKY_STARTING_ROW,Constants.BLINKY_STARTING_COL);
        _map[_blinky.getRowLocation()][_blinky.getColLocation()].getSquareElements().add(_blinky);

        // counter for pen is restarted so that the ghosts get released one by one
        _pen.setPenCounter(0);

        // pinky, inky and clyde are sent back to the pen
        _pen.addToPen(_pinky);
        _pen.addToPen(_inky);
        _pen.addToPen(_clyde);
    }

    /**
     * This private method is executed to end the game when
     * pacman loses its last life.
     */
    private void endGame(){

        // this ensures that the game ends with the ghosts
        // displaying their original colors (and not cyan blue,
        // if the game ends wih the ghosts in frightened mode)
        _blinky.setToOriginalColor();
        _clyde.setToOriginalColor();
        _pinky.setToOriginalColor();
        _inky.setToOriginalColor();

        // ghosts are returned to their starting positions
        _pacman.setLocation(Constants.PACMAN_STARTING_ROW,Constants.PACMAN_STARTING_COL);
        _blinky.setLocation(Constants.BLINKY_STARTING_ROW,Constants.BLINKY_STARTING_COL);
        _inky.setLocation(Constants.INKY_STARTING_ROW,Constants.INKY_STARTING_COL);
        _pinky.setLocation(Constants.PINKY_STARTING_ROW,Constants.PINKY_STARTING_COL);
        _clyde.setLocation(Constants.CLYDE_STARTING_ROW,Constants.CLYDE_STARTING_COL);
    }

    /**
     * This private method sets up the game is over label
     * and adds it to the boardPane.
     */
    private void setUpGameIsOverLabel(){
        Label label = new Label();
        label.setText("Game Over!");
        label.setLayoutX(Constants.GAME_IS_OVER_X);
        label.setLayoutY(Constants.GAME_IS_OVER_Y);
        label.setTextFill(Color.rgb(255, 255, 0));
        label.setFont(new Font("Arial", Constants.GAME_IS_OVER_FONT_SIZE));
        _boardPane.getChildren().add(label);
    }

    /**
     * Instead of the ghost class being associated with the pen,
     * this public method allows the ghost to access the pen
     * (and add itself to it).
     */
    public GhostPen getPen(){
        return _pen;
    }
}








