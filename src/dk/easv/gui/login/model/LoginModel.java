package dk.easv.gui.login.model;

import dk.easv.be.User;
import dk.easv.bll.LoginManager;

import java.sql.SQLException;

public class LoginModel {
    private LoginManager lM;

    public LoginModel() {
        lM = new LoginManager();
    }

    public User loginUser(String username, String password) throws SQLException {
       return lM.loginUser(username,password);
    }

    public void createUser(String username, String password) throws SQLException {
        lM.createUser(username, password);
    }
}
