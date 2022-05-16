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
import java.util.List;
import java.util.Map;

public class FunktionTabFactory {
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
        int hGap = 50;
        int vGap = 10;
        FunkChunkAnswer chunkAnswer = new FunkChunkAnswer(
                createNiveauComboBox(createImages()),
                createNiveauComboBox(createImages()),
                createUdførelseComboBox(),
                createBetydningComboBox(),
                createTextArea(),
                createTextArea(),
                createTextArea());

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
        Label obsLabel = new Label("Observations Notat");
        TextArea obsTextArea = chunkAnswer.getObsTextArea();
        chunk.getChildren().addAll(headerLabel, gridpane, obsLabel, obsTextArea);

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
                //todo træk list fra db
                "Udføre selv",
                "Udfører dele af aktiviteten",
                "Udfører ikke selv aktiviteten",
                "Ikke relevant"
        ));

    }

    private static ComboBox<String> createBetydningComboBox() {
        return new ComboBox<>(FXCollections.observableArrayList(
                //todo træk list fra db
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

    private static TextArea createTextArea() {
        int width = 600;
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setMaxWidth(width);

        return textArea;
    }
}