package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.Interfaces.IController;
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
    User user;

    public CitizenTeacherViewController() throws SQLServerException, IOException {
        userModel = new UserModel();
        citizenModel = new CitizenModel();
    }

    /**
     * Initistaliserer alle vores columns i de forskellige tableviews vi anvender i denne klasse
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //sets the list with names of the templates/copies
        tempFnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        tempLnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        //sets the list with names of the students


        citizenDisplayID.setCellValueFactory(new PropertyValueFactory<>("id"));
        citizenDisplayFname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        citizenDisplayLname.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        setToolTip();
        tempTableView.setItems(citizenModel.getAllTemplatesOfCitizensObservable());


    }

    /**
     * Her sættes en tooltip, som husker brugeren på at man skal trykke på control for at vælge
     * flere users.
     */
    private void setToolTip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Hold ''Ctrl'' knappen inde på dit tastatur, mens du klikker, for at vælge mere end en elev ad gangen");
        tooltip.setShowDelay(Duration.seconds(1));
        tooltip.setWrapText(true);
        Tooltip.install(studentTipImageView, tooltip);
    }


    /**
     * Denne metode fjerner en citizen fra en student.
     * Hvis der ikke er valgt både en elev og en citizen, popper der en fejlbesked op
     * @param actionEvent
     */
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
     * Fejl besked
     *
     * @param text
     */
    private void error(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR, text, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Fremviser alle citizens som er tilknyttet den valgte student. Denne metode anvendes i en mouseclicked metode
     * da dette er en metode som er lavet for at undgå redudant kode
     * og sætter en tekst med user fornavnet og efternavnet
     * @param user
     */
    private void displayCitizensFromStudent(User user) {
        displayStudentNameCol.setText("Borgere tilknyttet: " + user.getFirstname() + " " + user.getLastname());
        displayTableView.setItems(citizenModel.getAllCitizenFromUserObservable(user));
    }

    /**
     * Sætter beskrivelsen på hver citizen
     * Reverter tilbage til et tomt String, hvis der ikke er valgt en citizen
     * @param citizen - Citizen der er valgt
     * @throws SQLException
     */
    public void setDescription(Citizen citizen) throws SQLException {
        if (citizen.getDescription() != null){
        descriptionTextArea.setText(citizen.getDescription());
        descriptionTextArea.setWrapText(true);
        }else{
            descriptionTextArea.setText(" ");
        }
    }

    /**
     * Viser alle citizen tilknyttet til en student.
     * @param mouseEvent
     */
    public void handleStudentTableCLicked(MouseEvent mouseEvent) {
        User selectedUser = studentTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null){
            displayCitizensFromStudent(selectedUser);
        }
    }

    /**
     * Mouseevent, der anvender setDescription metode til at sætte beskrivelsen på hver citizen
     * når disse bliver trykket på
     * @param mouseEvent
     * @throws SQLException
     */
    public void handleSetDescription(MouseEvent mouseEvent) throws SQLException {
        setDescription(tempTableView.getSelectionModel().getSelectedItem());
    }

    /**
     * Sætter user infoet for læreren der er logget ind, og alle de forskellige
     * Properties for studenterne
     * Samtidig er studentTableView sat til at kunnne vælge flere, ved anvendelse af
     * SelectionMode.MULTIPLE
     * @param user
     */
    @Override
    public void setUserInfo(User user) {
        this.user = user;
        studentFnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        studentLnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        studentTableView.setItems(userModel.getAllStudentList(user.getSchoolID()));
        studentTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Denne metode gør således, at en citizen kan blive tildelt til alle studenter.
     * Dette gøres ved at vi henter vores fulde studentliste, og herefter kører et forloop igennem
     * der for hver student i listen bliver tildelt en individuel kopieret skabelon
     * Metoden createCopyCitizen() bliver anvendt til at lave en kopi af denne borger, og addCitizenToUser()
     * anvendes til adde en citizene til useren
     * @throws SQLServerException
     */
    public void handleAddOneCitizenToAllStudents() throws SQLServerException {
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
                    citizenModel.addCitizenToUser(newCitizenID, u);
                }
            }
        } else {
            errorMessage("Vælg venligst en skabelon");
        }
    }

    /**
     * Denne metode gør således, at vi kan vælge en vis mængde af users som får den samme kopieret citizen.
     * Dette gør således, at de forskellige users kan arbejde i gruppe om den samme bruger.
     * Gøres på samme måde med at køre listen igennem af de obserable users, men i stedet for
     * at den kører igennem alle users, kører den igennem de valgte.
     * Metoden createCopyCitizen() bliver anvendt til at lave en kopi af denne borger, og addCitizenToUser()
     * anvendes til adde en citizene til useren
     */
    public void handleAddCitizenToStudent() {
        Citizen selectedTemplate = tempTableView.getSelectionModel().getSelectedItem();
        ObservableList<User> selectedStudents = studentTableView.getSelectionModel().getSelectedItems();
        if (selectedTemplate != null && !selectedStudents.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Advarsel");
            alert.setContentText("Vil du gerne give alle valgte elever, deres egen borger ud fra den valgte skabelon?");
            alert.setHeaderText("Er du sikker?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                int newCitizenID = citizenModel.createCopyCitizen(selectedTemplate);
                for (User u : selectedStudents) {
                    citizenModel.addCitizenToUser(newCitizenID, u);
                }
                displayCitizensFromStudent(studentTableView.getSelectionModel().getSelectedItem());
            }
        } else {
            errorMessage("Vælg venligst en skabelon");
        }
    }

}
