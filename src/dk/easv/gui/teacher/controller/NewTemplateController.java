package dk.easv.gui.teacher.controller;

import dk.easv.be.FunkNodeContainer;
import dk.easv.be.HealthNodeContainer;
import dk.easv.be.Section;
import dk.easv.bll.Util.FuncTabFactory;
import dk.easv.bll.Util.GenInfoTabFactory;
import dk.easv.bll.Util.HealthTabFactory;
import dk.easv.gui.supercontroller.SaveCitizenController;
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

public class NewTemplateController extends SaveCitizenController implements Initializable {
    public TextField fNameInput;
    public DatePicker dateInput;
    public TextField lNameInput;
    public TextArea descriptionInput;
    public DatePicker obsDatePicker;
    public VBox genInfoVBox;
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

    public NewTemplateController() throws IOException {
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
                tabList.add(FuncTabFactory.buildFunkTab(section, funkNodeMap));
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
        String fName = fNameInput.getText().trim();
        String lName = lNameInput.getText().trim();
        if (!fName.isEmpty() && !lName.isEmpty() && dateInput.getValue() != null) {
            java.sql.Date birthDate = Date.valueOf(dateInput.getValue().toString());
            try {
                String description = descriptionInput.getText().trim();
                java.sql.Date obsDate = Date.valueOf(obsDatePicker.getValue().toString());

                sM.saveTemplate(fName, lName, birthDate, description,
                        saveGeninfo(genInfoNodeMap),
                        saveFunk(funkNodeMap),
                        saveHealth(healthNodeMap),
                        obsDate);
                Stage stage = (Stage) fNameInput.getScene().getWindow();
                stage.close();
            } catch (Exception ignored) {
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Udfyld fornavn, efternavn og fødselsdato");
            alert.showAndWait();
        }
    }

    public void handleAnullerbtn(ActionEvent actionEvent) {
        Stage stage = (Stage) fNameInput.getScene().getWindow();
        stage.close();
    }
}
