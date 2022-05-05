package dk.easv.dal.interfaces;

import dk.easv.be.Citizen;

import java.sql.Date;
import java.util.List;

public interface ICitizienDAO {
    List<Citizen> getAllCitizens();
    void createCitizen(String fName, String lName, Date birthDate);
}
