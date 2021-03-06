package dk.easv.bll.Util;

import dk.easv.be.HealthNodeContainer;
import dk.easv.be.HealthResult;
import dk.easv.be.Section;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HealthTabFactory {
    private static int spacing = 100;
    private static Insets padding = new Insets(80, 80, 80, 80);

    /**
     * skaber en tom tab for ud fra info i den givne section
     * @param section det objekt der indeholder infoen om alle overskrifter
     * @param answerMap map hvori container objekter gemmes
     * @return en tab
     */
    public static Tab buildHealthTab(Section section, Map<Integer, HealthNodeContainer> answerMap) {
        Tab tab = new Tab(section.getSectionTitle());
        VBox contentBox = new VBox(spacing);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefWidth(Region.USE_COMPUTED_SIZE);

        contentBox.getStylesheets().add("dk/easv/gui/CSS/Skabelon.css");
        contentBox.setPadding(padding);
        contentBox.setId("VBOX");

        for (int key : section.getProblemidTitleMap().keySet()) {
            contentBox.getChildren().add(buildHealthChunk(key, section, answerMap, new HealthNodeContainer(createTextArea(), createTextArea(), createExpectedComboBox(), createTextArea())));
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contentBox);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    /**
     * samler en vbox med alle nødvendige inputs ud fra en overskrift
     * @param key det problemId som chunken tilhøre
     * @param section den section som chunken er en del af
     * @param nodeContainerMap mappet hvori container objektet for chunken gemmes
     * @param startingContainer containeren der indeholder alle nodes til chunken
     * @return vbox
     */
    private static VBox buildHealthChunk(int key, Section section, Map<Integer, HealthNodeContainer> nodeContainerMap, HealthNodeContainer startingContainer) {
        int buttonSpacing = 20;
        int gridPaneHGap = 40;
        int gridPaneVGap = 40;
        int chunkSpacing = 20;

        VBox chunk = new VBox(chunkSpacing);
        chunk.setAlignment(Pos.TOP_CENTER);
        Label headerLabel = new Label(section.getProblemidTitleMap().get(key));
        headerLabel.setId("healthHeader");
        HBox buttonHBox = new HBox(buttonSpacing);
        buttonHBox.setAlignment(Pos.TOP_CENTER);
        buttonHBox.getChildren().addAll(createRadioButtons(key, nodeContainerMap, startingContainer));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(gridPaneHGap);
        gridPane.setVgap(gridPaneVGap);
        gridPane.addRow(0, createTechnicalVBox(startingContainer.getTechnicalTextArea()), createCurrentVBox(startingContainer.getCurrentTextarea()));
        gridPane.addRow(1, createExpectedVBox(startingContainer), createObservationVBox(startingContainer.getObservationTextArea()));

        chunk.getChildren().addAll(headerLabel, buttonHBox, gridPane);
        nodeContainerMap.put(key, startingContainer);
        return chunk;
    }

    private static VBox createExpectedVBox(HealthNodeContainer chunkAnswer) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().addAll(new Label("Forventet niveau"), chunkAnswer.getExpectedComboBox());
        return vBox;
    }

    private static Node createObservationVBox(TextArea observationTextArea) {
        VBox obsVBox = new VBox();
        obsVBox.setAlignment(Pos.TOP_CENTER);
        obsVBox.getChildren().addAll(new Label("Observations notat"), observationTextArea);
        return obsVBox;
    }

    private static VBox createCurrentVBox(TextArea currentTextarea) {
        VBox currentBox = new VBox();
        currentBox.setAlignment(Pos.TOP_CENTER);
        currentBox.getChildren().addAll(new Label("Nuværende vurdering"), currentTextarea);
        return currentBox;
    }

    private static VBox createTechnicalVBox(TextArea technicalTextArea) {
        VBox technicalBox = new VBox();
        technicalBox.setAlignment(Pos.TOP_CENTER);
        technicalBox.getChildren().addAll(new Label("Faglig notat"), technicalTextArea);
        return technicalBox;
    }

    private static List<RadioButton> createRadioButtons(int sectionId, Map<Integer, HealthNodeContainer> nodeMap,HealthNodeContainer container) {
        ArrayList<RadioButton> radioList = new ArrayList<>();
        ToggleGroup toggleGroup = new ToggleGroup();
        Map<Integer, String> nameMap = new LinkedHashMap<>();
        nameMap.put(1, "Aktuel");
        nameMap.put(2, "Potentiel");
        nameMap.put(3, "Ikke relevant");

        for (int key : nameMap.keySet()) {
            RadioButton radioButton = new RadioButton(nameMap.get(key));
            radioButton.setToggleGroup(toggleGroup);

            if(key == container.getSelectedToggleId()){
                radioButton.fire();
            }

            if (key == 3) {
                radioButton.setOnAction(event -> {
                    nodeMap.get(sectionId).disableAllNodes();
                    nodeMap.get(sectionId).setSelectedToggleId(key);
                });
            } else {
                radioButton.setOnAction(event -> {
                    nodeMap.get(sectionId).setSelectedToggleId(key);
                    nodeMap.get(sectionId).enableAllNodes();
                });
            }
            radioList.add(radioButton);
        }
        return radioList;
    }

    private static TextArea createTextArea() {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        return textArea;
    }

    private static ComboBox<String> createExpectedComboBox() {
        int prefWidth = 200;
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(
                "Mindskes",
                "Forbliver uændret",
                "forsvinder"
        ));
        comboBox.setPrefWidth(prefWidth);
        return comboBox;
    }

    /**
     * skaber en tab med information i sig
     * @param section det objekt der indeholder infoen om alle overskrifter
     * @param healthNodeMap map til at gemme container objekter i
     * @param healthInfo map med result objekter der har
     *                  den info der skal indsættes i chunken
     * @param isDisabled true hvis tabben ikke må redigeres
     * @return en tab
     */
    public static Tab buildTabWithInfo(Section section, Map<Integer, HealthNodeContainer> healthNodeMap, Map<Integer, HealthResult> healthInfo,boolean isDisabled) {
        Tab tab = new Tab(section.getSectionTitle());
        VBox contentBox = new VBox(spacing);
        contentBox.setDisable(isDisabled);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefWidth(Region.USE_COMPUTED_SIZE);

        contentBox.getStylesheets().add("/dk/easv/CSS/Skabelon.css");
        contentBox.setPadding(padding);
        contentBox.setId("VBOX");

        for (int key : section.getProblemidTitleMap().keySet()) {
            HealthNodeContainer startingContainer = new HealthNodeContainer(
                    createTextArea(), createTextArea(), createExpectedComboBox(), createTextArea()
            );
            HealthResult info = healthInfo.get(key);
            startingContainer.setCurrentString(info.getCurrent());
            startingContainer.setObservationString(info.getObservation());
            startingContainer.setTechnicalString(info.getTechnical());
            startingContainer.setExpectedIndex(info.getExpectedIndex());
            startingContainer.setSelectedToggleId(info.getToggleId());

            if (startingContainer.getSelectedToggleId() == 3){
                startingContainer.disableAllNodes();
            }

            contentBox.getChildren().add(buildHealthChunk(key, section, healthNodeMap, startingContainer));
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contentBox);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

}

