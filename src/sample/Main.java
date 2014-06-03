package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public final class Main extends Application {

    static public File file;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("sample.fxml")).load()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}