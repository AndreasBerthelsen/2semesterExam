package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;

import java.sql.Date;
import java.util.List;

public interface ICitizienDAO {
    List<Citizen> getAllCitizens();
    void createCitizen(String fName, String lName, Date birthDate);
    void addUserToCitizen(User user, Citizen citizen);

    void createTemplate(Citizen citizen) throws SQLServerException;
}
