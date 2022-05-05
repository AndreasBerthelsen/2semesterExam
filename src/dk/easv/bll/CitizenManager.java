package dk.easv.bll;

import dk.easv.be.Section;
import dk.easv.dal.Facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CitizenManager {
    Facade facade;

    public CitizenManager() {
        facade = Facade.getInstance();
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

    public List<Section> getFunkSections() {
        return facade.getFunkSections();
    }

    public List<Section> getHealthSections(){
        return facade.getHealthSections();
    }

    public void saveTemplate() {
        facade.saveTemplate();
    }
}
