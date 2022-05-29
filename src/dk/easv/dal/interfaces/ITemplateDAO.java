package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.FunkResult;
import dk.easv.be.HealthResult;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ITemplateDAO {
    /**
     * sletter den valgte borger template
     * @param citizenId idet til den template der skal slettes
     * @throws SQLException
     */
    void deleteTemplate(int citizenId) throws SQLException;

    /**
     * finder en liste af alle borger templates
     * @return en liste af citizen objekter
     * @throws SQLException
     */
    List<Citizen> getAllTemplates() throws SQLException;

    /**
     * gemmer en borger template i databasen
     * @param fName fornavn
     * @param lName efternavn
     * @param birthDate fødselsdato
     * @param description beskrivelse af formålet med skabelonen
     * @param saveGeninfo map af generel info
     * @param saveFunk map af funkResults
     * @param saveHealth map af HealthResults
     * @param obsDate datoen hvor denne template er blevet gemt
     * @throws SQLServerException
     */
    void createTemplate(String fName, String lName, Date birthDate, String description, Map<String, String> saveGeninfo, Map<Integer, FunkResult> saveFunk, Map<Integer, HealthResult> saveHealth, Date obsDate) throws SQLServerException;

    /**
     * finder helbredstilstands info om den valgte template
     * @param citizenId
     * @return map af HealthResults
     */
    Map<Integer,HealthResult> loadHealthInfo(int citizenId);

    /**
     * finder funktionsevnetilstands info fra template
     * @param id idet af den valgte template
     * @return map af funkResults
     */
    Map<Integer, FunkResult> loadFunkInfo(int id);

    /**
     * finder alt generel info om en template
     * @param id idet af den template info skal tilhøre
     * @param fieldList liste af de kolonner der skal vælges i database
     * @return map af generel info
     */
    Map<String, String> loadGenInfo(int id, List<String> fieldList);

    /**
     * opdatere en template med ny info
     * @param updatedCitizen den nye borger der vil erstatte den gamle
     * @param genResultMap map af generel info
     * @param funkResultMap map af funkResults
     * @param healthResultMap map af HealthResults
     * @param obsDate datoen hvor ændringen er blevet lavet
     */
    void updateTemplate(Citizen updatedCitizen, Map<String, String> genResultMap, Map<Integer, FunkResult> funkResultMap, Map<Integer, HealthResult> healthResultMap, Date obsDate);
}
