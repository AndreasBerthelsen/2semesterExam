package dk.easv.bll;


import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.dal.CitizienDAO;

import dk.easv.be.Section;

import dk.easv.dal.Facade;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CitizenManager {
    Facade facade;


    public CitizenManager() throws IOException {
        facade = Facade.getInstance();
    }

    public ArrayList<String> getGeneralinfoFields() {
        return facade.getGeneralinfoFields();
    }

    public HashMap<Integer, String> getFunktionsTilstande() {
        return facade.getFunktionsTilstande();
    }

    public HashMap<Integer, ArrayList<String>> getFunktionsVandskligheder() {
        return facade.getFunktionsVandskligheder();
    }


    public HashMap<Integer, String> getHelbredsTilstande() {
        return facade.getHelbredsTilstande();
    }

    public HashMap<Integer, ArrayList<String>> getHelbredVanskligheder() {
        return facade.getHelbredVanskligheder();
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

    //TODO måske flyt til template manager
    public void saveTemplate(Citizen citizen) throws SQLServerException {
        facade.saveTemplate(citizen);
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


    public List<Citizen> getAllTemplatesOfCitizens() {
        return facade.getAllTemplatesOfCitizens();
    }

    public void deleteCitizenFromUser(Citizen citizenToBeDeleted, User user) {
        facade.deleteCitizenFromUser(citizenToBeDeleted, user);
    }

    public Citizen loadTemplate(Citizen citizen) {
        return facade.loadTemplate(citizen);

    }

    public void updateTemplate(Citizen citizen, int id) throws SQLServerException {
        facade.updateTemplate(citizen, id);
    }

    public void deleteCitizen(int citizenId) throws SQLException {
        facade.deleteCitizen(citizenId);
    }

    public Citizen loadCitizen(Citizen citizen) {
        return facade.loadCitizen(citizen);
    }

    public void updateLastEdited(Citizen citizen) throws SQLException {
        facade.updateLastEdited(citizen);
    }
}

