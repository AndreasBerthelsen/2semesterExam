package dk.easv.bll;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.bll.Util.BCrypt;
import dk.easv.dal.Facade;

import java.sql.SQLException;
import java.util.List;

public class UserManager {
    Facade facade;

    public UserManager() {
        facade = Facade.getInstance();
    }

    public void createUser(String firstName, String lastName, String username, String password, UserType userType, int schoolID) throws SQLException {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        facade.createUser(firstName, lastName,username, hashedPassword, salt, userType, schoolID);
    }

    public List<User> getAllStudents() throws SQLServerException {
        return facade.getAllUsers(UserType.STUDENT);
    }

    public void deleteUser(User user)  {
        facade.deleteUser(user);
    }

    public void udpateUser(User user) {
        facade.updateUser(user);
    }

    public void updateAdminUser(User user) {facade.updateAdminUser(user);}

    public void updatePassword(User user, String hashPassword) throws SQLServerException {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(hashPassword, salt);
        facade.updatePassword(user, hashedPassword, salt);
    }

    public List<User> getAllTeachersFromSchool(School school) {
        return facade.getAllUsersFromSchools(school,UserType.TEACHER);
    }

    public List<User> getAllStudentsFromSchool(School school) {
        return facade.getAllUsersFromSchools(school, UserType.STUDENT);
    }

    public List<School> getAllSchools() throws SQLServerException {
        return facade.getAllSchools();
    }

    public boolean checkUsername(String username){
        return facade.checkUsername(username);
    }

    public List<User> getAllStudentFromIndividuelSchool(int schoolID) {
        return facade.getAllStudentsFromSchool(schoolID);
    }


}
