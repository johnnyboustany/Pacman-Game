package pacman;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import tetris.Constants;

public class PaneOrganizer {

    private BorderPane _root;
    private HBox _buttonPane;
    private Sidebar _sidebar;


    public PaneOrganizer(){

        _root = new BorderPane();
        Pane boardPane = new Pane();
        _root.setTop(boardPane);
        boardPane.setStyle("-fx-background-color: black");
        this.setUpButtonPane();

        new Game(boardPane, _sidebar);

    }

    public Pane getRoot(){
        return _root;
    }

    private void setUpButtonPane(){
        _buttonPane = new HBox();
        _root.setBottom(_buttonPane);


        Button button = new Button("Quit");
        button.setAlignment(Pos.CENTER_RIGHT);
        _buttonPane.getChildren().add(button);

        _sidebar = new Sidebar(_buttonPane);


        _buttonPane.setStyle("-fx-background-color: darkblue");
        _buttonPane.setSpacing(200);
        button.setOnAction(new QuitHandler());
    }



    private class QuitHandler implements EventHandler<ActionEvent> {

        /**
         *  The handle method makes the system exit the Tetris program.
         *  The event is passed in when this method is called.
         */
        public void handle(ActionEvent event) {
            System.exit(0);
        }
    }
}
