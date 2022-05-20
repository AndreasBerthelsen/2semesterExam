package dk.easv.gui.teacher.controller;

import dk.easv.be.FunkNodeContainer;
import dk.easv.be.HealthNodeContainer;
import dk.easv.be.Section;
import dk.easv.bll.Util.FunktionTabFactory;
import dk.easv.bll.Util.GenInfoTabFactory;
import dk.easv.bll.Util.HealthTabFactory;
import dk.easv.gui.supercontroller.saveCitizenController;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NySkabelonMainController extends saveCitizenController implements Initializable {
    public TextField fNameInput;
    public DatePicker dateInput;
    public TextField lNameInput;
    public TextArea descriptionInput;
    public DatePicker obsDatePicker;
    public VBox genInfoVBox;
    //todo hent alle info fields med ny thread
    CitizenModel sM = new CitizenModel();
    ExecutorService service = Executors.newCachedThreadPool();
    //funktion
    public TabPane funktionInnerTabPane;
    private final Map<Integer, FunkNodeContainer> funkNodeMap = new LinkedHashMap<>();

    //Gen info
    private final Map<String, TextArea> genInfoNodeMap = new LinkedHashMap<>();

    //helbred
    public TabPane helbredsInnerTabPane;
    private final Map<Integer, HealthNodeContainer> healthNodeMap = new LinkedHashMap<>();

    public NySkabelonMainController() throws IOException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        obsDatePicker.setValue(LocalDate.now());
        try {
            setupGenInfoTab();
            setupFunkTab();
            setupHelbredTab();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setupGenInfoTab() throws ExecutionException, InterruptedException {
        List<String> fieldList = service.submit((Callable<List<String>>) () -> sM.getGeneralinfoFields()).get();
        genInfoVBox.getChildren().add(GenInfoTabFactory.createGenInfoContent(fieldList, genInfoNodeMap));
    }

    private void setupFunkTab() throws ExecutionException, InterruptedException {
        service.submit(() -> {
            List<Section> funkSectionList = sM.getFunkSections();
            List<Tab> tabList = new ArrayList<>();
            for (Section section : funkSectionList) {
                tabList.add(FunktionTabFactory.buildFunkTab(section, funkNodeMap));
            }
            Platform.runLater(() -> funktionInnerTabPane.getTabs().addAll(tabList));
        });
    }

    private void setupHelbredTab() throws ExecutionException, InterruptedException {
        service.submit(() -> {
            List<Section> healthSections = sM.getHealthSections();

            List<Tab> tabList = new ArrayList<>();
            for (Section section : healthSections) {
                tabList.add(HealthTabFactory.buildHealthTab(section, healthNodeMap));
            }
            Platform.runLater(() -> helbredsInnerTabPane.getTabs().addAll(tabList));
        });

    }

    public void handleGembtn(ActionEvent actionEvent) {
        try {
            String fName = fNameInput.getText().trim();
            String lName = lNameInput.getText().trim();
            java.sql.Date birthDate = Date.valueOf(dateInput.getValue().toString());
            String description = descriptionInput.getText().trim();
            java.sql.Date obsDate = Date.valueOf(obsDatePicker.getValue().toString());

            sM.saveTemplate(fName, lName, birthDate, description, saveGeninfo(genInfoNodeMap), saveFunk(funkNodeMap), saveHealth(healthNodeMap), obsDate);
            Stage stage = (Stage) fNameInput.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
        }
    }

    public void handleAnullerbtn(ActionEvent actionEvent) {
        Stage stage = (Stage) fNameInput.getScene().getWindow();
        stage.close();
    }
}
