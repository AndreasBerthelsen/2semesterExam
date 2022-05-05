package dk.easv.gui.teacher.model;

import dk.easv.be.Section;
import dk.easv.bll.CitizenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CitizenModel {
    CitizenManager cM = new CitizenManager();
    public ArrayList<String> getGeneralinfoFields(){
        return cM.getGeneralinfoFields();
    }

    public HashMap<Integer,String> getFunktionsTilstande(){
        return cM.getFunktionsTilstande();
    }

    public HashMap<Integer,ArrayList<String>> getFunktionsVandskligheder() {
        return cM.getFunktionsVandskligheder();
    }

    public HashMap<Integer, String> getHelbredsTilstande() {
        return cM.getHelbredsTilstande();
    }

    public HashMap<Integer, ArrayList<String>> getHelbredVanskligheder() {
        return cM.getHelbredVanskligheder();
    }

    public List<Section> getFunkSections() {
        return cM.getFunkSections();
    }

    public List<Section> getHealthSections(){
        return cM.getHealthSections();
    }

    public void saveTemplate() {
        cM.saveTemplate();
    }
}
