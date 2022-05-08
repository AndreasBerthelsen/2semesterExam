package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.gui.teacher.model.CitizenModel;
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
import java.util.ResourceBundle;

public class CitizenTeacherViewController implements Initializable {


    @FXML
    private TableView<Citizen> tempTableView;
    @FXML
    private TableColumn<Citizen, String> tempFnameCol;
    @FXML
    private TableColumn<Citizen, String> tempLnameCol;
    @FXML
    private TableColumn<Citizen, Integer> tempDisplayID;
    @FXML
    private TableColumn<Citizen, String> tempDisplayFname;
    @FXML
    private TableColumn<Citizen, String> tempDisplayLname;
    @FXML
    private TableColumn<Citizen, String> citizenFnameCol;
    @FXML
    private TableColumn<Citizen, String> citizenLnameCol;
    @FXML
    private TableColumn<User, String> studentFnameCol;
    @FXML
    private TableColumn<User, String> studentLnameCol;
    @FXML
    private TableView<Citizen> displayTableView;
    @FXML
    private TableView<Citizen> citizenTableView;
    @FXML
    private TableView<User> studentTableView;


    private UserModel userModel;
    private CitizenModel citizenModel;

    public CitizenTeacherViewController() throws SQLServerException, IOException {
        userModel = new UserModel();
        citizenModel = new CitizenModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //sets the list with names of the templates/copies
        tempFnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        tempLnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        //sets the list with names of the students
        studentFnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        studentLnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        //sets the list with names of the citizens
        citizenFnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        citizenLnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        try {
            tempTableView.setItems(citizenModel.getAllTemplatesOfCitizensObservable());
            studentTableView.setItems(userModel.getObservableStudents());
            citizenTableView.setItems(citizenModel.getAllCitizenObservable());

        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        }
    }

    public void handleCopyCitizenBtn(ActionEvent actionEvent) {

    }

    public void handleRemoveCitizenBtn(ActionEvent actionEvent) {
    }

    public void handleAddTemplateToStudentBtn(ActionEvent actionEvent) throws SQLServerException {
        boolean alreadyHasCitizen = false;
        Citizen selectedCitizen = tempTableView.getSelectionModel().getSelectedItem();
        User selectedUser = studentTableView.getSelectionModel().getSelectedItem();

        if (selectedCitizen != null && selectedUser != null) {
            ObservableList<Citizen> citizensObservable = citizenModel.getAllCitizenFromUserObservable(selectedUser);
            for (Citizen citizen : citizensObservable) {
                if (citizen.getId() == selectedCitizen.getId()) {
                    alreadyHasCitizen = true;
                    break;
                }
            }

            if (!alreadyHasCitizen) {
                citizenModel.addUserToCitizen(selectedCitizen, selectedUser);
                citizensObservable.add(selectedCitizen);
                displayTableView.getItems().clear();
                displayTableView.setItems(citizensObservable);
            } else {
                //error(selectedCitizen.getfirstname() + " " + selectedCitizen.getlastname() + " er allerede tilføjet " + selectedUser.getFirstname() + " " + selectedUser.getLastname());
            }
        } else {
            error("Vælg en Borger og den studerende, der skal tilknyttes");
        }

    }


    public void handleRemoveTemplateFromStudentBtn(ActionEvent actionEvent) {
        citizenModel.deleteCitizenFromUser(displayTableView.getSelectionModel().getSelectedItem(), studentTableView.getSelectionModel().getSelectedItem());
    }

    /**
     * Error message
     *
     * @param text
     */
    private void error(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
    }

    public void displayTempCitizensFromStudent(User user) {
        user = selectedUser();
        tempDisplayID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tempDisplayFname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        tempDisplayLname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        displayTableView.setItems(citizenModel.getAllCitizenFromUserObservable(user));

    }

    public User selectedUser() {
        return studentTableView.getSelectionModel().getSelectedItem();
    }

    public void handleStudentTableCLicked(MouseEvent mouseEvent) {
        displayTempCitizensFromStudent(selectedUser());
    }
}
