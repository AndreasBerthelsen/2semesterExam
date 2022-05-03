package dk.easv.gui.login.model;

import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.bll.LoginManager;

import java.sql.SQLException;

public class LoginModel {
    private LoginManager lM;

    public LoginModel() {
        lM = new LoginManager();
    }

    public User loginUser(String username, String password) throws SQLException {
       return lM.loginUser(username,password);
    }

    public void createStudent(String firstName, String lastName,String username, String password) throws SQLException {
        lM.createUser(firstName, lastName,username, password, UserType.STUDENT);
    }

    public void createTeacher(String firstName, String lastName, String username, String password) throws SQLException {
        lM.createUser(firstName, lastName, username, password, UserType.TEACHER);
    }
}
