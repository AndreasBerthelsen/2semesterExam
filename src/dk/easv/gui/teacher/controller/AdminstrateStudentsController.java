package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.Interfaces.IController;
import dk.easv.gui.teacher.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminstrateStudentsController extends SuperController implements Initializable, IController {
    @FXML
    private TableView<User> studentTable;
    @FXML
    private TableColumn<User, String> firstnameCol;
    @FXML
    private TableColumn<User, String> lastnameCol;

    private UserModel uM;
    private User user;

    public AdminstrateStudentsController() throws SQLServerException {
        uM = new UserModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Denne metode sender useren hen til vinduet, hvori at de kan tilføje en student
     * @param actionEvent
     * @throws IOException
     * @throws SQLServerException
     */
    public void handleAddStudentBtn(ActionEvent actionEvent) throws IOException, SQLServerException {
        openNewSceneAsUser2(user,"/dk/easv/gui/teacher/view/AddStudentView.fxml","Tilføj en elev");
        studentTable.setItems(uM.getAllStudentList(user.getSchoolID()));
    }

    /**
     * Denne metode sender sletter en student fra student tablet viewet.
     * Hvis der ikke er valgt en user, popper der en fejlbesked op
     * @param actionEvent
     */
    public void handleDeleteStudentBtn(ActionEvent actionEvent) {
        if (studentTable.getSelectionModel().getSelectedItem() == null) {
            errorMessage("Vælg den elev, som du vil slette");
        } else {
            User user = studentTable.getSelectionModel().getSelectedItem();
            if (confirmationBox("Er du sikker på, at du vil slette" + " " + user.getFirstname() + " " + user.getLastname() + "?").get() == ButtonType.YES) {
                uM.deleteUser(user);
                studentTable.getItems().remove(user);
            }
        }
    }
    /**
     * Denne metode sender useren hen til vinduet, hvori at de kan redigere en student
     * @param actionEvent
     * @throws IOException
     * @throws SQLServerException
     */
    public void handleUpdateStudentBtn(ActionEvent actionEvent) throws SQLServerException, IOException {
        User student = studentTable.getSelectionModel().getSelectedItem();
        if (student != null) {
            openNewSceneAsUser2(student,"/dk/easv/gui/teacher/view/EditStudentView.fxml","Rediger din elev");
            studentTable.setItems(uM.getAllStudentList(user.getSchoolID()));
        } else {
            errorMessage("Vælg den elev, som du vil redigere");
        }
    }

    /**
     * Sætter useren informationer
     * @param user - Useren der bliver sat
     */
    @Override
    public void setUserInfo(User user) {
        this.user = user;
        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
        studentTable.setItems(uM.getAllStudentList(user.getSchoolID()));
    }
}
