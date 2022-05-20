package dk.easv.gui.admin.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.gui.admin.Model.AdminModel;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.teacher.Interfaces.IController;
import dk.easv.gui.teacher.model.UserModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Struct;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMainViewController extends SuperController implements Initializable, IController {
    @FXML
    private ComboBox<School> comboBox;
    @FXML
    private TableView<User> studentTable;
    @FXML
    private TableView<User> teacherTable;
    @FXML
    private TableColumn<User, String> fNameTeacher;
    @FXML
    private TableColumn<User, String> lNameTeacher;
    @FXML
    private TableColumn<User, String> fNameStudent;
    @FXML
    private TableColumn<User, String> lNameStudent;
    @FXML
    private Button exitBtn;

    private AdminModel aM;
    private UserModel uM;

    User user;

    public AdminMainViewController() throws SQLServerException {
        aM = new AdminModel();
        uM = new UserModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fNameTeacher.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
        lNameTeacher.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
        fNameStudent.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
        lNameStudent.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
        try {
            ObservableList<School> listOfSchols = aM.getObservableSchools();
            comboBox.setItems(listOfSchols);
            comboBox.getSelectionModel().select(listOfSchols.get(0));
            displayUsers(listOfSchols.get(0));
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
    }

    public void handleAddUserBtn(ActionEvent actionEvent) throws IOException {
        openScene("/dk/easv/gui/admin/view/AddTeacherView.fxml",false, "Tilføj en lærer eller elev", false);
    }

    public void handleSignOut(ActionEvent actionEvent) throws IOException {
        closeWindow(exitBtn);
        openScene("/dk/easv/gui/login/view/loginview.fxml",false, "Log ind som lærer, admin eller elev", false);
    }

    public void handleComboboxClicked(ActionEvent actionEvent) throws SQLServerException {
        displayUsers(selectedSchool());
    }

    public void displayUsers(School school) throws SQLServerException {
        school = selectedSchool();
        teacherTable.setItems(aM.getObservableTeachers(school));
        studentTable.setItems(aM.getObservableStudents(school));
    }

    private School selectedSchool() {
        return comboBox.getSelectionModel().getSelectedItem();
    }

    @Override
    public void setUserInfo(User user) {
        this.user = user;
    }

    public void handleEditTeacherBtn(ActionEvent actionEvent) throws SQLServerException, IOException {
        User teacher = teacherTable.getSelectionModel().getSelectedItem();
        if (teacher != null) {
            openNewSceneAsUser2(teacher,"/dk/easv/gui/admin/view/EditTeacherOrStudentView.fxml","Rediger din elev");
            teacherTable.setItems(aM.getObservableTeachers(selectedSchool()));
        } else {
            errorMessage("Vælg den lærer, som du vil redigere");
        }
    }


    public void handleDeleteTeacherBtn(ActionEvent actionEvent) {
        if (teacherTable.getSelectionModel().getSelectedItem() == null) {
            errorMessage("Vælg venligst en lærer, som du vil slette");
        } else {
            User teacher = teacherTable.getSelectionModel().getSelectedItem();
            if (confirmationBox("Er du sikker på, at du vil slette" + " " + teacher.getFirstname() + " " + teacher.getLastname() + "?").get() == ButtonType.YES) {
                uM.deleteUser(teacher);
                teacherTable.getItems().remove(teacher);
            }
        }
    }

    public void handleEditStudentBtn(ActionEvent actionEvent) throws SQLServerException, IOException {
        User student = studentTable.getSelectionModel().getSelectedItem();
        if (student != null) {
            openNewSceneAsUser2(student,"/dk/easv/gui/admin/view/EditTeacherOrStudentView.fxml","Rediger din elev");
            studentTable.setItems(aM.getObservableStudents(selectedSchool()));
        } else {
            errorMessage("Vælg den elev, som du vil redigere");
        }
    }


    public void handleDeleteStudentBtn(ActionEvent actionEvent) {
        if (studentTable.getSelectionModel().getSelectedItem() == null) {
            errorMessage("Vælg venligst en elev, som du vil slette");
        } else {
            User student = studentTable.getSelectionModel().getSelectedItem();
            if (confirmationBox("Er du sikker på, at du vil slette" + " " + student.getFirstname() + " " + student.getLastname() + "?").get() == ButtonType.YES) {
                uM.deleteUser(student);
                studentTable.getItems().remove(student);
            }
        }
    }
}

