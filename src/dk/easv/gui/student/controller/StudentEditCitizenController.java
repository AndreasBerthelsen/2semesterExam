package dk.easv.gui.student.controller;

import dk.easv.be.Citizen;
import dk.easv.be.Section;
import dk.easv.gui.supercontroller.saveCitizenController;
import dk.easv.gui.teacher.Interfaces.ICitizenSelector;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

public class StudentEditCitizenController extends saveCitizenController implements ICitizenSelector {
    public ScrollPane genScrollPane;
    public TabPane funktionInnerTabPane;
    public TabPane helbredsInnerTabPane;
    public DatePicker dateInput;
    public TextField fNameInput;
    public TextField lNameInput;
    private CitizenModel cM;
    private Citizen citizen;
    private int id;

    private Map<String, TextArea> genInfoTextAreaMap;


    private Map<Integer, ComboBox<ImageView>> currentComboMap;
    private Map<Integer, ComboBox<String>> targetComboMap;
    private Map<Integer, TextArea> funkInfoTextAreaMap;

    private Map<Integer, ToggleGroup> healthToggleMap;
    private Map<Integer, TextArea> helbredTextAreaMap;

    public StudentEditCitizenController() throws IOException {
        this.cM = new CitizenModel();
        genInfoTextAreaMap = new LinkedHashMap<>();
        currentComboMap = new LinkedHashMap<>();
        targetComboMap = new LinkedHashMap<>();
        funkInfoTextAreaMap = new LinkedHashMap<>();
        healthToggleMap = new LinkedHashMap<>();
        helbredTextAreaMap = new LinkedHashMap<>();
    }

    //TODO REWORK ALT
    @Override
    public void setCitizen(Citizen citizen) {

        this.id = citizen.getId();
        fNameInput.setText(citizen.getFirstname());
        lNameInput.setText(citizen.getLastname());
        dateInput.setValue(citizen.getbDate().toLocalDate());
        setupGeneralInfo();
        setupFunkTab();
        setupHelbredTab();
    }

    private void setupGeneralInfo() {
        VBox vBox = new VBox();
        Map<String, String> generalInfoMap = citizen.getGenInfoText();
        for (String field : generalInfoMap.keySet()) {
            Label label = new Label(field);
            TextArea textArea = createTextArea(genInfoTextAreaMap, field, generalInfoMap.get(field));
            vBox.getChildren().add(label);
            vBox.getChildren().add(textArea);
        }
        genScrollPane.setContent(vBox);
    }

    private void setupFunkTab() {
        List<Image> imageList = createImages();
        List<Section> funkSectionList = cM.getFunkSections();

        Map<Integer, Integer> currentMap = citizen.getCurrentCombo();
        Map<Integer, Integer> targetMap = citizen.getTargetCombo();
        Map<Integer, String> funkTextMap = citizen.getFunkInfo();

        for (Section section : funkSectionList) {
            //tab for hver afdeling
            Tab tab = new Tab(section.getSectionTitle());
            VBox contentVBox = new VBox(80);
            contentVBox.setPrefWidth(800);
            for (int key : section.getProblemidTitleMap().keySet()) {
                String chunkTitle = section.getProblemidTitleMap().get(key);

                HBox headerBox = new HBox();
                Label headerLabel = new Label(chunkTitle);
                headerBox.getChildren().add(headerLabel);

                GridPane gridPane = new GridPane();
                gridPane.setHgap(50);
                gridPane.setVgap(10);
                gridPane.addRow(1, new Label("Nuværende Niveau"), new Label("Forventet Niveau"));

                //todo Fix start index det er mega shit

                ComboBox<ImageView> currentBox = createNiveauComboBox(currentComboMap, key, imageList, currentMap.get(key)); // måske replace med combobox factory
                ComboBox<String> targetBox = createTargetComboBox(targetComboMap, key, targetMap.get(key) - 1);//TODO YIKES
                gridPane.addRow(2, currentBox, targetBox);

                Label boxTitle = new Label("Borgerens Ønsker og mål");
                TextArea textArea = createTextArea(funkInfoTextAreaMap, key, funkTextMap.get(key));

                VBox chunk = new VBox();
                chunk.getChildren().addAll(headerBox, gridPane, boxTitle, textArea);
                contentVBox.getChildren().add(chunk);

                //TODO FIX SCROLLPANE SCROLL???
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(contentVBox);

                scrollPane.setPrefSize(gridPane.getPrefWidth(),500);
                tab.setContent(scrollPane);
            }
            funktionInnerTabPane.getTabs().add(tab);
        }

    }

    private void setupHelbredTab() {
        List<Section> healthSections = cM.getHealthSections();
        Map<Integer, Integer> relevansMap = citizen.getRelevansMap();
        Map<Integer, String> healthInfoMap = citizen.getHelbredInfo();

        //todo gem radio buttons og text til ale felter selv hvis tom

        for (Section section : healthSections) {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(50);
            int index = 0;

            for (int key : section.getProblemidTitleMap().keySet()) {
                String chunkTitle = section.getProblemidTitleMap().get(key);
                Label labelSub = new Label(chunkTitle);
                TextArea textArea = createTextArea(helbredTextAreaMap, key, healthInfoMap.get(key));
                textArea.setDisable(true);
                ArrayList<RadioButton> radioButtonList = createRadioButtons(healthToggleMap, key, relevansMap.get(key));
                gridPane.addRow(index++, labelSub, radioButtonList.get(0), radioButtonList.get(1), radioButtonList.get(2), textArea);
            }
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(gridPane);
            Tab tab = new Tab(section.getSectionTitle());
            tab.setContent(scrollPane);
            helbredsInnerTabPane.getTabs().add(tab);
        }
    }

    private TextArea createTextArea(Map<String, TextArea> map, String textAreaKey, String startingText) {
        if (startingText == null){
            startingText = "";
        }
        TextArea textArea = new TextArea(startingText);
        textArea.setWrapText(true);
        map.put(textAreaKey, textArea);
        return textArea;
    }

    private TextArea createTextArea(Map<Integer, TextArea> textAreaMap, int textAreaKey, String startingText) {
        if (startingText == null){
            startingText="";
        }
        TextArea textArea = new TextArea(startingText);
        textArea.setWrapText(true);
        textAreaMap.put(textAreaKey, textArea);
        return textArea;
    }

    private ComboBox<String> createTargetComboBox(Map<Integer, ComboBox<String>> comboBoxMap, int id, int startIndex) {
        ComboBox<String> comboBox = new ComboBox<>();
        //todo træk valgmuligheder fra database
        comboBox.getItems().add("Udføre selv");
        comboBox.getItems().add("Udfører dele af aktiviteten");
        comboBox.getItems().add("Udfører ikke selv aktiviteten");
        comboBox.getItems().add("Ikke relevant");
        comboBox.getSelectionModel().select(startIndex);
        comboBoxMap.put(id, comboBox);
        return comboBox;
    }

    private ComboBox<ImageView> createNiveauComboBox(Map<Integer, ComboBox<ImageView>> comboBoxMap, int id, List<Image> imageList, int startIndex) {
        List<ImageView> imgViewList = new ArrayList<>();
        for (Image image : imageList) {
            imgViewList.add(new ImageView(image));
        }
        ComboBox<ImageView> comboBox = new ComboBox<>(
                FXCollections.observableArrayList(imgViewList)
        );
        comboBox.setButtonCell(new ListCell<>() {
                                   @Override
                                   protected void updateItem(ImageView item, boolean empty) {
                                       super.updateItem(item, empty);
                                       if (item == null || empty) {
                                           setGraphic(null);
                                       } else {
                                           setGraphic(new ImageView(item.getImage()));
                                       }
                                   }
                               }
        );
        if (startIndex == 9) {
            startIndex = 5; //TODO FIX DET ER SHIT
        }
        comboBox.getSelectionModel().select(startIndex);
        comboBoxMap.put(id, comboBox);

        return comboBox;
    }

    private ArrayList<Image> createImages() {
        ArrayList<Image> list = new ArrayList<>();
        //5 ikke en god ide, ændre til funktion mængder??
        for (int i = 0; i <= 5; i++) {
            Image image = new Image("/dk/easv/Images/funktion" + i + ".png");
            list.add(image);
        }
        return list;
    }

    private ArrayList<RadioButton> createRadioButtons(Map<Integer, ToggleGroup> toggleMap, int key, int startIndex) {
        ArrayList<RadioButton> radioList = new ArrayList<>();
        ToggleGroup toggleGroup = new ToggleGroup();
        ArrayList<String> radioNames = new ArrayList<>();
        //TODO REPLACE STRINGS MED ENUM HOLY SHIT
        radioNames.add("Aktuel");
        radioNames.add("Potentiel");
        radioNames.add("Ikke relevant");

        for (int i = 0; i < 3; i++) {
            RadioButton radio = new RadioButton(radioNames.get(i));
            radio.setToggleGroup(toggleGroup);
            radio.setUserData(radioNames.get(i));

            //TODO FIX MED ENUM DET HER ER SHIT måske ændre visable
            if (Objects.equals(radio.getText(), "Aktuel") || Objects.equals(radio.getText(), "Potentiel")) {
                radio.setOnAction(event -> {
                    helbredTextAreaMap.get(key).setDisable(false);
                });
            } else {
                radio.setOnAction(event -> {
                    helbredTextAreaMap.get(key).setDisable(true);
                });
            }
            //todo fix start index, det her er shit
            if (toggleGroup.getSelectedToggle() == null) {
                if (i == 0 && startIndex == 1) {
                    radio.fire();
                    helbredTextAreaMap.get(key).setDisable(false);
                }
                if (i == 1 && startIndex == 2) {
                    radio.fire();
                    helbredTextAreaMap.get(key).setDisable(false);
                }
                if (i == 2 && startIndex == 3) {
                    radio.fire();
                }
            }
            radioList.add(radio);
        }
        toggleMap.put(key, toggleGroup);
        return radioList;
    }

    public void handleGembtn(ActionEvent actionEvent) {
        //rework
    }

    public void handleAnnullerbtn(ActionEvent actionEvent) {
        Stage stage = (Stage) fNameInput.getScene().getWindow();
        stage.close();
    }
}
