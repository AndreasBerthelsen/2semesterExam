package dk.easv.gui.student.controller;

import dk.easv.be.*;
import dk.easv.bll.Util.FuncTabFactory;
import dk.easv.bll.Util.GenInfoTabFactory;
import dk.easv.bll.Util.HealthTabFactory;
import dk.easv.gui.supercontroller.SaveCitizenController;
import dk.easv.gui.Interfaces.ICitizenSelector;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;

public class StudentEditCitizenController extends SaveCitizenController implements Initializable, ICitizenSelector {
    public TabPane funktionInnerTabPane;
    public TabPane helbredsInnerTabPane;
    public DatePicker dateInput;
    public TextField fNameInput;
    public TextField lNameInput;
    public VBox genInfoVBox;
    public ComboBox<Date> dateSelectorCombo;
    public TabPane logFunkTabPane;

    public ProgressBar progressBar;
    public TabPane logHealthTabPane;
    public TabPane logBookTabPane;
    public DatePicker newDate;
    private CitizenModel cM;
    private Citizen citizen;
    private int id;
    ExecutorService service = Executors.newCachedThreadPool();
    private final Map<String, TextArea> genInfoNodeMap = new LinkedHashMap<>();
    private final Map<Integer, FunkNodeContainer> funkNodeMap = new LinkedHashMap<>();
    private final Map<Integer, HealthNodeContainer> healthNodeMap = new LinkedHashMap<>();
    // fill combo -> load journals

    public StudentEditCitizenController() throws IOException {
        this.cM = new CitizenModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newDate.setValue(LocalDate.now());
    }

    /**
     * sætter den valgte citizen efterfulgt af gui elementer der er
     * afhængig af hvilken den citizen
     * @param citizen
     */
    @Override
    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
        this.id = citizen.getId();
        fNameInput.setText(citizen.getFirstname());
        lNameInput.setText(citizen.getLastname());
        dateInput.setValue(citizen.getbDate().toLocalDate());
        fillDateSelector();
        setupGeneralInfo();
        setupFunkTab();
        setupHelbredTab();
    }

    /**
     * Fylder log date comboxen med datoer fra hvert indslag
     */
    private void fillDateSelector() {
        dateSelectorCombo.setItems(cM.getObservableLogDates(id));
    }

    /**
     * bygger generalinfo tabben med info fra citizen
     */
    private void setupGeneralInfo() {
        List<String> fieldList = cM.getGeneralinfoFields();
        Map<String, String> genInfo = cM.loadGenInfo(id, fieldList);
        genInfoVBox.getChildren().add(GenInfoTabFactory.createGenInfoContentWithInfo(fieldList, genInfoNodeMap, genInfo));

    }

    /**
     * Samler funktionsevnetilstande tabben
     */
    private void setupFunkTab() {
        service.submit(() -> {
            List<Section> funkSectionList = cM.getFunkSections();
            List<Tab> tabList = new ArrayList<>();
            for (Section section : funkSectionList) {
                tabList.add(FuncTabFactory.buildFunkTab(section, funkNodeMap));
            }
            Platform.runLater(() -> funktionInnerTabPane.getTabs().addAll(tabList));
        });
    }

    /**
     * samler helbredstilstande tabben
     */
    private void setupHelbredTab() {
        service.submit(() -> {
            List<Section> healthSections = cM.getHealthSections();

            List<Tab> tabList = new ArrayList<>();
            for (Section section : healthSections) {
                tabList.add(HealthTabFactory.buildHealthTab(section, healthNodeMap));
            }
            Platform.runLater(() -> helbredsInnerTabPane.getTabs().addAll(tabList));
        });
    }

    /**
     * Skaber en task der bygger en funktionsevnetilsands tab og helbredstilstands tab
     * med data fra den valgte dato
     * @param citizenId
     * @param date
     */
    private void setupOldInfo(int citizenId, Date date) {
        Map<Integer,FunkNodeContainer> funkDummy = new LinkedHashMap<>();
        Map<Integer,HealthNodeContainer> healthDummy = new LinkedHashMap<>();
        Task<Void> loadInfoFromDate = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                List<Section> fSections = cM.getFunkSections();
                //helbreds info
                Map<Integer, HealthResult> healthInfo = cM.loadHealthInfoFromDate(citizenId, date);
                List<Section> hSectionList = cM.getHealthSections();
                List<Tab> hTabList = new ArrayList<>();
                for (Section section : hSectionList) {
                    hTabList.add(HealthTabFactory.buildTabWithInfo(section, healthDummy, healthInfo, true));
                    updateProgress(hTabList.size(),fSections.size()+hSectionList.size());
                }

                //Funk info
                Map<Integer, FunkResult> funkInfo = cM.loadFunkInfoFromDate(citizenId, date);
                List<Tab> fTabList = new ArrayList<>();
                for (Section section : fSections) {
                    fTabList.add(FuncTabFactory.buildFunkTabWithInfo(section, funkDummy, funkInfo, true));
                    updateProgress(hTabList.size() + fTabList.size(),fSections.size()+hSectionList.size());
                }
                //Alle skabte tabs bliver tilføjet til GUI'en
                Platform.runLater(() -> {
                    logHealthTabPane.getTabs().setAll(hTabList);
                    logFunkTabPane.getTabs().setAll(fTabList);
                    logBookTabPane.getSelectionModel().selectLast();
                    logBookTabPane.getSelectionModel().selectFirst();
                });
                return null;
            }
        };
        service.submit(loadInfoFromDate);
        progressBar.progressProperty().bind(loadInfoFromDate.progressProperty());
    }

    /**
     * Gemmer alle info fra container objekter i result objekter
     * hvis et der ikke taget stilling til et punkt, vil der komme
     * en alert.
     * @param actionEvent
     */
    public void handleGembtn(ActionEvent actionEvent) {
        Map<Integer,FunkResult> funkResultMap = saveFunk(funkNodeMap);
        Map<Integer,HealthResult> healthResultMap = saveHealth(healthNodeMap);

        boolean validSave = true;
        for(FunkResult result: funkResultMap.values()){
            if (!result.isValid()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Du er ikke færdig!");
                alert.setContentText("Tag stilling til alle funktionsevnetilstande før du gemmer ");
                alert.showAndWait();
                validSave = false;
                break;
            }
        }
        for (HealthResult result : healthResultMap.values()){
            if (!result.isValid()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Du er ikke færdig!");
                alert.setContentText("Tag stilling til alle Helbredstilstande før du kan gemme");
                alert.showAndWait();
                validSave = false;
                break;
            }
        }

        if (validSave){
            try {
                cM.saveCitizen(citizen,Date.valueOf(newDate.getValue()),funkResultMap,healthResultMap,saveGeninfo(genInfoNodeMap));
                Stage stage = (Stage) fNameInput.getScene().getWindow();
                stage.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Håndtere annuller knappen
     * lukker vinduet uden at gemme
     * @param actionEvent
     */
    public void handleAnullerbtn(ActionEvent actionEvent) {
        Stage stage = (Stage) fNameInput.getScene().getWindow();
        stage.close();
    }

    /**
     * Håndtere on action for log combobox
     * @param actionEvent
     */
    public void handleDateCombo(ActionEvent actionEvent) {
        Date date = dateSelectorCombo.getSelectionModel().getSelectedItem();
        setupOldInfo(citizen.getId(),date);
    }


}
