package dk.easv.gui.teacher.model;


import dk.easv.be.Citizen;

import dk.easv.be.Section;

import dk.easv.bll.CitizenManager;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CitizenModel {
    CitizenManager cM = new CitizenManager();

    public CitizenModel() throws IOException {
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

    public HashMap<Integer, String> getHelbredsTilstande() {
        return cM.getHelbredsTilstande();
    }

    public HashMap<Integer, ArrayList<String>> getHelbredVanskligheder() {
        return cM.getHelbredVanskligheder();
    }


    public ObservableList<Citizen> getAllCitizenObservable() {
        List<Citizen> tempCitizenList;
        ObservableList<Citizen> citizens = FXCollections.observableArrayList();
        tempCitizenList = this.cM.getAllCitizen();
        citizens.addAll(tempCitizenList);
        return citizens;
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
