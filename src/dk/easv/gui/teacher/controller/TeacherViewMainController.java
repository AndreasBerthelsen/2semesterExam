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
        setBorderpaneContent("/dk/easv/gui/teacher/view/TeacherSkabelonerView.fxml");
    }

    public void handleBorgererbtn(ActionEvent actionEvent) throws IOException {
        setBorderpaneContent("/dk/easv/gui/teacher/view/CitizenTeacherView.fxml");
    }

    public void handleEleverbtn(ActionEvent actionEvent) throws IOException {
        setBorderpaneContent("/dk/easv/gui/teacher/view/AdminstrateStudents.fxml");
    }

    private void setBorderpaneContent(String fxml) throws IOException {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        borderpane.setCenter(pane);
    }

    @Override
    public void setUserInfo(User user) {
        this.teacher = user;
    }
}
