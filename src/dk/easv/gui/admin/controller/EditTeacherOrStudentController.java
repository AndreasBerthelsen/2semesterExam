package dk.easv.gui.admin.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.gui.admin.Model.AdminModel;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.Interfaces.IController;
import dk.easv.gui.teacher.model.UserModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditTeacherOrStudentController extends SuperController implements IController, Initializable {
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

    public EditTeacherOrStudentController() throws SQLServerException {
        adminModel = new AdminModel();
        userModel = new UserModel();
    }

    /**
     * Denne metode gemmer enten en lærer eller elev i system, hvori at vi gemmer dem med alle dens redigerede attributter
     * ved anvendelse af metoden updateAdminUser()
     * @param actionEvent
     * @throws SQLServerException
     */
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

    /**
     * Denne metode gør således, at den lukkre vinduet, når cancel knappen bliver trykket
     * @param actionEvent
     */
    public void handleCancelBtn(ActionEvent actionEvent) {
        closeWindow(cancelBtn);
    }

    /**
     * Denne metode sætter de forskellige info som useren har med fra AdminMainWindow controlleren
     * @param user
     */
    @Override
    public void setUserInfo(User user) {
        this.user = user;
        firstnameTxtField.setText(user.getFirstname());
        lastnameTxtField.setText(user.getLastname());
        usernameTxt.setText(user.getUsername());
        setCombobox(user);
    }

    /**
     * Denne metode kører igennem et forloop af alle objekterne i skolecomboboxen, og sætter brugerens id til at være den skole
     * som de er på
     * @param user
     */
    private void setCombobox(User user){
        for (School school : schoolCombobox.getItems()) {
            if (user.getSchoolID() == school.getId()){
                schoolCombobox.getSelectionModel().select(school);
                break;
            }
        }
    }

    /**
     * Initaliserer vores skole combobox, og sætter herefter items.
     * @param location
     * @param resources
     */
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
