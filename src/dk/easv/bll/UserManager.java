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

    /**
     * Denne metode opretter en user, hvori at alle dens attributer er med.
     * I denne metode krypterer vi også vores password ved anvendelse af BCrypt klassen, hvor vi anvender
     * genstalt() metoden til at genere et salt og derefter hasher password ved anvendelse af hashpw() metoden
     * @param firstName - Foravnet for useren
     * @param lastName - Efternavnet for useren
     * @param username - Userens brugernavn
     * @param password - Userens password
     * @param userType - Userens brugertype
     * @param schoolID - ID'et for den skole useren er tilknyttet til
     * @throws SQLException
     */
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

    /**
     * Heri har vi en metode, der kan opdatere et password. Som i vores createUser() metode
     * genererer vi et salt ved anvendelse af metoden gensalt(), og herefter hasher vi passwordet
     * ved anvendelse af hashPW() metoden.
     * @param user
     * @param hashPassword
     * @throws SQLServerException
     */
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
