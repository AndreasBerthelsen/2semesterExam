package dk.easv.gui.teacher.controller;

import dk.easv.be.User;
import dk.easv.gui.teacher.Interfaces.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;

public class TeacherViewMainController implements IController {
    public BorderPane borderpane;
    private User teacher;

    public void handleSkabelonerbtn(ActionEvent actionEvent) throws IOException {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/view/NySkabelonMain.fxml")));
        borderpane.setCenter(pane);
    }

    public void handleBorgererbtn(ActionEvent actionEvent) throws IOException {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/view/TeacherSkabelonerView.fxml")));
        borderpane.setCenter(pane);
    }

    public void handleEleverbtn(ActionEvent actionEvent) throws IOException {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/view/AdminstrateStudents.fxml")));
        borderpane.setCenter(pane);
    }

    @Override
    public void setUserInfo(User user) {
        this.teacher = user;
    }
}
