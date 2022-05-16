package dk.easv.gui.teacher.controller;

import dk.easv.be.FunkChunkAnswer;
import dk.easv.be.HealthChunkAnswer;
import dk.easv.be.Section;
import dk.easv.bll.Util.FunktionTabFactory;
import dk.easv.bll.Util.GenInfoTabFactory;
import dk.easv.bll.Util.HealthTabFactory;
import dk.easv.gui.supercontroller.saveCitizenController;
import dk.easv.gui.teacher.model.CitizenModel;
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

public class NySkabelonMainController extends saveCitizenController implements Initializable {
    public TextField fNameInput;
    public DatePicker dateInput;
    public TextField lNameInput;
    public TextArea descriptionInput;
    public DatePicker obsDatePicker;
    public VBox genInfoVBox;
    //todo hent alle info fields med ny thread
    CitizenModel sM = new CitizenModel();
    //funktion
    public TabPane funktionInnerTabPane;
    private final Map<Integer, FunkChunkAnswer> funkSectionAnswerMap = new LinkedHashMap<>();

    //Gen info
    private final Map<String,TextArea> genInfoAnserMap = new LinkedHashMap<>();

    //helbred
    public TabPane helbredsInnerTabPane;
    private final Map<Integer, HealthChunkAnswer> healthChunkAnswerMap = new LinkedHashMap<>();

    public NySkabelonMainController() throws IOException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        obsDatePicker.setValue(LocalDate.now());
        setupFunkTab();
        setupHelbredTab();
        setupGenInfoTab();
    }

    private void setupGenInfoTab() {
        List<String> fieldList = sM.getGeneralinfoFields();
            genInfoVBox.getChildren().add(GenInfoTabFactory.createGenInfoContent(fieldList,genInfoAnserMap));
    }


    private void setupFunkTab() {
        List<Section> funkSectionList = sM.getFunkSections();

        for (Section section : funkSectionList) {
            funktionInnerTabPane.getTabs().add(FunktionTabFactory.buildFunkTab(section, funkSectionAnswerMap));
        }
    }

    private void setupHelbredTab() {
        List<Section> healthSections = sM.getHealthSections();

        for (Section section : healthSections) {
                  helbredsInnerTabPane.getTabs().add(HealthTabFactory.buildHealthTab(section,healthChunkAnswerMap));
        }
    }

    public void handleGembtn(ActionEvent actionEvent) {
        try {
            String fName = fNameInput.getText().trim();
            String lName = lNameInput.getText().trim();
            java.sql.Date birthDate = Date.valueOf(dateInput.getValue().toString());
            String description = descriptionInput.getText().trim();
            java.sql.Date obsDate = Date.valueOf(obsDatePicker.getValue().toString());

            sM.saveTemplate(fName, lName, birthDate,description, genInfoAnserMap, funkSectionAnswerMap, healthChunkAnswerMap,obsDate);
            Stage stage = (Stage) fNameInput.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace(); //todo fix date snak med jens
            System.out.println("error");
        }


    }

    public void handleAnullerbtn(ActionEvent actionEvent) {
        Stage stage = (Stage) fNameInput.getScene().getWindow();
        stage.close();
    }
}
