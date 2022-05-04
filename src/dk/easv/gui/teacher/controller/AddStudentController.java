package dk.easv.gui.teacher.controller;

import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.teacher.model.TeacherModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddStudentController extends SuperController {
    @FXML
    private TextField fNameTxtfield;
    @FXML
    private TextField lNameTxtField;
    @FXML
    private TextField usernameTxtfield;
    @FXML
    private TextField passwordTxtfield;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;

    private TeacherModel teacherModel;

    public AddStudentController() {
        teacherModel = new TeacherModel();
    }

    public void handleSaveBtn(ActionEvent actionEvent) throws SQLException {
        String firstname = getFirstName(fNameTxtfield);
        String lastname = getLastName(lNameTxtField);
        String username = getUsername(usernameTxtfield);
        String password = getPassword(passwordTxtfield);
        if (firstname != null && lastname != null && username != null && password != null) {
            teacherModel.createStudent(firstname, lastname, username, password);
            closeWindow(saveBtn);
        }
    }

    public void handleCancelBtn(ActionEvent actionEvent) {
    }
}
