package dk.easv.be;

import java.sql.Date;
import java.util.HashMap;

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

    private String fName;
    private String lName;
    private java.sql.Date bDate;

    public Citizen(int id, String fName, String lName, java.sql.Date bDate) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
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

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public java.sql.Date getbDate() {
        return bDate;
    }

    public void setbDate(java.sql.Date bDate) {
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

}
