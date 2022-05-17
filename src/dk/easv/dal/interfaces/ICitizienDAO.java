package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ICitizienDAO {
    List<Citizen> getAllCitizens();
    List<Citizen> getAllTemplatesOfCitizens();
    void createCitizen(String fName, String lName, Date birthDate);
    void addUserToCitizen(User user, Citizen citizen);
    void deleteCitizenFromUser(Citizen citizenToBeDeleted, User user);
    
    List<Citizen> getAllCitizensFromUser(User user);

    void createCopyCitizen(Citizen citizen);

    void deleteCitizen(int citizenId) throws SQLException;
    void updateLastEdited(Citizen citizen) throws SQLException;

    void saveCitizen(Citizen citizen);
}
