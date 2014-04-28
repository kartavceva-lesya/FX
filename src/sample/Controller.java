package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class Controller {
    public Button button12;


    public void change(ActionEvent actionEvent) {
        button12.setText("Already pressed!!");
    }
}
