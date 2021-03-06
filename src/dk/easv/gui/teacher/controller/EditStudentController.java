package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.Interfaces.IController;
import dk.easv.gui.teacher.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    /**
     * Sætter userens informationer
     * @param user
     */
    @Override
    public void setUserInfo(User user) {
        this.user = user;
        firstnameTxtField.setText(user.getFirstname());
        lastnameTxtField.setText(user.getLastname());
        usernameTxtField.setText(user.getUsername());
    }

    /**
     * Denne metode gemmer enten en lev i system, hvori at vi gemmer dem med alle dens redigerede attributter
     * ved anvendelse metoden updateUser()
     * @param actionEvent
     * @throws SQLServerException
     */
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
        else {
            errorMessage("Check venligst om alle felterne er udfyldt eller om der eksisterer en bruger med samme brugernavn");
        }
        if(password != null){
            userModel.updatePassword(user, password);
        }
    }

    /**
     * Denne metode lukker vinduet
     * @param actionEvent
     */
    public void handleCancelBtn(ActionEvent actionEvent) {
        closeWindow(cancelBtn);
    }
}