package dk.easv.gui.teacher.model;


import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.*;
import dk.easv.bll.CitizenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CitizenModel {
    CitizenManager cM = new CitizenManager();
    private ObservableList<Citizen> citizenList;
    private ObservableList<Citizen> templateList;

    public CitizenModel() throws IOException {
        citizenList = FXCollections.observableArrayList();
        citizenList.addAll(getAllCitizens());

        templateList = FXCollections.observableArrayList();
        templateList.addAll(getAllTemplatesOfCitizens());
    }

    private List<Citizen> getAllCitizens() {
        return cM.getAllCitizen();
    }

    public List<Citizen> getAllTemplatesOfCitizens() {
        return cM.getAllTemplatesOfCitizens();
    }

    public ArrayList<String> getGeneralinfoFields() {
        return cM.getGeneralinfoFields();
    }


    public ObservableList<Citizen> getAllCitizenObservable() {
        citizenList.setAll(getAllCitizens());
        return citizenList;
    }

    public ObservableList<Citizen> getAllTemplatesOfCitizensObservable() {
        templateList.setAll(getAllTemplatesOfCitizens());
        return templateList;
    }

    public ObservableList<Citizen> getAllCitizenFromUserObservable(User user) {
        List<Citizen> tempCitizenList;
        ObservableList<Citizen> citizens = FXCollections.observableArrayList();
        tempCitizenList = this.cM.getAllCitizenFromUser(user);
        citizens.addAll(tempCitizenList);
        return citizens;

    }

    public void addCitizenToUser(int citizen, User user) {
        cM.addCitizenToUser(citizen, user);
    }

    public List<Section> getFunkSections() {
        return cM.getFunkSections();
    }

    public List<Section> getHealthSections() {
        return cM.getHealthSections();
    }

    public void deleteTemplate(int citizenId) throws SQLException {
        cM.deleteTemplate(citizenId);
    }

    public ObservableList<Citizen> getObservableTemplates() throws SQLException {
        return FXCollections.observableArrayList(cM.getAllTemplates());
    }

    public int createCopyCitizen(Citizen citizen) {
        return cM.createCopyCitizen(citizen);
    }

    public void createCopyCase(Citizen citizen, String fName, String lName) throws SQLServerException {
        cM.createCopyCase(citizen, fName, lName);
    }

    public void deleteCitizenFromUser(Citizen citizenToBeDeleted, User user) {
        cM.deleteCitizenFromUser(citizenToBeDeleted, user);
    }


    public void deleteCitizen(int citizenId) throws SQLException {
        cM.deleteCitizen(citizenId);
    }


    public void saveTemplate(String fName, String lName, Date birthDate, String description, Map<String, String> saveGeninfo, Map<Integer, FunkResult> saveFunk, Map<Integer, HealthResult> saveHealth, Date obsDate) throws SQLServerException {
        cM.saveTemplate(fName, lName, birthDate, description, saveGeninfo, saveFunk, saveHealth, obsDate);
    }

    public Map<Integer, HealthResult> loadHealthInfo(int id) {
        return cM.loadHealthInfo(id);
    }

    public Map<Integer, FunkResult> loadFunkInfo(int id) {
        return cM.loadFunkInfo(id);
    }

    public Map<String, String> loadGenInfo(int id, List<String> fieldList) {
        return cM.loadGenInfo(id, fieldList);
    }

    public void updateTemplate(Citizen updatedCitizen, Map<String, String> genResultMap, Map<Integer, FunkResult> funkResultMap, Map<Integer, HealthResult> healthResultMap, Date obsDate) {
        cM.updateTemplate(updatedCitizen, genResultMap, funkResultMap, healthResultMap, obsDate);
    }

    public ObservableList<Date> getObservableLogDates(int id) {
        return FXCollections.observableArrayList(cM.getLogDates(id));
    }

    public Map<Integer, HealthResult> loadHealthInfoFromDate(int id, Date date) throws SQLException {
        return cM.loadHealthInfoFromDate(id,date);
    }

    public Map<Integer, FunkResult> loadFunkInfoFromDate(int id, Date date) {
        return cM.loadFunkInfoFromDate(id,date);
    }

    public void saveCitizen(Citizen citizen, Date date, Map<Integer, FunkResult> funkResultMap, Map<Integer, HealthResult> healthResultMap, Map<String, String> genInfoMap) throws SQLException {
        cM.saveCitizen(citizen,date,funkResultMap,healthResultMap,genInfoMap);
    }
}
