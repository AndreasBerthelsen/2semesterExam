package dk.easv.be;


import java.sql.Date;

import java.util.HashMap;


import java.sql.Date;

public class Citizen {

    //gen id i DAO metode??
    private int id;

    //gen info
    private HashMap<String,String> genInfoText;

    //funktion
    private HashMap<Integer,Integer> currentCombo;
    private HashMap<Integer,Integer> targetCombo;
    private HashMap<Integer,String> funkInfo;

    //Helbred
    private HashMap<Integer, Integer> relevansMap;
    private HashMap<Integer, String> helbredInfo;

    private String firstname;
    private String lastname;

    private java.sql.Date bDate;

    public Citizen(int id, String firstname, String lastname, java.sql.Date bDate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bDate = bDate;
    }

    public Citizen(String fName, String lName, Date date, HashMap<String, String> genInfoText, HashMap<Integer, Integer> currentCombo, HashMap<Integer, Integer> targetCombo, HashMap<Integer, String> funkInfo, HashMap<Integer, Integer> relevansMap, HashMap<Integer, String> helbredInfo) {
        this.genInfoText = genInfoText;
        this.currentCombo = currentCombo;
        this.targetCombo = targetCombo;
        this.funkInfo = funkInfo;
        this.relevansMap = relevansMap;
        this.helbredInfo = helbredInfo;
        this.fName = fName;
        this.lName = lName;
        this.bDate = date;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
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

    public void setbDate(Date bDate) {
        this.bDate = bDate;
    }


    public HashMap<String, String> getGenInfoText() {
        return genInfoText;
    }

    public HashMap<Integer, Integer> getCurrentCombo() {
        return currentCombo;
    }

    public HashMap<Integer, Integer> getTargetCombo() {
        return targetCombo;
    }

    public HashMap<Integer, String> getFunkInfo() {
        return funkInfo;
    }

    public HashMap<Integer, Integer> getRelevansMap() {
        return relevansMap;
    }

    public HashMap<Integer, String> getHelbredInfo() {
        return helbredInfo;
    }


    @Override
    public String toString() {
        return "Citizen{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", bDate=" + bDate +
                '}';
    }

}
