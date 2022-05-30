package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.Interfaces.IController;
import dk.easv.gui.teacher.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddStudentController extends SuperController implements IController {
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

    private UserModel userModel;
    private User user;

    public AddStudentController() throws SQLServerException {
        userModel = new UserModel();
    }
    /**
     * Denne metode gemmer en elev i system, hvori at vi gemmer dem med alle dens attributter ved anvendelse af metoden createStudent()
     * Vi tjekker også i denne metode hvorvidt at brugernavnet allerede eksisterer i vores database ved checkUserName() metoden
     * @throws SQLException
     */
    public void handleSaveBtn(ActionEvent actionEvent) throws SQLException {
        String firstname = getFirstName(fNameTxtfield);
        String lastname = getLastName(lNameTxtField);
        String username = getUsername(usernameTxtfield);
        String password = getPassword(passwordTxtfield);
        int schoolId = user.getSchoolID();
        if (firstname != null && lastname != null && username != null && password != null && !userModel.checkUsername(username)) {
            userModel.createStudent(firstname, lastname, username, password, schoolId);
            closeWindow(saveBtn);
        }
        else {
            errorMessage("Check venligst om alle felterne er udfyldt eller om der allerede eksisterer en bruger med samme brugernavn i programmet");
        }
    }
    /**
     * Lukker vinduet når der bliver trykket på cancel knappen
     */
    public void handleCancelBtn(ActionEvent actionEvent) {
        closeWindow(cancelBtn);
    }

    /**
     * Sætter userens info, som er logget ind.
     * @param user - useren der er logget ind
     */
    @Override
    public void setUserInfo(User user) {
        this.user = user;
    }
}
