package dk.easv.bll.Util;

import dk.easv.be.FunkChunkAnswer;
import dk.easv.be.Section;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabFactory {
    public static Tab buildFunkTab(Section section, Map<Integer, FunkChunkAnswer> answerMap) {
        //tab for hver afdeling
        Tab tab = new Tab(section.getSectionTitle());
        VBox contentVBox = new VBox(100);
        contentVBox.setAlignment(Pos.TOP_CENTER);
        contentVBox.setPrefWidth(Region.USE_COMPUTED_SIZE);

        contentVBox.getStylesheets().add("dk/easv/CSS/Skabelon.css");
        contentVBox.setPadding(new Insets(80, 80, 80, 80));
        contentVBox.setId("VBOX");


        for (int key : section.getProblemidTitleMap().keySet()) {
            contentVBox.getChildren().add(buildFunkChunk(key, section, answerMap));
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contentVBox);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    private static VBox buildFunkChunk(int key, Section section, Map<Integer, FunkChunkAnswer> answerMap) {
        FunkChunkAnswer chunkAnswer = new FunkChunkAnswer(
                createNiveauComboBox(createImages()),
                createNiveauComboBox(createImages()),
                createUdførelseComboBox(),
                createBetydningComboBox(),
                createTextArea(),
                createTextArea());

        int hGap = 50;
        int vGap = 10;

        VBox chunk = new VBox();
        chunk.setAlignment(Pos.TOP_CENTER);

        GridPane gridpane = new GridPane();
        gridpane.setAlignment(Pos.CENTER);


        gridpane.addRow(0, buildFunkLeftBox(hGap, vGap, chunkAnswer), buildFunkRightBox(hGap, vGap, chunkAnswer));
        gridpane.addRow(1, new Label("Fagligt Notat"), new Label("Borgerens Ønsker og mål"));

        gridpane.addRow(2, chunkAnswer.getFagTextArea(), chunkAnswer.getCitizenTextArea());

        GridPane.setHalignment(gridpane, HPos.CENTER);
        GridPane.setValignment(gridpane, VPos.CENTER);
        gridpane.setHgap(hGap);
        gridpane.setVgap(vGap);

        Label headerLabel = new Label(section.getProblemidTitleMap().get(key));
        chunk.getChildren().addAll(headerLabel, gridpane);

        answerMap.put(key, chunkAnswer);
        return chunk;
    }

    private static GridPane buildFunkRightBox(int hGap, int vGap, FunkChunkAnswer chunkAnswer) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(hGap);
        gridPane.setVgap(vGap);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.addRow(1, new Label("Udførelse"), new Label("Betydning af udførelse"));
        gridPane.addRow(2, chunkAnswer.getUdførelseComboBox(), chunkAnswer.getBetydningComboBox());

        return gridPane;
    }

    private static GridPane buildFunkLeftBox(int hGap, int vGap, FunkChunkAnswer chunkAnswer) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(hGap);
        gridPane.setVgap(vGap);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.addRow(1, new Label("Nuværende Niveau"), new Label("Forventet Niveau"));
        gridPane.addRow(2, chunkAnswer.getCurrentComboBox(), chunkAnswer.getTargetComboBox());

        return gridPane;
    }

    private static ComboBox<String> createUdførelseComboBox() {
        return new ComboBox<>(FXCollections.observableArrayList(
                "Udføre selv",
                "Udfører dele af aktiviteten",
                "Udfører ikke selv aktiviteten",
                "Ikke relevant"
        ));

    }

    private static ComboBox<String> createBetydningComboBox() {
        return new ComboBox<>(FXCollections.observableArrayList(
                "Oplever ikke begrænsninger",
                "Oplever begrænsninger"
        ));
    }

    private static ComboBox<ImageView> createNiveauComboBox(List<Image> imageList) {
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

        return comboBox;
    }

    private static ArrayList<Image> createImages() {
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
/*
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

 */
        }


        toggleMap.put(key, toggleGroup);
        return radioList;
    }

    private static TextArea createTextArea() {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);

        return textArea;
    }

    private TextArea createTextArea(HashMap<Integer, TextArea> textAreaMap, int textAreaKey) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textAreaMap.put(textAreaKey, textArea);
        return textArea;
    }
}
