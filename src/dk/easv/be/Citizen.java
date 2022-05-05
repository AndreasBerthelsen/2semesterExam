package dk.easv.be;

import java.util.Date;

public class Citizen {

    private int id;
    private String fName;
    private String lName;
    private Date bDate;

    public Citizen(int id, String fName, String lName, Date bDate) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.bDate = bDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getbDate() {
        return bDate;
    }

    public void setbDate(Date bDate) {
        this.bDate = bDate;
    }
}
