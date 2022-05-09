package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.Section;
import dk.easv.gui.supercontroller.saveCitizenController;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.*;

public class NySkabelonMainController extends saveCitizenController implements Initializable {
    public TextField fNameInput;
    public DatePicker dateInput;
    public TextField lNameInput;
    //todo hent alle info fields med ny thread
    CitizenModel sM = new CitizenModel();
    //funktion
    private final HashMap<Integer, TextArea> funkInfoTextAreaMap = new HashMap<>();
    private final HashMap<Integer, ComboBox<ImageView>> currentComboMap = new HashMap<>();
    private final HashMap<Integer, ComboBox<String>> targetComboMap = new HashMap<>();
    public TabPane funktionInnerTabPane;

    //Gen info
    public javafx.scene.control.ScrollPane genScrollPane;
    private final HashMap<String, TextArea> genInfoTextAreaMap = new HashMap<>();
    private final ArrayList<String> genInfoFieldList = sM.getGeneralinfoFields();


    //helbred
    private final HashMap<Integer, ToggleGroup> healthToggleMap = new HashMap<>();
    private final HashMap<Integer, TextArea> helbredTextAreaMap = new HashMap<>();
    public TabPane helbredsInnerTabPane;

    public NySkabelonMainController() throws IOException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGeneralInfo();
        setupFunkTab();
        setupHelbredTab();
    }

    private void setupGeneralInfo() {
        VBox vBox = new VBox();
        vBox.setPrefWidth(1200);
        for (String field : genInfoFieldList) {
            Label label = new Label(field);
            label.getStylesheets().add("dk/easv/CSS/Skabelon.css");
            TextArea textArea = createTextArea(genInfoTextAreaMap, field);
            textArea.setId("textArea");

            vBox.getChildren().add(label);
            vBox.getChildren().add(textArea);
        }
        genScrollPane.setId("VBOX");
        genScrollPane.getStylesheets().add("dk/easv/CSS/Skabelon.css");
        genScrollPane.setContent(vBox);
        genScrollPane.getStylesheets().add("dk/easv/CSS/Skabelon.css");
        vBox.setId("VBOX");
    }

    private void setupFunkTab() {
        List<Image> imageList = createImages();
        List<Section> funkSectionList = sM.getFunkSections();

        for (Section section : funkSectionList) {
            //tab for hver afdeling
            Tab tab = new Tab(section.getSectionTitle());
            VBox contentVBox = new VBox(80);
            contentVBox.getStylesheets().add("dk/easv/CSS/Skabelon.css");
            contentVBox.setPrefWidth(800);
            contentVBox.setId("VBOX");
            for (int key : section.getProblemidTitleMap().keySet()) {
                String chunkTitle = section.getProblemidTitleMap().get(key);

                HBox headerBox = new HBox();
                Label headerLabel = new Label(chunkTitle);
                headerBox.getChildren().add(headerLabel);

                GridPane gridPane = new GridPane();
                gridPane.setHgap(50);
                gridPane.setVgap(10);
                gridPane.addRow(1, new Label("Nuværende Niveau"), new Label("Forventet Niveau"));

                ComboBox<ImageView> currentBox = createNiveauComboBox(currentComboMap, key, imageList); // måske replace med combobox factory
                ComboBox<String> targetBox = createTargetComboBox(targetComboMap, key); //Udføre selv | Udfører dele af aktiviteten | Udfører ikke selv aktiviteten | Ikke relevant
                gridPane.addRow(2, currentBox, targetBox);

                Label boxTitle = new Label("Borgerens Ønsker og mål");
                TextArea textArea = createTextArea(funkInfoTextAreaMap, key);
                textArea.setWrapText(true);

                VBox chunk = new VBox();
                chunk.getChildren().addAll(headerBox, gridPane, boxTitle, textArea);
                contentVBox.getChildren().add(chunk);

                //TODO FIX SCROLLPANE SCROLL???
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(contentVBox);
                scrollPane.getStylesheets().add("dk/easv/CSS/Skabelon.css");
                scrollPane.setId("VBOX");

                scrollPane.setPrefSize(contentVBox.getPrefWidth() + 20, 700);
                tab.setContent(scrollPane);
            }
            funktionInnerTabPane.getTabs().add(tab);
        }

    }

    private void setupHelbredTab() {
        List<Section> healthSections = sM.getHealthSections();

        for (Section section : healthSections) {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(50);
            gridPane.getStylesheets().add("dk/easv/CSS/Skabelon.css");
            gridPane.setId("VBOX");
            int index = 0;

            for (int key : section.getProblemidTitleMap().keySet()) {
                String chunkTitle = section.getProblemidTitleMap().get(key);
                Label labelSub = new Label(chunkTitle);
                ArrayList<RadioButton> radioButtonList = createRadioButtons(healthToggleMap, key);
                TextArea textArea = createTextArea(helbredTextAreaMap, key);
                textArea.setDisable(true);
                gridPane.addRow(index++, labelSub, radioButtonList.get(0), radioButtonList.get(1), radioButtonList.get(2), textArea);
            }
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(gridPane);
            Tab tab = new Tab(section.getSectionTitle());
            tab.setContent(scrollPane);
            helbredsInnerTabPane.getTabs().add(tab);
        }


    }

    private ComboBox<String> createTargetComboBox(HashMap<Integer, ComboBox<String>> comboBoxMap, int id) {
        ComboBox<String> comboBox = new ComboBox<>();
        //todo træk valgmuligheder fra database
        comboBox.getItems().add("Udføre selv");
        comboBox.getItems().add("Udfører dele af aktiviteten");
        comboBox.getItems().add("Udfører ikke selv aktiviteten");
        comboBox.getItems().add("Ikke relevant");

        comboBoxMap.put(id, comboBox);
        return comboBox;
    }

    private ComboBox<ImageView> createNiveauComboBox(HashMap<Integer, ComboBox<ImageView>> comboBoxMap, int id, List<Image> imageList) {
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

    private ArrayList<RadioButton> createRadioButtons(HashMap<Integer, ToggleGroup> toggleMap, int key) {
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
            radioList.add(radio);
        }
        toggleMap.put(key, toggleGroup);
        return radioList;
    }

    private TextArea createTextArea(HashMap<String, TextArea> helbredsTextMap, String textAreaKey) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        helbredsTextMap.put(textAreaKey, textArea);
        return textArea;
    }

    private TextArea createTextArea(HashMap<Integer, TextArea> textAreaMap, int textAreaKey) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textAreaMap.put(textAreaKey, textArea);
        return textArea;
    }

    public void handleGembtn(ActionEvent actionEvent) throws SQLServerException {
        if (!fNameInput.getText().isEmpty() && !lNameInput.getText().isEmpty() && dateInput.getValue() != null) {
            String fname = fNameInput.getText().trim();
            String lname = lNameInput.getText().trim();
            java.sql.Date date = Date.valueOf(dateInput.getValue().toString());

            //Gen info
            HashMap<String, String> genInfoText = saveGenInfo(genInfoTextAreaMap);

            //funktion
            HashMap<Integer, Integer> currentCombo = saveFunkCurrentCombo(currentComboMap);
            HashMap<Integer, Integer> targetCombo = saveFunkTargetCombo(targetComboMap);
            HashMap<Integer, String> funkInfoMap = saveFunkTextArea(funkInfoTextAreaMap);

            //Helbred
            HashMap<Integer, Integer> relevansMap = saveHealthRelevans(healthToggleMap);
            HashMap<Integer, String> helbredInfo = saveHealthInfo(helbredTextAreaMap);


            new Citizen(fname, lname, date, genInfoText, currentCombo, targetCombo, funkInfoMap, relevansMap, helbredInfo);
            sM.saveTemplate(new Citizen(fname, lname, date, genInfoText, currentCombo, targetCombo, funkInfoMap, relevansMap, helbredInfo));
            Stage stage = (Stage) fNameInput.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("udfyld navn og dato");
        }
    }

    public void handleAnullerbtn(ActionEvent actionEvent) {
        Stage stage = (Stage) fNameInput.getScene().getWindow();
        stage.close();
    }
}
