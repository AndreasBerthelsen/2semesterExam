package dk.easv.bll;

import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.dal.BCrypt;
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

    public void createUser(String firstName, String lastName, String username, String password, UserType userType) throws SQLException {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        facade.createUser(firstName, lastName,username, hashedPassword, salt, userType);
    }
}
