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
    private TableColumn<Citizen, Integer> citizenDisplayID;
    @FXML
    private TableColumn<Citizen, String> citizenDisplayFname;
    @FXML
    private TableColumn<Citizen, String> citizenDisplayLname;
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
    @FXML
    private TableView<String> templateTableView;

    private UserModel userModel;
    private CitizenModel citizenModel;

    public CitizenTeacherViewController() throws SQLServerException, IOException {
        userModel = new UserModel();
        citizenModel = new CitizenModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        citizenFnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        citizenLnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        studentFnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        studentLnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        try {
            citizenTableView.setItems(citizenModel.getAllCitizenObservable());
            studentTableView.setItems(userModel.getObservableStudents());
            
        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        }
    }

    public void handleCopyCitizenBtn(ActionEvent actionEvent) {
        Citizen selectedCitizen = citizenTableView.getSelectionModel().getSelectedItem();
        citizenModel.createCopyCitizen(selectedCitizen);
    }

    public void handleRemoveCitizenBtn(ActionEvent actionEvent) {
    }

    public void handleAddCitizenToStudentBtn(ActionEvent actionEvent) throws SQLServerException {
        boolean alreadyHasCitizen = false;
        Citizen selectedCitizen = citizenTableView.getSelectionModel().getSelectedItem();
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


    public void handleRemoveCitizenFromStudentBtn(ActionEvent actionEvent) {
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

    public void displayCitizensFromStudent(User user){
        user = selectedUser();
        citizenDisplayID.setCellValueFactory(new PropertyValueFactory<>("id"));
        citizenDisplayFname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        citizenDisplayLname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        displayTableView.setItems(citizenModel.getAllCitizenFromUserObservable(user));

    }

    public User selectedUser(){
    return studentTableView.getSelectionModel().getSelectedItem();
    }

    public void handleStudentTableCLicked(MouseEvent mouseEvent) {
        displayCitizensFromStudent(selectedUser());
    }
}
