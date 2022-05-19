package dk.easv.gui.student.controller;

import dk.easv.be.Citizen;
import dk.easv.gui.supercontroller.saveCitizenController;
import dk.easv.gui.teacher.Interfaces.ICitizenSelector;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class StudentEditCitizenController extends saveCitizenController implements ICitizenSelector {
    public TabPane funktionInnerTabPane;
    public TabPane helbredsInnerTabPane;
    public DatePicker dateInput;
    public TextField fNameInput;
    public TextField lNameInput;
    public TextArea descriptionInput;
    public VBox genInfoVBox;
    public ComboBox<String> dateSelectorCombo;
    private CitizenModel cM;
    private Citizen citizen;
    private int id;


    // fill combo -> load journals

    public StudentEditCitizenController() throws IOException {
        this.cM = new CitizenModel();

    }


    @Override
    public void setCitizen(Citizen citizen) {
        this.id = citizen.getId();
        fNameInput.setText(citizen.getFirstname());
        lNameInput.setText(citizen.getLastname());
        dateInput.setValue(citizen.getbDate().toLocalDate());
        fillDateSelector();
        setupGeneralInfo();
        setupFunkTab();
        setupHelbredTab();
    }

    private void fillDateSelector() {
        dateSelectorCombo.setItems(cM.getObservableLogDates(id)); //eksemple [19-05-2022] Idag  -> [09-05-2022] -> [27-04-2022]
        dateSelectorCombo.getSelectionModel().select(0);
    }

    private void setupGeneralInfo() {

    }

    private void setupFunkTab() {


    }

    private void setupHelbredTab() {

    }


    public void handleGembtn(ActionEvent actionEvent) {
        //rework
    }


    public void handleAnullerbtn(ActionEvent actionEvent) {
    }

    public void handleDateCombo(ActionEvent actionEvent) {
        Date date= Date.valueOf(dateSelectorCombo.getSelectionModel().getSelectedItem().substring(0,9));

        System.out.println(dateSelectorCombo.getItems());
        System.out.println(date);
    }
}
