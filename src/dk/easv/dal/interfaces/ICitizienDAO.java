package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.FunkResult;
import dk.easv.be.HealthResult;
import dk.easv.be.User;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ICitizienDAO {
    /**
     *finder alle datoer hvor der er blevet lavet indslag på en borger
     * @param id den borger du vil have alle datoer f ra
     * @return en liste af sql.Date objekter
     */
    Collection<Date> getLogDates(int id);

    /**
     * finder alle borgere
     * @return en liste af Citizens der i databasen er markeret som en borger
     */
    List<Citizen> getAllCitizens();

    /**
     *finder alle skabeloner
     * @return en liste af citizens der i databasen er markeret som template
     */
    List<Citizen> getAllTemplatesOfCitizens();

    /**
     *skaber en borger i databasen
     * @param fName borgerens fornavn
     * @param lName borgerens efternavn
     * @param birthDate borgeren fødselsdag
     */
    void createCitizen(String fName, String lName, Date birthDate);

    /**
     * sætter en borger på en elev
     * @param user den elever der skal have adgang til borgeren
     * @param citizen borgeren der skal gives til en elev
     */
    void addUserToCitizen(User user, int citizen);

    /**
     *Sletter forbindelsen imellem en elev og en borger
     * @param citizenToBeDeleted
     * @param user
     */
    void deleteCitizenFromUser(Citizen citizenToBeDeleted, User user);

    /**
     * finder alle borgere der er tilknyttet en user
     * @param user
     * @return en liste af citizens
     */
    List<Citizen> getAllCitizensFromUser(User user);

    /**
     * Kopiere en borger i databasen
     * @param citizen den borger der skal kopieres
     * @return
     */
    int createCopyCitizen(Citizen citizen);


    void createCopyCase(Citizen citizen, String fName, String lName) throws SQLServerException;

    /**
     * sletter den den borger der er tilknyttet input id'et
     * @param citizenId id'et af den borger der skal slettes fra databasen
     * @throws SQLException
     */
    void deleteCitizen(int citizenId) throws SQLException;

    /**
     * opdatere hvornår en borger sidst blev redigeret
     * @param citizen den borger der skal have sin tid opdateret
     * @throws SQLException
     */
    void updateLastEdited(Citizen citizen) throws SQLException;

    /**
     * Gemmer en ny borger og deres info i databasen
     * @param citizen den borger det får gemt nyt info
     * @param newDate datoen hvorpå der bliver gemt
     * @param funkMap map der indeholder funkResult objekter
     * @param healthMap map der indeholder healthResult objekter
     * @param genInfoMap map der indeholder generel info
     * @throws SQLException
     */
    void saveCitizen(Citizen citizen, java.sql.Date newDate, Map<Integer, FunkResult> funkMap, Map<Integer, HealthResult> healthMap, Map<String, String> genInfoMap) throws SQLException;

    /**
     * finder healthresults fra en dato
     * @param id idet fra den borger du vil have info fra
     * @param date den dato info skal være fra
     * @return et map der indeholder HealthResult objekter ud fra den valgte dato
     * @throws SQLException
     */
    Map<Integer, HealthResult> loadHealthInfoFromDate(int id, Date date) throws SQLException;

    /**
     * finder funkresults fra en dato
     * @param id idet fra den borger du vil have info fra
     * @param date den dato info skal være fra
     * @return et map der indeholder FunkResult objekter ud fra den valgte dato
     * @throws SQLException
     */
    Map<Integer, FunkResult> loadFunkInfoFromDate(int id, Date date);
}
