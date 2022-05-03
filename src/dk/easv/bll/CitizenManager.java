package dk.easv.bll;

import dk.easv.dal.Facade;

import java.util.ArrayList;

public class CitizenManager {
    Facade facade;

    public CitizenManager() {
        facade = Facade.getInstance();
    }

    public ArrayList<String> getGeneralinfoFields(){
        return facade.getGeneralinfoFields();
    }
}
