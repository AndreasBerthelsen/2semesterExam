package dk.easv.gui.teacher.model;

import dk.easv.bll.CitizenManager;

import java.util.ArrayList;
import java.util.HashMap;

public class SkabelonModel {
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
}
