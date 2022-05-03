package dk.easv.gui.login.Controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.gui.login.model.LoginModel;
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

public class LoginViewController {
    public TextField fakeuser;
    public TextField fakePW;
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
                    case TEACHER -> openNewScene(user, "/dk/easv/gui/teacher/view/TeacherViewMain.fxml", "TeacherView", actionEvent);
                    case STUDENT -> openNewScene(user, "/dk/easv/gui/teacher/view/TeacherSkabelonerView.fxml", "StudentView", actionEvent);
                }
            } else {
                errorMessage("Fejl i brugernavn eller adgangskode");
            }
        }
    }

    public void setUserInfo(User user) {
    }

    private void openNewScene(User user,String fxmlPath, String Title, ActionEvent actionEvent) throws IOException, SQLServerException {
        FXMLLoader root = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root.load());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);

        setUserInfo(user);

        stage.setTitle(Title);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void errorMessage(String errorTxt) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(errorTxt);
        alert.showAndWait();
    }

    public void fakeBtn(ActionEvent actionEvent) throws SQLException {
        String username = fakeuser.getText();
        String password = fakePW.getText();

        loginModel.createUser(username, password);
    }
}
