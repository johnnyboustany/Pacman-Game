package pacman;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Sidebar {

    private HBox _buttonPane;
    private Label _label2;


    public Sidebar(HBox buttonPane){

        _buttonPane = buttonPane;

        Label label1 = new Label("Lives:");
        label1.setTextFill(Color.rgb(255, 255, 255));
        label1.setFont(new Font("Arial", 14));
        label1.setAlignment(Pos.CENTER_LEFT);
        _buttonPane.getChildren().add(label1);

        _label2 = new Label("Score:" + 0);
        _label2.setTextFill(Color.rgb(255, 255, 255));
        _label2.setFont(new Font("Arial", 14));
        _label2.setAlignment(Pos.CENTER);
        _buttonPane.getChildren().add(_label2);
    }

    public void updateScoreLabel(int score){
       int currentScore = score;
         _label2.setText("Score:" + currentScore);

    }


}
