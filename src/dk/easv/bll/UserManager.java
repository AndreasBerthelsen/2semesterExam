package dk.easv.bll;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.dal.BCrypt;
import dk.easv.dal.Facade;

import java.sql.SQLException;
import java.util.List;

public class UserManager {
    Facade facade;

    public UserManager() {
        facade = Facade.getInstance();
    }

    public void createUser(String firstName, String lastName, String username, String password, UserType userType) throws SQLException {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        facade.createUser(firstName, lastName,username, hashedPassword, salt, userType);
    }

    public List<User> getAllStudents() throws SQLServerException {
        return facade.getAllUsers(UserType.STUDENT);
    }
    public List<User> getAllTeachers() throws SQLServerException {
        return facade.getAllUsers(UserType.TEACHER);
    }

    public void deleteUser(User user)  {
        facade.deleteUser(user);
    }

    public void udpateUser(User user) {
        facade.updateUser(user);
    }

    public void updatePassword(User user, String hashPassword) throws SQLServerException {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(hashPassword, salt);
        facade.updatePassword(user, hashedPassword, salt);
    }

}
