package dk.easv.gui.teacher.model;

import dk.easv.bll.CitizenManager;

import java.util.ArrayList;

public class SkabelonModel {
    CitizenManager cM = new CitizenManager();
    public ArrayList<String> getGeneralinfoFields(){
        return cM.getGeneralinfoFields();
    }
}
