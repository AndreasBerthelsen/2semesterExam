package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.be.UserType;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    void createUser(String firstName, String lastName, String username, String hashedPassword, String salt, UserType userType) throws SQLException;
    List<User> getAllUsers(UserType userType) throws SQLServerException;
}
