package pacman;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *  The PaneOrganizer class creates the root pane, BorderPane. Then, it
 *  creates the child nodes: boardPane (which will be used in the Game class as a
 *  root for other nodes), HBox and a Quit Button (which is added to the HBox).
 *  A new instance of the Game class is passed in a boardPane. The PaneOrganizer
 *  class has a method that sets up the button, which is used to display the Quit button,
 *  allowing the user to exit from the game.
 *
 */
public class PaneOrganizer {
    private BorderPane _root;
    private HBox  _sidebarPane;
    private Sidebar _sidebar;

    /**
     *  In the constructor, boardPane is instantiated from the vanilla Pane class,
     *  and is set at the top of the BorderPane layout. An HBox is set
     *  at the bottom via the setUpSidebar and a button is set up via that same method.
     *  The top-level logic class, Game, is then instantiated
     *  and is passed boardPane and sidebar.
     *
     */
    public PaneOrganizer(){
        _root = new BorderPane();
        Pane boardPane = new Pane();
        _root.setTop(boardPane);
        boardPane.setStyle("-fx-background-color: black");
        this.setUpSidebar();
        new Game(boardPane, _sidebar);
    }

    /**
     *  Sets up the HBox _sidebarPane and adds a quit button to it
     *  and then passes it into the SideBar class which adds
     *  two labels to it.
     *
     */
    private void setUpSidebar(){
        _sidebarPane = new HBox();
        _root.setBottom(_sidebarPane);

        Button button = new Button("Quit");
        button.setAlignment(Pos.CENTER_RIGHT);
        _sidebarPane.getChildren().add(button);
        button.setOnAction(new QuitHandler());
        _sidebarPane.setStyle("-fx-background-color: darkblue");
        _sidebarPane.setSpacing(Constants.LABEL_SPACING);

        _sidebar = new Sidebar(_sidebarPane);
    }

    /**
     *  This private class implements the EventHandler interface and
     *  specifies that it knows how to handle events of type ActionEvent.
     *  Every button click causes JavaFX to make an ActionEvent, exiting
     *  the user from the program.
     */
    private class QuitHandler implements EventHandler<ActionEvent> {

        /**
         *  The handle method makes the system exit the Pacman program.
         *  The event is passed in when this method is called.
         */
        public void handle(ActionEvent event) {
            System.exit(0);
        }
    }

    /**
     * Helper method used in the App class to retrieve the root Pane
     * stored in the _root instance variable.
     */
    public Pane getRoot(){
        return _root;
    }
}
