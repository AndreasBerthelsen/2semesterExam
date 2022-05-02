package dk.easv.gui.teacher.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;

public class TeacherViewMainController {
    public BorderPane borderpane;

    public void handleSkabelonerbtn(ActionEvent actionEvent) throws IOException {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/skabelonerView.fxml")));
        borderpane.setCenter(pane);
    }

}
