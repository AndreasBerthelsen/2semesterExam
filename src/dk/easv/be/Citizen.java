package dk.easv.be;

import java.util.Date;

public class Citizen {

    private int id;
    private String name;
    private Date bDate;

    public Citizen(int id, String name, Date bDate) {
        this.id = id;
        this.name = name;
        this.bDate = bDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setbDate(Date bDate) {
        this.bDate = bDate;
    }
}
