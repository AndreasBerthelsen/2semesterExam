package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.be.UserType;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    void createUser(String firstName, String lastName, String username, String hashedPassword, String salt, UserType userType, int schoolID) throws SQLException;
    List<User> getAllUsers(UserType userType) throws SQLServerException;
    void deleteUser(User userToBeDeleted);
    void updateUser(User user);

    void updatePassword(User user, String hashPassword, String salt) throws SQLServerException;

    List<User> getAllUsersFromSchools(School school, UserType userType);

    List<School> getAllSchools() throws SQLServerException;

    boolean checkUsername(String username);

    void updateAdminUser(User user);
    List<User> getAllStudentFromSchool(int schoolID);
}
