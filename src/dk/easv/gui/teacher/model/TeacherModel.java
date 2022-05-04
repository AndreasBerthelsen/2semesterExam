package dk.easv.gui.teacher.model;

import dk.easv.be.UserType;
import dk.easv.bll.LoginManager;
import dk.easv.bll.TeacherManager;

import java.sql.SQLException;

public class TeacherModel {
    private TeacherManager tM;

    public TeacherModel() {
        tM = new TeacherManager();
    }

    public void createStudent(String firstName, String lastName,String username, String password) throws SQLException {
        tM.createUser(firstName, lastName,username, password, UserType.STUDENT);
    }

    public void createTeacher(String firstName, String lastName, String username, String password) throws SQLException {
        tM.createUser(firstName, lastName, username, password, UserType.TEACHER);
    }
}
