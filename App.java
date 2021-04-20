package pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The App class instantiates the PaneOrganizer, which in turn allows the children nodes
 * to be instantiated. It passes in the root pane from the instance of the PaneOrganizer into
 * the constructor of the scene. This allows the stage to set the scene, which will be used to
 * display the Pacman game.
 *
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Instantiating the PaneOrganizer, setting up the scene, and showing the stage.
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot(), Constants.PANE_WIDTH, Constants.PANE_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Pacman!");
        stage.show();
    }

    /*
    * Here is the mainline! No need to change this.
    */
    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
