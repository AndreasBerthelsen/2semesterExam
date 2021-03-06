package dk.easv.gui.teacher.model;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.bll.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class UserModel {
    private UserManager uM;
    private ObservableList<User> studentList;
    private ObservableList<User> getStudentList;

    public UserModel() throws SQLServerException {
        uM = new UserManager();
        studentList = FXCollections.observableArrayList();
        getStudentList = FXCollections.observableArrayList();
        studentList.addAll(getAllStudents());
    }

    public void createStudent(String firstName, String lastName, String username, String password, int schoolID) throws SQLException {
        uM.createUser(firstName, lastName, username, password, UserType.STUDENT, schoolID);
    }

    public void createUser(String firstname, String lastName, String username, String password, int schoolID, UserType userType) throws SQLException {
        uM.createUser(firstname,lastName,username,password,userType,schoolID);
    }

    public void deleteUser(User user) {
        uM.deleteUser(user);
    }

    public void updateUser(User user) {
        uM.udpateUser(user);
    }

    public void updateAdminUser(User user) { uM.updateAdminUser(user);}

    public void createTeacher(String firstName, String lastName, String username, String password, int schoolID) throws SQLException {
        uM.createUser(firstName, lastName, username, password, UserType.TEACHER, schoolID);
    }

    private List<User> getAllStudents() throws SQLServerException {
        return uM.getAllStudents();
    }

    public ObservableList<User> getObservableStudents() throws SQLServerException {
        studentList.setAll(getAllStudents());
        return studentList;
    }

    public void updatePassword(User user, String hashPassword) throws SQLServerException {
        uM.updatePassword(user, hashPassword);
    }
    public boolean checkUsername(String username){
        return uM.checkUsername(username);
    }

    public ObservableList<User> getAllStudentList(int schoolID) {
        getStudentList.addAll(getAllStudentsFromIndividualSchool(schoolID));
        getStudentList.setAll(getAllStudentsFromIndividualSchool(schoolID));
        return getStudentList;
    }

    private List<User> getAllStudentsFromIndividualSchool(int schoolID){
        return uM.getAllStudentFromIndividuelSchool(schoolID);
    }
}
