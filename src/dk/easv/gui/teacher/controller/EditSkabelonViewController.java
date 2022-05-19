package dk.easv.gui.teacher.controller;

import dk.easv.be.*;
import dk.easv.bll.Util.FunktionTabFactory;
import dk.easv.bll.Util.GenInfoTabFactory;
import dk.easv.bll.Util.HealthTabFactory;
import dk.easv.gui.supercontroller.saveCitizenController;
import dk.easv.gui.teacher.Interfaces.ICitizenSelector;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditSkabelonViewController extends saveCitizenController implements Initializable, ICitizenSelector {
    public TextField fNameInput;
    public DatePicker dateInput;
    public TextField lNameInput;
    public TextArea descriptionInput;
    public DatePicker obsDatePicker;
    public VBox genInfoVBox;
    private CitizenModel cM;
    private Citizen citizen;
    private int id;
    ExecutorService service = Executors.newCachedThreadPool();


    //funktion
    public TabPane funktionInnerTabPane;
    private final Map<Integer, FunkNodeContainer> funkNodeMap = new LinkedHashMap<>();

    //Gen info
    private final Map<String, TextArea> genInfoNodeMap = new LinkedHashMap<>();

    //helbred
    public TabPane helbredsInnerTabPane;
    private final Map<Integer, HealthNodeContainer> healthNodeMap = new LinkedHashMap<>();

    public EditSkabelonViewController() throws IOException {
        this.cM = new CitizenModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        obsDatePicker.setValue(LocalDate.now());
    }

    @Override
    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;

        setupGeneralInfo();
        setupFunkTab();
        setupHelbredTab();
    }

    private void setupGeneralInfo() {
        fNameInput.setText(citizen.getFirstname());
        lNameInput.setText(citizen.getLastname());
        dateInput.setValue(citizen.getbDate().toLocalDate());
        descriptionInput.setText(citizen.getDescription());

        List<String> fieldList = cM.getGeneralinfoFields();
        Map<String, String> genInfo = cM.loadGenInfo(citizen.getId(), fieldList);
        genInfoVBox.getChildren().add(GenInfoTabFactory.createGenInfoContentWithInfo(fieldList, genInfoNodeMap, genInfo));
    }

    private void setupFunkTab() {
        service.submit(() -> {
            Map<Integer, FunkResult> funkInfo = cM.loadFunkInfo(citizen.getId());
            List<Section> sectionList = cM.getFunkSections();
            List<Tab> tabList = new ArrayList<>();
            for (Section section : sectionList) {
                tabList.add(FunktionTabFactory.buildFunkTabWithInfo(section, funkNodeMap, funkInfo));
            }
            Platform.runLater(() -> funktionInnerTabPane.getTabs().addAll(tabList));
        });
    }

    private void setupHelbredTab() {
        service.submit(() -> {
            Map<Integer, HealthResult> healthInfo = cM.loadHealthInfo(citizen.getId());
            List<Section> sectionList = cM.getHealthSections();

            List<Tab> tabList = new ArrayList<>();
            for (Section section : sectionList) {
                tabList.add(HealthTabFactory.buildTabWithInfo(section, healthNodeMap, healthInfo));
            }
            Platform.runLater(() -> helbredsInnerTabPane.getTabs().addAll(tabList));
        });

    }

    public void handleGembtn(ActionEvent actionEvent) {
        try {
            String fName = fNameInput.getText().trim();
            String lName = lNameInput.getText().trim();
            String description = descriptionInput.getText().trim();
            Date birthDate = Date.valueOf(dateInput.getValue());
            Citizen updatedCitizen = new Citizen(citizen.getId(), fName, lName, birthDate, description);
            Date obsDate = Date.valueOf(obsDatePicker.getValue());
            cM.updateTemplate(updatedCitizen, saveGeninfo(genInfoNodeMap), saveFunk(funkNodeMap), saveHealth(healthNodeMap),obsDate);
            Stage stage = (Stage) fNameInput.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("udfyld navne birthdate og desc");
        }

    }

    public void handleAnullerbtn(ActionEvent actionEvent) {
        Stage stage = (Stage) fNameInput.getScene().getWindow();
        stage.close();
    }


}
