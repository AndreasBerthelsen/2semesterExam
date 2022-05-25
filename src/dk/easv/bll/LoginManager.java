package dk.easv.bll;

import dk.easv.be.User;
import dk.easv.dal.Facade;

import java.sql.SQLException;

public class LoginManager {
    Facade facade;

    public LoginManager() {
        facade = Facade.getInstance();
    }

    public User loginUser(String username, String password) throws SQLException {
       return facade.loginUser(username, password);
    }
}
