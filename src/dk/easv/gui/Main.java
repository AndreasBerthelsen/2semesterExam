package dk.easv.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/TeacherViewMain.fxml")));
        Scene scene = new Scene(parent);
        stage.setTitle("SOSU FS3");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.setMaximized(true);
        //stage.setFullScreen(true); Giver en F11 fullscreen
        stage.show();
    }
}
