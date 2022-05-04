package dk.easv.bll;

import dk.easv.be.UserType;
import dk.easv.dal.BCrypt;
import dk.easv.dal.Facade;

import java.sql.SQLException;

public class TeacherManager {
    Facade facade;

    public TeacherManager() {
        facade = Facade.getInstance();
    }

    public void createUser(String firstName, String lastName, String username, String password, UserType userType) throws SQLException {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        facade.createUser(firstName, lastName,username, hashedPassword, salt, userType);
    }
}
