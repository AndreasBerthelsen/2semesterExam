package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.teacher.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    public void handleAddStudentBtn(ActionEvent actionEvent) throws IOException, SQLServerException {
        openScene("/dk/easv/gui/teacher/view/AddStudentView.fxml",true, "Tilføj en elev", false);
        studentTable.setItems(uM.getObservableStudents());
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
}
