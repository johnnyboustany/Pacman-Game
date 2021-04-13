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
    private Direction _blinkyDirection;
    private BoardCoordinate _pacmanCoordinate;
    private BoardCoordinate _blinkyCoordinate;

    private int _score;
    private Sidebar _sidebar;
    private Ghost _blinky;

    public Game(Pane boardPane, Sidebar sidebar) {

        _boardPane = boardPane;
        _sidebar = sidebar;
        this.setUpBoard();

        _pacmanDirection = Direction.NULL;
        _blinkyDirection = Direction.RIGHT;

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
                    case WALL:
                        _map[row][col].setColor(Color.DARKBLUE);
                        _map[row][col].addToPane(_boardPane);
                        break;
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
                    case GHOST_START_LOCATION:
                        Ghost inky = new Ghost("inky", _boardPane, _map);
                        inky.setLocation(row, col);
                        inky.addToPane(_boardPane);
                        _map[row][col].getSquareElements().add(inky);

                        Ghost clyde = new Ghost("clyde", _boardPane, _map);
                        clyde.setLocation(row, col + 1);
                        clyde.addToPane(_boardPane);
                        _map[row][col+1].getSquareElements().add(clyde);

                        Ghost pinky = new Ghost("pinky", _boardPane, _map);
                        pinky.setLocation(row, col - 1);
                        pinky.addToPane(_boardPane);
                        _map[row][col-1].getSquareElements().add(pinky);

                        _blinky = new Ghost("blinky", _boardPane, _map);
                        _blinky.setLocation(row -2, col);
                        _blinky.addToPane(_boardPane);
                        _map[row-2][col].getSquareElements().add(_blinky);

                        // row+1 col+4
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
                   // _direction = _direction.opposite();
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

            _blinkyCoordinate = new BoardCoordinate(_blinky.getRowLocation(), _blinky.getColLocation(), false);
            _pacmanCoordinate = new BoardCoordinate(_pacman.getRowLocation(), _pacman.getColLocation(), false);

            _pacman.move(_pacmanDirection);
            this.checkForCollision();

            _blinkyDirection = _blinky.ghostBFS(_blinkyCoordinate, _blinkyDirection, _pacmanCoordinate);
            _blinky.move(_blinkyDirection);

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
                    currentSquare.getSquareElements().remove(i);
                }

             }
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






