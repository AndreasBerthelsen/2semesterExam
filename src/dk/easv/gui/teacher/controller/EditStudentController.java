package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.teacher.Interfaces.IController;
import dk.easv.gui.teacher.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditStudentController extends SuperController implements IController {
    @FXML
    private TextField passwordTxtFIeld;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField firstnameTxtField;
    @FXML
    private TextField lastnameTxtField;
    @FXML
    private TextField usernameTxtField;

    private School school;

    private UserModel userModel;

    User user;


    public EditStudentController() throws SQLServerException {
        userModel = new UserModel();
    }

    @Override
    public void setUserInfo(User user) {
        this.user = (User) user;
        firstnameTxtField.setText(user.getFirstname());
        lastnameTxtField.setText(user.getLastname());
        usernameTxtField.setText(user.getUsername());
    }

    public void handleSaveBtn(ActionEvent actionEvent) throws SQLServerException {
        String firstname = getFirstName(firstnameTxtField);
        String lastname = getLastName(lastnameTxtField);
        String username = getUsername(usernameTxtField);
        String password = getPasswordUpdate(passwordTxtFIeld);
        int id = user.getId();
        int schoolID = user.getSchoolID();
        User user = new User(id, firstname, lastname, username, UserType.STUDENT, schoolID);
        if (firstname != null && lastname != null && username != null && !userModel.checkUsername(username)) {
            userModel.updateUser(user);
            closeWindow(saveBtn);
        }
        if(password != null){
            userModel.updatePassword(user, password);
        }
        else {
            errorMessage("Check venligst om alle felterne er udfyldt eller om der eksisterer en bruger med samme brugernavn i databasen");
        }
    }

    public void handleCancelBtn(ActionEvent actionEvent) {
        closeWindow(cancelBtn);
    }


    public void handleUpdatePassword(ActionEvent actionEvent) {
    }
}