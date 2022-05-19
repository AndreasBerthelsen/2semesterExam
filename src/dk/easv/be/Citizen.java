package dk.easv.be;


import java.sql.Date;

import java.util.HashMap;


import java.sql.Date;
import java.util.Map;

public class Citizen {

    private int id;

    private String firstname;
    private String lastname;
    private String description;

    private java.sql.Date bDate;

    private java.sql.Date lastChanged;



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

    public Citizen(int id, String firstname, String lastname, java.sql.Date bDate, java.sql.Date lastChanged) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bDate = bDate;
        this.lastChanged = lastChanged;
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


    public String getDescription() {
        return description;
    }

    public Date getLastChanged() {
        return lastChanged;
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
