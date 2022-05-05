package dk.easv.bll;


import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.dal.CitizienDAO;

import dk.easv.be.Section;

import dk.easv.dal.Facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CitizenManager {
    Facade facade;
    private CitizienDAO citizienDAO;

    public CitizenManager() throws IOException {
        facade = Facade.getInstance();
        citizienDAO = new CitizienDAO();
    }

    public ArrayList<String> getGeneralinfoFields(){
        return facade.getGeneralinfoFields();
    }
    public HashMap<Integer,String> getFunktionsTilstande(){
        return facade.getFunktionsTilstande();
    }
    public HashMap<Integer,ArrayList<String>> getFunktionsVandskligheder(){return facade.getFunktionsVandskligheder();}


    public HashMap<Integer, String> getHelbredsTilstande() {
        return facade.getHelbredsTilstande();
    }

    public HashMap<Integer, ArrayList<String>> getHelbredVanskligheder() {
        return facade.getHelbredVanskligheder();
    }


    public List<Citizen> getAllCitizen() {
        return citizienDAO.getAllCitizens();
    }
    public List<Section> getFunkSections(){
        return facade.getFunkSections();
    }

    public List<Section> getHealthSections(){
        return facade.getHealthSections();
    }

    public void saveTemplate(Citizen citizen) throws SQLServerException {
        facade.saveTemplate(citizen);
    }
}
