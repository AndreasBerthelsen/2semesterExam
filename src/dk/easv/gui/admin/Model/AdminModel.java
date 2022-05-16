package dk.easv.gui.admin.Model;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.bll.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class AdminModel {
    private UserManager uM;
    private ObservableList<User> studentList;
    private ObservableList<User> teacherList;
    private ObservableList<School> schoolList;


    public AdminModel() throws SQLServerException {
        
        uM = new UserManager();
        studentList = FXCollections.observableArrayList();
        teacherList = FXCollections.observableArrayList();
        schoolList = FXCollections.observableArrayList();
    }

    private List<User> getAllStudents(School school) throws SQLServerException {
        return uM.getAllStudentsFromSchool(school);
    }

    private List<User> getAllTeachers(School school) {
        return uM.getAllTeachersFromSchool(school);
    }

    public ObservableList<User> getObservableStudents(School school) throws SQLServerException {
        studentList.addAll(getAllStudents(school));
        studentList.setAll(getAllStudents(school));
        return studentList;
    }

    public ObservableList<User> getObservableTeachers(School school) {
        teacherList.addAll(getAllTeachers(school));
        teacherList.setAll(getAllTeachers(school));
        return teacherList;
    }

    private List<School> getAllSchools() throws SQLServerException {
        return uM.getAllSchools();
    }

    public ObservableList<School> getObservableSchools() throws SQLServerException {
        schoolList.addAll(getAllSchools());
        schoolList.setAll(getAllSchools());
        return schoolList;
    }


}
