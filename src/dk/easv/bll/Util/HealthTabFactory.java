package dk.easv.bll.Util;

import dk.easv.be.HealthNodeContainer;
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

import java.util.*;

public class HealthTabFactory {

    public static Tab buildHealthTab(Section section, Map<Integer, HealthNodeContainer> answerMap) {
        Tab tab = new Tab(section.getSectionTitle());
        VBox contentBox = new VBox(100);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefWidth(Region.USE_COMPUTED_SIZE);

        contentBox.getStylesheets().add("dk/easv/CSS/Skabelon.css");
        contentBox.setPadding(new Insets(80, 80, 80, 80));
        contentBox.setId("VBOX");

        for (int key : section.getProblemidTitleMap().keySet()) {
            contentBox.getChildren().add(buildHealthChunk(key, section, answerMap));
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contentBox);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    private static VBox buildHealthChunk(int key, Section section, Map<Integer, HealthNodeContainer> answerMap) {
        int buttonSpacing = 20;
        HealthNodeContainer chunkAnswer = new HealthNodeContainer(
                createTextArea(),
                createTextArea(),
                createExpectedComboBox(),
                createTextArea()
        );

        VBox chunk = new VBox(20);
        chunk.setAlignment(Pos.TOP_CENTER);
        Label headerLabel = new Label(section.getProblemidTitleMap().get(key));
        HBox buttonHBox = new HBox(buttonSpacing);
        buttonHBox.setAlignment(Pos.TOP_CENTER);
        buttonHBox.getChildren().addAll(createRadioButtons(key,answerMap));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(40);
        gridPane.setVgap(40);
        gridPane.addRow(0,createTechnicalVBox(chunkAnswer.getTechnicalTextArea()),createCurrentVBox(chunkAnswer.getCurrentTextarea()));
        gridPane.addRow(1,createExpectedVBox(chunkAnswer),createObservationVBox(chunkAnswer.getObservationTextArea()));

        chunk.getChildren().addAll(headerLabel,buttonHBox,gridPane);
        answerMap.put(key,chunkAnswer);
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
        obsVBox.getChildren().addAll(new Label("Observations notat"),observationTextArea);
        return obsVBox;
    }

    private static VBox createCurrentVBox(TextArea currentTextarea) {
        VBox currentBox = new VBox();
        currentBox.setAlignment(Pos.TOP_CENTER);
        currentBox.getChildren().addAll(new Label("Nuværende vurdering"),currentTextarea);
        return currentBox;
    }

    private static VBox createTechnicalVBox(TextArea technicalTextArea) {
        VBox technicalBox = new VBox();
        technicalBox.setAlignment(Pos.TOP_CENTER);
        technicalBox.getChildren().addAll(new Label("Faglig notat"),technicalTextArea);
        return technicalBox;
    }


    private static List<RadioButton> createRadioButtons(int sectionId,Map<Integer, HealthNodeContainer> answerMap) {
        ArrayList<RadioButton> radioList = new ArrayList<>();
        ToggleGroup toggleGroup = new ToggleGroup();
        Map<Integer,String> nameMap = new LinkedHashMap<>();
        nameMap.put(1,"Aktuel");
        nameMap.put(2,"Potentiel");
        nameMap.put(3,"Ikke relevant");

        toggleGroup.getSelectedToggle();
        for(int key : nameMap.keySet()){
            RadioButton radioButton = new RadioButton(nameMap.get(key));
            radioButton.setToggleGroup(toggleGroup);
            if (key == 3){
                radioButton.setOnAction(event -> {
                    answerMap.get(sectionId).disableAllNodes();
                    answerMap.get(sectionId).setSelectedToggleId(key);
                    System.out.println(answerMap.get(sectionId).getSelectedToggleId());
                });
            }else
            {
                radioButton.setOnAction(event -> {
                    answerMap.get(sectionId).setSelectedToggleId(key);
                    answerMap.get(sectionId).enableAllNodes();
                    System.out.println(answerMap.get(sectionId).getSelectedToggleId());
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
        ComboBox<String> comboBox =  new ComboBox<>(FXCollections.observableArrayList(
                //todo træk fra db?
                "Mindskes",
                "Forbliver uændret",
                "forsvinder"
        ));
        comboBox.setPrefWidth(prefWidth);
        return comboBox;
    }
}

