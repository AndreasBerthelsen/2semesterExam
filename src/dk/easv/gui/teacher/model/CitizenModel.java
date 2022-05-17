package dk.easv.gui.teacher.model;


import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.*;
import dk.easv.bll.CitizenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

    public HashMap<Integer, String> getFunktionsTilstande() {
        return cM.getFunktionsTilstande();
    }

    public HashMap<Integer, ArrayList<String>> getFunktionsVandskligheder() {
        return cM.getFunktionsVandskligheder();
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

    public void addUserToCitizen(Citizen citizen, User user) {
        cM.addUserToCitizen(citizen, user);
    }

    public List<Section> getFunkSections() {
        return cM.getFunkSections();
    }

    public List<Section> getHealthSections() {
        return cM.getHealthSections();
    }

    public void saveTemplate(String fName, String lName, Date date, String description, Map<String, TextArea> genInfoMap, Map<Integer, FunkNodeContainer> funkAnswerMap, Map<Integer, HealthNodeContainer> healthAnswerMap, Date obsDate) throws SQLServerException {
        cM.saveTemplate(fName, lName, date,description,genInfoMap,funkAnswerMap,healthAnswerMap,obsDate);
    }


    public void deleteTemplate(int citizenId) throws SQLException {
        cM.deleteTemplate(citizenId);
    }

    public ObservableList<Citizen> getObservableTemplates() throws SQLException {
        return FXCollections.observableArrayList(cM.getAllTemplates());
    }

    public void createCopyCitizen(Citizen citizen) {
        cM.createCopyCitizen(citizen);
    }

    public void deleteCitizenFromUser(Citizen citizenToBeDeleted, User user) {
        cM.deleteCitizenFromUser(citizenToBeDeleted, user);
    }

    public Citizen loadTemplate(Citizen citizen) {
        return cM.loadTemplate(citizen);

    }

    public void updateTemplate(Citizen citizen, int id) throws SQLServerException {
        cM.updateTemplate(citizen, id);
    }

    public void deleteCitizen(int citizenId) throws SQLException {
        cM.deleteCitizen(citizenId);
    }

    public Citizen loadCitizen(Citizen citizen) {
        return cM.loadCitizen(citizen);
    }
}
