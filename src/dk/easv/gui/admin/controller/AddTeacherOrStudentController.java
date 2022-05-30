package dk.easv.gui.admin.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.UserType;
import dk.easv.gui.admin.Model.AdminModel;
import dk.easv.gui.supercontroller.SuperController;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddTeacherOrStudentController extends SuperController implements Initializable {
    @FXML
    private ComboBox<School> schoolCombobox;
    @FXML
    private ComboBox<UserType> usertypeCombobox;
    @FXML
    private TextField passwordTxtFIeld;
    @FXML
    private TextField firstnameTxtField;
    @FXML
    private TextField lastnameTxtField;
    @FXML
    private TextField usernameTxtField;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;

    private AdminModel aM;
    private UserModel uM;

    public AddTeacherOrStudentController() throws SQLServerException {
        aM = new AdminModel();
        uM = new UserModel();
    }

    /**
     * Denne metode gemmer enten en lærer eller elev i system, hvori at vi gemmer dem med alle dens attributter
     * ved anvendelse af metoden createUser()
     * Vi tjekker også i denne metode hvorvidt at brugernavnet allerede eksisterer i vores database ved checkUserName() metoden
     * @throws SQLException
     */
    public void handleSaveBtn(ActionEvent actionEvent) throws SQLException {
        String firstname = getFirstName(firstnameTxtField);
        String lastname = getLastName(lastnameTxtField);
        String username = getUsername(usernameTxtField);
        String password = getPassword(passwordTxtFIeld);
        Integer schoolID = getSchoolId(schoolCombobox);
        UserType userType = getUsertype(usertypeCombobox);
        if (firstname != null && lastname != null && username != null && password != null &&  userType != null && schoolID != null && !aM.checkUsername(username)) {
            uM.createUser(firstname, lastname, username, password, schoolID, userType);
            closeWindow(saveBtn);
        }
        else {
            errorMessage("Udfyld venligst alle feltenerne ellers check venligst om brugernavnet er anvendt");
        }
    }

    /**
     * Lukker vinduet når der bliver trykket på cancel knappen
     */
    public void handleCancelBtn(ActionEvent actionEvent) {
        closeWindow(cancelBtn);
    }

    /**
     * Initaliserer vores comboboxe med deres respektive observaable lister
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usertypeCombobox.setItems(FXCollections.observableArrayList(UserType.STUDENT, UserType.TEACHER));
        try {
            ObservableList<School> listOfSchols = aM.getObservableSchools();
            schoolCombobox.setItems(listOfSchols);
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
    }
}
