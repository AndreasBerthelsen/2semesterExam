package dk.easv.bll;


import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.*;
import dk.easv.dal.Facade;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CitizenManager {
    Facade facade;


    public CitizenManager() throws IOException {
        facade = Facade.getInstance();
    }

    public ArrayList<String> getGeneralinfoFields() {
        return facade.getGeneralinfoFields();
    }

    public List<Citizen> getAllCitizen() {
        return facade.getAllCitiziens();
    }

    public List<Citizen> getAllCitizenFromUser(User user) {
        return facade.getAllCitiziensFromUser(user);
    }

    public void addUserToCitizen(Citizen citizen, User user) {
        facade.addUserToCitizen(citizen, user);
    }

    public List<Section> getFunkSections() {
        return facade.getFunkSections();
    }

    public List<Section> getHealthSections() {
        return facade.getHealthSections();
    }


    public void deleteTemplate(int citizenid) throws SQLException {
        facade.deleteTemplate(citizenid);
    }

    public List<Citizen> getAllTemplates() throws SQLException {
        return facade.getAllTemplates();
    }

    public void createCopyCitizen(Citizen citizen) {
        facade.createCopyCitizen(citizen);
    }

    public void createCopyCase(Citizen citizen, String fName, String lName) throws SQLServerException {
        facade.createCopyCase(citizen, fName, lName);
    }

    public List<Citizen> getAllTemplatesOfCitizens() {
        return facade.getAllTemplatesOfCitizens();
    }

    public void deleteCitizenFromUser(Citizen citizenToBeDeleted, User user) {
        facade.deleteCitizenFromUser(citizenToBeDeleted, user);
    }

    public void deleteCitizen(int citizenId) throws SQLException {
        facade.deleteCitizen(citizenId);
    }

    public void updateLastEdited(Citizen citizen) throws SQLException {
        facade.updateLastEdited(citizen);
    }

    public void saveTemplate(String fName, String lName, Date birthDate, String description, Map<String, String> saveGeninfo, Map<Integer, FunkResult> saveFunk, Map<Integer, HealthResult> saveHealth, Date obsDate) throws SQLServerException {
        facade.saveTemplate(fName, lName, birthDate, description, saveGeninfo, saveFunk, saveHealth, obsDate);

    }

    public Map<Integer, HealthResult> loadHealthInfo(int id) {
        return facade.loadHealthInfo(id);
    }

    public Map<Integer, FunkResult> loadFunkInfo(int id) {
        return facade.loadFunkInfo(id);
    }

    public Map<String, String> loadGenInfo(int id, List<String> fieldList) {
        return facade.loadGenInfo(id, fieldList);
    }

    public void updateTemplate(Citizen updatedCitizen, Map<String, String> genResultMap, Map<Integer, FunkResult> funkResultMap, Map<Integer, HealthResult> healthResultMap, Date obsDate) {
        facade.updateTemplate(updatedCitizen, genResultMap, funkResultMap, healthResultMap, obsDate);
    }

    public List<Date> getLogDates(int id) {
        return (List<Date>) facade.getLogDates(id);

    }

    public Map<Integer, HealthResult> loadHealthInfoFromDate(int id, Date date) throws SQLException {
        return facade.loadHealthInfoFromDate(id, date);
    }

    public Map<Integer, FunkResult> loadFunkInfoFromDate(int id, Date date) {
        return facade.loadFunkInfoFromDate(id,date);
    }

    public void saveCitizen(Citizen citizen, Date date, Map<Integer, FunkResult> funkResultMap, Map<Integer, HealthResult> healthResultMap, Map<String, String> genInfoMap) throws SQLException {
        facade.saveCitizen(citizen,date,funkResultMap,healthResultMap,genInfoMap);
    }
}

