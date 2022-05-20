package dk.easv.gui.admin.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.gui.admin.Model.AdminModel;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.teacher.Interfaces.IController;
import dk.easv.gui.teacher.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditTeacherOrStudent extends SuperController implements IController, Initializable {
    public TextField usernameTxt;
    @FXML
    private ComboBox<School> schoolCombobox;
    @FXML
    private TextField passwordTxtFIeld;
    @FXML
    private TextField firstnameTxtField;
    @FXML
    private TextField lastnameTxtField;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;

    private School school;

    private UserModel userModel;
    private AdminModel adminModel;

    User user;

    public EditTeacherOrStudent() throws SQLServerException {
        adminModel = new AdminModel();
        userModel = new UserModel();
    }

    public void handleSaveBtn(ActionEvent actionEvent) throws SQLServerException {
        String firstname = getFirstName(firstnameTxtField);
        String lastname = getLastName(lastnameTxtField);
        String username = getUsername(usernameTxt);
        String password = getPasswordUpdate(passwordTxtFIeld);
        Integer schoolID = getSchoolId(schoolCombobox);
        int id = user.getId();
        UserType userType = user.getType();
        User user = new User(id, firstname, lastname, username, userType, schoolID);
        if (firstname != null && lastname != null && username != null && userType != null) {
            userModel.updateAdminUser(user);
            closeWindow(saveBtn);
        }
        else {
            errorMessage("Check venligst om alle felterne er udfyldt, eller om der eksisterer anden en bruger med det samme brugernavn");
        }
        if(password != null){
            userModel.updatePassword(user, password);
        }
    }


    public void handleCancelBtn(ActionEvent actionEvent) {
        closeWindow(cancelBtn);
    }

    @Override
    public void setUserInfo(User user) {
        this.user = user;
        firstnameTxtField.setText(user.getFirstname());
        lastnameTxtField.setText(user.getLastname());
        usernameTxt.setText(user.getUsername());
        setCombobox(user);
    }

    private void setCombobox(User user){
        for (School school : schoolCombobox.getItems()) {
            if (user.getSchoolID() == school.getId()){
                schoolCombobox.getSelectionModel().select(school);
                break;
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ObservableList<School> listOfSchools = adminModel.getObservableSchools();
            schoolCombobox.setItems(listOfSchools);
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
    }
}
