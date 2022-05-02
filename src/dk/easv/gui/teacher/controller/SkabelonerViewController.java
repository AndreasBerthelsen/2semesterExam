package dk.easv.gui.teacher.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SkabelonerViewController {
    public void handleNySkabelonbtn(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/view/NySkabelonMain.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Ny Skabelon");
        stage.setResizable(true);
        stage.setScene(scene);
        //stage.setFullScreen(true); Giver en F11 fullscreen
        stage.showAndWait();
    }
}
