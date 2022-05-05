package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.gui.supercontroller.SuperController;
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

public class AdminstrateStudentsController extends SuperController implements Initializable {
    @FXML
    private TableView<User> studentTable;
    @FXML
    private TableColumn<User, String> firstnameCol;
    @FXML
    private TableColumn<User, String> lastnameCol;

    private UserModel uM;

    public AdminstrateStudentsController() throws SQLServerException {
        uM = new UserModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstnameCol.setCellValueFactory(new PropertyValueFactory<User, String>("Firstname"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<User, String>("Lastname"));

        try {
            studentTable.setItems(uM.getObservableStudents());
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
    }

    public void handleAddStudentBtn(ActionEvent actionEvent) throws IOException, SQLServerException {
        openScene("/dk/easv/gui/teacher/view/AddStudentView.fxml",true, "Tilføj en elev", false);
        studentTable.setItems(uM.getObservableStudents());
    }

    public void handleDeleteStudentBtn(ActionEvent actionEvent) {
        if (studentTable.getSelectionModel().getSelectedItem() == null) {
            errorMessage("Vælg den elev, som du vil slette");
        } else {
            User user = (User) studentTable.getSelectionModel().getSelectedItem();
            if (confirmationBox("Er du sikker på, at du vil slette" + " " + user.getFirstname() + " " + user.getLastname() + "?").get() == ButtonType.YES) {
                uM.deleteUser(user);
                studentTable.getItems().remove(user);
            }
        }
    }

    public void handleUpdateStudentBtn(ActionEvent actionEvent) throws SQLServerException, IOException {
        User user = (User) studentTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            openNewSceneAsUser2(user,"/dk/easv/gui/teacher/view/EditStudentView.fxml","Rediger din elev");
            studentTable.setItems(uM.getObservableStudents());
        } else {
            errorMessage("Vælg den elev, som du vil redigere");
        }
    }
}
