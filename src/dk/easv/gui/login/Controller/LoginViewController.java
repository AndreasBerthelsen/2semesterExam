package dk.easv.gui.login.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginViewController {
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;

    public void handleLoginBtn(ActionEvent actionEvent) {
        String username = usernameInput.getText();
        String password = passwordInput.getText();

        // FIXME: 5/2/2022 
    }
}
