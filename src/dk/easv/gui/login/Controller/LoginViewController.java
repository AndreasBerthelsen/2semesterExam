package dk.easv.gui.login.Controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.gui.login.model.LoginModel;
import dk.easv.gui.supercontroller.SuperController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    public void handleLoginBtn(ActionEvent actionEvent) throws SQLException, IOException {
        String username = usernameInput.getText();
        String password = passwordInput.getText();

        if (!username.isBlank() && !password.isBlank()) {
            User user = loginModel.loginUser(username, password);
            if (user != null){
                UserType type = user.getType();
                switch (type) {
                    case TEACHER -> openSceneAsUser(user, "/dk/easv/gui/teacher/view/TeacherViewMain.fxml", "TeacherView", actionEvent);
                    case STUDENT -> openSceneAsUser(user, "/dk/easv/gui/elev/View/ElevView.fxml", "StudentView", actionEvent);
                }
            } else {
                errorMessage("Fejl i brugernavn eller adgangskode");
            }
        }
    }
}
