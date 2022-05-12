package dk.easv.be;


import java.sql.Date;

import java.util.HashMap;


import java.sql.Date;
import java.util.Map;

public class Citizen {

    //gen id i DAO metode??
    private int id;

    //gen info
    private Map<String,String> genInfoText;

    //funktion
    private Map<Integer,Integer> currentCombo;
    private Map<Integer,Integer> targetCombo;
    private Map<Integer,String> funkInfo;

    //Helbred
    private Map<Integer, Integer> relevansMap;
    private Map<Integer, String> helbredInfo;

    private String firstname;
    private String lastname;
    private String description;

    private java.sql.Date bDate;

    public Citizen(int id, String firstname, String lastname, java.sql.Date bDate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bDate = bDate;
    }

    public Citizen(int id, String firstname, String lastname, java.sql.Date bDate, String description) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bDate = bDate;
        this.description = description;
    }

    public Citizen(String fName, String lName, Date date, Map<String, String> genInfoText, Map<Integer, Integer> currentCombo, Map<Integer, Integer> targetCombo, Map<Integer, String> funkInfo, Map<Integer, Integer> relevansMap, Map<Integer, String> helbredInfo) {
        this.genInfoText = genInfoText;
        this.currentCombo = currentCombo;
        this.targetCombo = targetCombo;
        this.funkInfo = funkInfo;
        this.relevansMap = relevansMap;
        this.helbredInfo = helbredInfo;
        this.firstname = fName;
        this.lastname = lName;
        this.bDate = date;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;

    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getbDate() {
        return bDate;
    }



    public Map<String, String> getGenInfoText() {
        return genInfoText;
    }

    public Map<Integer, Integer> getCurrentCombo() {
        return currentCombo;
    }

    public Map<Integer, Integer> getTargetCombo() {
        return targetCombo;
    }

    public Map<Integer, String> getFunkInfo() {
        return funkInfo;
    }

    public Map<Integer, Integer> getRelevansMap() {
        return relevansMap;
    }

    public Map<Integer, String> getHelbredInfo() {
        return helbredInfo;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", bDate=" + bDate + '\'' +
                ", description=" + description +
                '}';
    }

}
