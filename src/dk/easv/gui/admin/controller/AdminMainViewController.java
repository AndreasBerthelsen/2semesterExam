package dk.easv.gui.admin.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.gui.admin.Model.AdminModel;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.Interfaces.IController;
import dk.easv.gui.teacher.model.UserModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
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

    /**
     * Initaliserer vores forskellige comboboxe med deres respektive comboboxe.
     * Her bliver de forskellige propertyValues også sat i de reprænsitive tableviews til både elever og lærere
     * @param location
     * @param resources
     */
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

    /**
     * Denne metode får vores addTeacher/AddElev view til at poppe op.
     * @throws IOException
     * @throws SQLServerException
     */
    public void handleAddUserBtn(ActionEvent actionEvent) throws IOException, SQLServerException {
        openScene("/dk/easv/gui/admin/view/AddTeacherView.fxml",true, "Tilføj en lærer eller elev", false);
        displayUsers(selectedSchool(actionEvent));
    }

    /**
     * Denne metode gør således at man kan logge ud af admin viewet, så man komme tilbage til startn
     * @throws IOException
     */
    public void handleSignOut(ActionEvent actionEvent) throws IOException {
        closeWindow(exitBtn);
        openScene("/dk/easv/gui/login/view/loginview.fxml",false, "Log ind som lærer, admin eller elev", false);
    }

    /**
     * Denne metode gør således, at når man trykker på comboboxen, bliver alle users fra den respektive
     * skole vist i de to tableviews ved anvendelse af displayUsers() metoden.
     * @throws SQLServerException
     */
    public void handleComboboxClicked(ActionEvent actionEvent) throws SQLServerException {
        displayUsers(selectedSchool(actionEvent));
    }

    /**
     * Denne metode gør således, at alle elever og lærere bliver sat baseret på den skole der er valgt
     * @param school
     * @throws SQLServerException
     */
    public void displayUsers(School school) throws SQLServerException {
        teacherTable.setItems(aM.getObservableTeachers(school));
        studentTable.setItems(aM.getObservableStudents(school));
    }

    /**
     * Denne metode gør således, at den tjekker på hvilken skole der er valgt i comboxen
     * @return
     */
    private School selectedSchool(ActionEvent actionEvent) {
        return comboBox.getSelectionModel().getSelectedItem();
    }

    /**
     * Sætter brugerens info
     * @param user - brugeren der bliver sat
     */
    @Override
    public void setUserInfo(User user) {
        this.user = user;
    }

    /**
     * Denne metode gør således, at når man vælger en lærer i lærer tabellen, føres læren med over i et
     * vindue, og åbner vores edit vindue.
     * Hvis der ikke er valgt en lærer, popper der en fejlbesked op.
     * @throws SQLServerException
     * @throws IOException
     */
    public void handleEditTeacherBtn(ActionEvent actionEvent) throws SQLServerException, IOException {
        User teacher = teacherTable.getSelectionModel().getSelectedItem();
        if (teacher != null) {
            openNewSceneAsUser2(teacher,"/dk/easv/gui/admin/view/EditTeacherOrStudentView.fxml","Rediger din elev");
            teacherTable.setItems(aM.getObservableTeachers(selectedSchool(actionEvent)));
        } else {
            errorMessage("Vælg den lærer, som du vil redigere");
        }
    }

    /**
     * I denne metode kan man vælge en lærer i lærer tabelview og slette læreren.
     * Hvis en lærer ikke er valgt til slet, popper der en fejlbesked op
     */
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

    /**
     * Denne metode gør således, at når man vælger en student i student tabellen, føres læren med over i et
     * vindue, og åbner vores edit vindue.
     * Hvis der ikke er valgt en student, popper der en fejlbesked op.
     * @throws SQLServerException
     * @throws IOException
     */
    public void handleEditStudentBtn(ActionEvent actionEvent) throws SQLServerException, IOException {
        User student = studentTable.getSelectionModel().getSelectedItem();
        if (student != null) {
            openNewSceneAsUser2(student,"/dk/easv/gui/admin/view/EditTeacherOrStudentView.fxml","Rediger din elev");
            studentTable.setItems(aM.getObservableStudents(selectedSchool(actionEvent)));
        } else {
            errorMessage("Vælg den elev, som du vil redigere");
        }
    }

    /**
     * I denne metode kan man vælge en student i lærer tabelview og slette læreren.
     * Hvis en lærer ikke er valgt til slet, popper der en fejlbesked op
     */
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

