package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.be.UserType;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    /**
     * skaber en bruger i databasen
     * @param firstName fornavn
     * @param lastName efternavn
     * @param username brugernavn
     * @param hashedPassword
     * @param salt
     * @param userType usertype bestemmer om det skal være en elev / lære / admin
     * @param schoolID idet af den skole som den nye bruger tilhøre
     * @throws SQLException
     */
    void createUser(String firstName, String lastName, String username, String hashedPassword, String salt, UserType userType, int schoolID) throws SQLException;

    /**
     * finder en liste af alle brugere af den givne userType
     * @param userType den userType listen skal users skal have
     * @return liste af users der har den valgte usertype
     * @throws SQLServerException
     */
    List<User> getAllUsers(UserType userType) throws SQLServerException;

    /**
     * sletter en user i databasen
     * @param userToBeDeleted
     */
    void deleteUser(User userToBeDeleted);

    /**
     * opdatere en users info i databasen
     * @param user den user der skal overskrive den
     */
    void updateUser(User user);

    /**
     * opdatere passwordet af en user
     * @param user den user der skal have sit password opdateret
     * @param hashPassword det nye password
     * @param salt
     * @throws SQLServerException
     */
    void updatePassword(User user, String hashPassword, String salt) throws SQLServerException;

    /**
     * finder en liste af users fra den valgte skole af den valgte usertype
     * @param school skolen som listen skal tilhøre
     * @param userType hvilken usertype listen skal bestå af
     * @return list af users
     */
    List<User> getAllUsersFromSchools(School school, UserType userType);

    /**
     * finder alle skoler
     * @return liste af School objekter
     * @throws SQLServerException
     */
    List<School> getAllSchools() throws SQLServerException;

    /**
     * tjekker om det inputtede username findes i databasen
     * @param username string username
     * @return true hvis inputtet findes i databsen, else false
     */
    boolean checkUsername(String username);

    /**
     * opdatere infoen om en user
     * @param user den nye user der skal overskrive den gamle
     */
    void updateAdminUser(User user);

    /**
     * finder alle elever der tilhøre en specifik skole
     * @param schoolID skole idet
     * @return en liste a Users
     */
    List<User> getAllStudentFromSchool(int schoolID);
}
