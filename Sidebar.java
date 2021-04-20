package pacman;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *  The Sidebar class takes in an HBox from
 *  the PaneOrganizer class and adds 2 labels to it
 *  that are updated throughout the game.
 *
 */
public class Sidebar {
    private HBox _sidebarPane;
    private Label _label1;
    private Label _label2;

    /**
     *  In the constructor, the 2 labels are created
     *  and added graphically to the sidebarPane stored in
     *  an instance variable.
     *
     */
    public Sidebar(HBox sidebarPane){
        _sidebarPane = sidebarPane;

        _label1 = new Label("Lives:");
        _label1.setTextFill(Color.rgb(255, 255, 255));
        _label1.setFont(new Font("Arial", 14));
        _label1.setAlignment(Pos.CENTER_LEFT);
        _sidebarPane.getChildren().add(_label1);

        _label2 = new Label("Score:" + 0);
        _label2.setTextFill(Color.rgb(255, 255, 255));
        _label2.setFont(new Font("Arial", 14));
        _label2.setAlignment(Pos.CENTER);
        _sidebarPane.getChildren().add(_label2);
    }

    /**
     * Updates the number of lives pacman has
     * remaining in the game (from the Game class).
     *
     */
    public void updateLivesLabel(int lives){
        if(lives < 0){
        lives = 0;
        }
        _label1.setText("Lives:" + lives);
    }

    /**
     * Updates the score in the game
     * (from the Game class).
     *
     */
    public void updateScoreLabel(int score){
        _label2.setText("Score:" + score);
    }
}
