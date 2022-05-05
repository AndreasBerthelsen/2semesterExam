package dk.easv.gui.teacher.controller;

import dk.easv.gui.teacher.model.CitizenModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class NySkabelonMainController implements Initializable {
    //Gen info
    private final HashMap<String, TextArea> genInfoTextAreaMap = new HashMap<>();
    //funktion
    private final HashMap<String, TextArea> funkInfoTextAreaMap = new HashMap<>();
    public BorderPane borderpane;
    public javafx.scene.control.ScrollPane genScrollPane;
    public TabPane funktionInnerTabPane;

    //todo hent alle info fields med ny thread
    public TabPane helbredsInnerTabPane;
    CitizenModel sM = new CitizenModel();
    private final ArrayList<String> genInfoFieldList = sM.getGeneralinfoFields();
    private final HashMap<Integer, String> funkTilstandsList = sM.getFunktionsTilstande();
    private final HashMap<Integer, ArrayList<String>> funkProblemMap = sM.getFunktionsVandskligheder();

    /*
    //section
        overcat ID:
        gui string:
        own ID

        Arraylist<Section>
     */

    private HashMap<Integer,ArrayList<HashMap<Integer,String>>> cancer = new HashMap<>();

    //helbred
    private HashMap<String, ToggleGroup> toggleMap = new HashMap<>();
    private HashMap<String, TextArea> helbredTextAreaMap = new HashMap<>();

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
        for (String field : genInfoFieldList) {
            Label label = new Label(field);
            TextArea textArea = createTextArea(genInfoTextAreaMap, field);
            vBox.getChildren().add(label);
            vBox.getChildren().add(textArea);
        }
        genScrollPane.setContent(vBox);
    }

    private void setupFunkTab() {
        List<Image> imageList = createImages();
        for (int key : funkTilstandsList.keySet()) {
            //tab for hver afdeling
            Tab tab = new Tab(funkTilstandsList.get(key));

            VBox contentVBox = new VBox(80);
            contentVBox.setPrefWidth(800);

            for (String string : funkProblemMap.get(key)) {
                //underpunkter

                HBox headerBox = new HBox();
                Label headerLabel = new Label(string);
                headerBox.getChildren().add(headerLabel);

                GridPane gridPane = new GridPane();
                gridPane.setHgap(50);
                gridPane.setVgap(10);
                gridPane.addRow(1, new Label("Nuværende Niveau"), new Label("Forventet Niveau"));

                ComboBox<ImageView> currentBox = createNiveauComboBox(imageList, string); // måske replace med combobox factory
                ComboBox<String> targetBox = createTargetComboBox(string); //Udføre selv | Udfører dele af aktiviteten | Udfører ikke selv aktiviteten | Ikke relevant
                gridPane.addRow(2, currentBox, targetBox);

                Label boxTitle = new Label("Borgerens Ønsker og mål");
                TextArea textArea = createTextArea(funkInfoTextAreaMap, string);
                textArea.setId(string + "TextArea");
                textArea.setWrapText(true);

                VBox section = new VBox();
                section.getChildren().addAll(headerBox, gridPane, boxTitle, textArea);
                contentVBox.getChildren().add(section);


                //TODO FIX SCROLLPANE SCROLL???
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(contentVBox);


                scrollPane.setPrefSize(contentVBox.getPrefWidth() + 20, 700);
                tab.setContent(scrollPane);
            }
            funktionInnerTabPane.getTabs().add(tab);
        }

    }

    private void setupHelbredTab() {
        //gen tabs -> gridpane -> header + radiobuttons -> textArea (Radio buttons event handler til inactive textArea)
        HashMap<Integer, String> tilstandsMap = sM.getHelbredsTilstande();
        HashMap<Integer, ArrayList<String>> vansklighedsMap = sM.getHelbredVanskligheder();
        for (int key : tilstandsMap.keySet()) {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(50);

            int index = 0;
            for (String subTitles : vansklighedsMap.get(key)) {
                Label labelSub = new Label(subTitles);
                ArrayList<RadioButton> radioButtonList = createRadioButtons(toggleMap, subTitles);
                TextArea textArea = createTextArea(helbredTextAreaMap, subTitles);
                textArea.setDisable(true);
                gridPane.addRow(index++, labelSub, radioButtonList.get(0), radioButtonList.get(1), radioButtonList.get(2), textArea);
            }
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(gridPane);
            Tab tab = new Tab(tilstandsMap.get(key));
            tab.setContent(scrollPane);
            helbredsInnerTabPane.getTabs().add(tab);
        }
    }

    private ComboBox<String> createTargetComboBox(String id) {
        String idSuffix = "Target";
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setId(id + idSuffix);
        comboBox.getItems().add("Udføre selv");
        comboBox.getItems().add("Udfører dele af aktiviteten");
        comboBox.getItems().add("Udfører ikke selv aktviteten");
        comboBox.getItems().add("Ikke relevant");
        return comboBox;
    }

    private ComboBox<ImageView> createNiveauComboBox(List<Image> imageList, String id) {
        String idSuffix = "Current";
        List<ImageView> imgViewList = new ArrayList<>();
        for (Image image : imageList) {
            imgViewList.add(new ImageView(image));
        }
        ComboBox<ImageView> comboBox = new ComboBox<>(
                FXCollections.observableArrayList(imgViewList)
        );
        comboBox.setId(id + idSuffix);


        //TODO update databasen med nye textfelt + forventet status
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

    private ArrayList<RadioButton> createRadioButtons(HashMap<String, ToggleGroup> toggleMap, String toggleMapKey) {
        ArrayList<RadioButton> radioList = new ArrayList<>();
        ToggleGroup toggleGroup = new ToggleGroup();
        ArrayList<String> radioNames = new ArrayList<>();
        radioNames.add("Relevant");
        radioNames.add("Potentielt");
        radioNames.add("Ikke relevant");

        for (int i = 0; i < 3; i++) {
            RadioButton radio = new RadioButton(radioNames.get(i));
            radio.setToggleGroup(toggleGroup);
            radio.setUserData(radioNames.get(i));

            //TODO FIX MED ENUM DET HER ER SHIT måske ændre visable
            if (Objects.equals(radio.getText(), "Relevant") || Objects.equals(radio.getText(), "Potentielt")) {
                radio.setOnAction(event -> {
                    helbredTextAreaMap.get(toggleMapKey).setDisable(false);
                    // textAreaMap.get(toggleMapKey).setVisible(true);
                });
            } else {
                radio.setOnAction(event -> {
                    helbredTextAreaMap.get(toggleMapKey).setDisable(true);
                    // textAreaMap.get(toggleMapKey).setVisible(false);
                });
            }
            radioList.add(radio);
        }
        toggleMap.put(toggleMapKey, toggleGroup);
        return radioList;
    }


    private TextArea createTextArea(HashMap<String, TextArea> helbredsTextMap, String textAreaKey) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        helbredsTextMap.put(textAreaKey, textArea);
        return textArea;
    }

    private TextArea createTextArea(HashMap<Integer, TextArea> helbredsTextMap, int textAreaKey) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        helbredsTextMap.put(textAreaKey, textArea);
        return textArea;
    }

    public void handleGembtn(ActionEvent actionEvent) {
        HashMap<String, String> genInfoText = gemGenInfo();

        //funktion
        HashMap<Integer, Integer> currentCombo;
        HashMap<Integer, Integer> targetCombo;
        HashMap<String, String> funkInfoMap = gemFunkTextArea();;

        //Helbred
        HashMap<Integer, Integer> relevansMap;
        HashMap<Integer, String> helbredInfo;


        //ændre citezen så den passer overstående
        //new Citizen( genInfoText,  currentCombo, targetCombo,  funkInfo, relevansMap,  helbredInfo);

    }

    private HashMap<String, String> gemGenInfo() {
        HashMap<String, String> map = new HashMap<>();
        for (String key : genInfoTextAreaMap.keySet()) {
            TextArea textArea = genInfoTextAreaMap.get(key);
            if (!textArea.getText().isBlank()) {
                map.put(key, textArea.getText().trim());
            }
        }
        return map;
    }

    private HashMap<String,String> gemFunkTextArea() {
        HashMap<String,String> map = new HashMap<>();
        for (String key : funkInfoTextAreaMap.keySet()) {
            TextArea textArea = funkInfoTextAreaMap.get(key);
            if (!textArea.getText().isBlank()) {
               map.put(key,textArea.getText().trim());
            }
        }
        System.out.println(map);

        /*
        //TODO REWORK LANG CANCER METODE TIL AT BRUGE HASHMAP

        // Giga forloop for at tjekke alle inputs i funktioninnerpane
        for (Tab tab : funktionInnerTabPane.getTabs()) {
            ScrollPane scrollPane = (ScrollPane) tab.getContent();
            VBox contentVBox = (VBox) scrollPane.getContent();
            List<Node> list = contentVBox.getChildren();
            for (Node n : list) {
                if (n instanceof VBox section) {
                    for (Node sN : section.getChildren()) {
                        //3 cases nu combobox, ønsket combobox og textarea
                        if (sN instanceof GridPane gridPane) {
                            for (Node gN : gridPane.getChildren()) {
                                if (gN instanceof ComboBox comboBox) {
                                    String comboId = comboBox.getId();
                                    if (comboId != null) {
                                        if (comboId.endsWith("Current")) {
                                            int index = comboBox.getSelectionModel().getSelectedIndex();
                                            System.out.println(comboId + ": " + index);
                                        } else if (comboId.endsWith("Target")) {
                                            int index = comboBox.getSelectionModel().getSelectedIndex();
                                            System.out.println(comboId + ": " + index);
                                        }
                                    }
                                }
                            }
                        } else if (sN instanceof TextArea textArea) {
                            String string = textArea.getText();
                            if (!string.isBlank()) {
                                System.out.println(textArea.getId() + string);
                            }
                        }
                    }
                }
            }
        }

         */
        return map;
    }

    private void gemHelbred() {
        //TODO PRINTER -> GEM I DB
        for (String key : toggleMap.keySet()) {
            ToggleGroup currentGroup = toggleMap.get(key);
            if (currentGroup.getSelectedToggle() != null) {
                System.out.println(key + ": " + currentGroup.getSelectedToggle().getUserData());
            }
        }

        for (String key : helbredTextAreaMap.keySet()) {
            TextArea textArea = helbredTextAreaMap.get(key);
            if (!textArea.isDisabled()) {
                System.out.println(key + ": " + textArea.getText());
            }
        }
    }
}
