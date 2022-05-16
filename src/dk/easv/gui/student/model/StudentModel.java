package dk.easv.gui.student.model;

import dk.easv.be.Citizen;
import dk.easv.bll.CitizenManager;

import java.io.IOException;
import java.sql.SQLException;

public class StudentModel {

    private CitizenManager citizenManager;

    public StudentModel() throws IOException {
        citizenManager = new CitizenManager();
    }

    public void lastChanged(Citizen citizen) throws SQLException {
        citizenManager.updateLastEdited(citizen);
    }
}
