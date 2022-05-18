package dk.easv.gui.teacher.controller;

import dk.easv.be.*;
import dk.easv.bll.Util.FunktionTabFactory;
import dk.easv.bll.Util.HealthTabFactory;
import dk.easv.gui.supercontroller.saveCitizenController;
import dk.easv.gui.teacher.Interfaces.ICitizenSelector;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditSkabelonViewController extends saveCitizenController implements ICitizenSelector {
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
    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;

        setupGeneralInfo();
        setupFunkTab();
        setupHelbredTab();
    }

    private void setupGeneralInfo() {

    }

    private void setupFunkTab() {
        Map<Integer, FunkResult> funkInfo = cM.loadFunkInfo(citizen.getId());
        List<Section> sectionList = cM.getHealthSections();
        List<Tab> tabList = new ArrayList<>();
        for (Section section : sectionList){
            tabList.add(FunktionTabFactory.buildFunkTabWithInfo(section,funkNodeMap,funkInfo));
        }
    }

    private void setupHelbredTab() {
        Map<Integer, HealthResult> healthInfo = cM.loadHealthInfo(citizen.getId());
        List<Section> sectionList = cM.getHealthSections();

        List<Tab> tabList = new ArrayList<>();
        for (Section section : sectionList) {
            tabList.add(HealthTabFactory.buildTabWithInfo(section, healthNodeMap, healthInfo));
        }
        helbredsInnerTabPane.getTabs().addAll(tabList);
    }

    public void handleGembtn(ActionEvent actionEvent) {
        //rework
    }

    public void handleAnullerbtn(ActionEvent actionEvent) {
        Stage stage = (Stage) fNameInput.getScene().getWindow();
        stage.close();
    }
}
