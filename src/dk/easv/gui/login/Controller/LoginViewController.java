package dk.easv.gui.login.Controller;

import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.gui.login.model.LoginModel;
import dk.easv.gui.supercontroller.SuperController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class LoginViewController extends SuperController {
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;

    LoginModel loginModel;

    public LoginViewController() {
        loginModel = new LoginModel();
    }

    /**
     * Denne metode handler login ind til alle de forskellige usertype vi har i vores program.
     * Der bliver anvendt en switch case i denne, som tager hensyn til alle userstype vi har i vores program,
     * således at hver usertype kommer ind i deres respektive vinduer
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void handleLoginBtn(ActionEvent actionEvent) throws SQLException, IOException {
        String username = usernameInput.getText();
        String password = passwordInput.getText();

        if (!username.isBlank() && !password.isBlank()) {
            User user = loginModel.loginUser(username, password);
            if (user != null){
                UserType type = user.getType();
                switch (type) {
                    case TEACHER -> openSceneAsUser(user, "/dk/easv/gui/teacher/view/TeacherViewMain.fxml", "TeacherView", actionEvent);
                    case STUDENT -> openSceneAsUser(user, "/dk/easv/gui/student/view/ElevView.fxml", "StudentView", actionEvent);
                    case ADMIN -> openSceneAsUser(user, "/dk/easv/gui/admin/view/AdminMainView.fxml", "AdminView", actionEvent);
                }
            } else {
                errorMessage("Fejl i brugernavn eller adgangskode");
            }
        }
    }
}
