package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.teacher.Interfaces.IController;
import dk.easv.gui.teacher.model.CitizenModel;
import dk.easv.gui.teacher.model.UserModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CitizenTeacherViewController extends SuperController implements Initializable, IController {


    @FXML
    private TableColumn displayStudentNameCol;
    @FXML
    private ImageView studentTipImageView;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TableView<Citizen> tempTableView;
    @FXML
    private TableColumn<Citizen, String> tempFnameCol;
    @FXML
    private TableColumn<Citizen, String> tempLnameCol;
    @FXML
    private TableColumn<Citizen, Integer> citizenDisplayID;
    @FXML
    private TableColumn<Citizen, String> citizenDisplayFname;
    @FXML
    private TableColumn<Citizen, String> citizenDisplayLname;
    @FXML
    private TableColumn<User, String> studentFnameCol;
    @FXML
    private TableColumn<User, String> studentLnameCol;
    @FXML
    private TableView<Citizen> displayTableView;
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
        setToolTip();
        try {
            tempTableView.setItems(citizenModel.getAllTemplatesOfCitizensObservable());
            studentTableView.setItems(userModel.getObservableStudents());
            studentTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setToolTip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Hold ''Ctrl'' knappen inde på dit tastatur, mens du klikker, for at vælge mere end en elev ad gangen");
        tooltip.setShowDelay(Duration.seconds(1));
        tooltip.setWrapText(true);
        Tooltip.install(studentTipImageView, tooltip);
    }



    public void handleRemoveCitizenFromStudentBtn(ActionEvent actionEvent) {
        try {
            User user = studentTableView.getSelectionModel().getSelectedItem();
            Citizen citizen = displayTableView.getSelectionModel().getSelectedItem();
            displayTableView.getItems().remove(citizen);
            citizenModel.deleteCitizenFromUser(citizen, user);
        } catch (Exception e) {
            error("vælg en elev og borger");
        }

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

    public void displayCitizensFromStudent(User user) {
        user = selectedUser();
        displayStudentNameCol.setText("Borgere tilknyttet: " + selectedUser().getFirstname() + " " + selectedUser().getLastname());
        citizenDisplayID.setCellValueFactory(new PropertyValueFactory<>("id"));
        citizenDisplayFname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        citizenDisplayLname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        displayTableView.setItems(citizenModel.getAllCitizenFromUserObservable(user));
    }

    public void setDescription(Citizen citizen) throws SQLException {
        if (citizen.getDescription() != null){
        descriptionTextArea.setText(citizen.getDescription());
        descriptionTextArea.setWrapText(true);
        }else{
            descriptionTextArea.setText(" ");
        }
    }


    public User selectedUser() {
        return studentTableView.getSelectionModel().getSelectedItem();
    }

    public void handleStudentTableCLicked(MouseEvent mouseEvent) {
        displayCitizensFromStudent(selectedUser());
    }

    public void handleSetDescription(MouseEvent mouseEvent) throws SQLException {
        setDescription(tempTableView.getSelectionModel().getSelectedItem());
    }

    @Override
    public void setUserInfo(User user) {

    }


    public void handleAddOneCitizenToAllStudents(ActionEvent actionEvent) throws SQLServerException {
        Citizen selectedTemplate = tempTableView.getSelectionModel().getSelectedItem();
        if (selectedTemplate != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Advarsel");
            alert.setContentText("Vil du gerne give alle elever, deres egen borger ud fra den valgte skabelon?");
            alert.setHeaderText("Er du sikker?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                ObservableList<User> studentList = studentTableView.getItems();
                for (User u : studentList) {
                    int newCitizenID = citizenModel.createCopyCitizen(selectedTemplate);
                    citizenModel.addUserToCitizen(newCitizenID, u);
                }
                displayCitizensFromStudent(studentTableView.getSelectionModel().getSelectedItem());
            }
        } else {
            errorMessage("Vælg venligst en skabelon");
        }
    }

    public void handleAddCitizenToStudent(ActionEvent actionEvent) {
        Citizen selectedTemplate = tempTableView.getSelectionModel().getSelectedItem();
        if (selectedTemplate != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Advarsel");
            alert.setContentText("Vil du gerne give alle valgte elever, deres egen borger ud fra den valgte skabelon?");
            alert.setHeaderText("Er du sikker?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                ObservableList<User> selectedStudents = studentTableView.getSelectionModel().getSelectedItems();
                int newCitizenID = citizenModel.createCopyCitizen(selectedTemplate);
                for (User u : selectedStudents) {
                    citizenModel.addUserToCitizen(newCitizenID, u);
                }
                displayCitizensFromStudent(studentTableView.getSelectionModel().getSelectedItem());
            }
        } else {
            errorMessage("Vælg venligst en skabelon");
        }
    }

}
