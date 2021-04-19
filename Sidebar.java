package pacman;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Sidebar {

    private HBox _buttonPane;
    private Label _label1;
    private Label _label2;


    public Sidebar(HBox buttonPane){
        _buttonPane = buttonPane;

        _label1 = new Label("Lives:");
        _label1.setTextFill(Color.rgb(255, 255, 255));
        _label1.setFont(new Font("Arial", 14));
        _label1.setAlignment(Pos.CENTER_LEFT);
        _buttonPane.getChildren().add(_label1);

        _label2 = new Label("Score:" + 0);
        _label2.setTextFill(Color.rgb(255, 255, 255));
        _label2.setFont(new Font("Arial", 14));
        _label2.setAlignment(Pos.CENTER);
        _buttonPane.getChildren().add(_label2);
    }

    public void updateLivesLabel(int lives){
        _label1.setText("Lives:" + lives);
    }

    public void updateScoreLabel(int score){
         _label2.setText("Score:" + score);
    }

}
