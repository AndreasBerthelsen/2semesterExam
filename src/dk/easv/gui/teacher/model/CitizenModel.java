package dk.easv.gui.teacher.model;


import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;


import dk.easv.be.Section;

import dk.easv.bll.CitizenManager;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CitizenModel {
    CitizenManager cM = new CitizenManager();
    private ObservableList<Citizen> citizenList;

    public CitizenModel() throws IOException {
        citizenList = FXCollections.observableArrayList();
        citizenList.addAll(getAllCitizens());
    }

    private List<Citizen> getAllCitizens() {
        return cM.getAllCitizen();
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

    public ObservableList<Citizen> getAllCitizenFromUserObservable(User user){
        List<Citizen> tempCitizenList;
        ObservableList<Citizen> citizens = FXCollections.observableArrayList();
        tempCitizenList = this.cM.getAllCitizenFromUser(user);
        citizens.addAll(tempCitizenList);
        return citizens;

    }

    public void addUserToCitizen(Citizen citizen, User user){
        cM.addUserToCitizen(citizen, user);
    }
    public List<Section> getFunkSections() {
        return cM.getFunkSections();
    }

    public List<Section> getHealthSections(){
        return cM.getHealthSections();
    }

    public void saveTemplate(Citizen citizen) throws SQLServerException {
        cM.saveTemplate(citizen);
    }
    public void deleteTemplate(Citizen citizen) throws SQLException {
        cM.deleteTemplate(citizen);
    }

    public ObservableList<Citizen> getObservableTemplates() throws SQLException {
        return FXCollections.observableArrayList(cM.getAllTemplates());
    }
}
