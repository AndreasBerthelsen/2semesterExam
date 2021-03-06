package dk.easv.bll.Util;

import dk.easv.be.FunkNodeContainer;
import dk.easv.be.FunkResult;
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

public class FuncTabFactory {
    private static List<Image> imgList = createImages();
    private static int spacing = 100;
    private static Insets padding = new Insets(80, 80, 80, 80);

    /**
     * skaber en tom tab for ud fra info i den givne section
     *
     * @param section   Det objekt der indeholder infoen om alle overskrifter
     * @param answerMap map hvori container objekter gemmes
     * @return en tab
     */
    public static Tab buildFunkTab(Section section, Map<Integer, FunkNodeContainer> answerMap) {
        //tab for hver afdeling
        Tab tab = new Tab(section.getSectionTitle());
        VBox contentVBox = new VBox(spacing);
        contentVBox.setAlignment(Pos.TOP_CENTER);
        contentVBox.setPrefWidth(Region.USE_COMPUTED_SIZE);

        contentVBox.getStylesheets().add("/dk/easv/gui/CSS/Skabelon.css");
        contentVBox.setPadding(padding);
        contentVBox.setId("VBOX");

        for (int problemId : section.getProblemidTitleMap().keySet()) {
            FunkNodeContainer container = new FunkNodeContainer(
                    createNiveauComboBox(imgList),
                    createNiveauComboBox(imgList),
                    createUdførelseComboBox(),
                    createBetydningComboBox(),
                    createTextArea(),
                    createTextArea(),
                    createTextArea());
            contentVBox.getChildren().add(buildFunkChunk(problemId, section, answerMap, container));
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contentVBox);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        return tab;
    }

    /**
     * samler en vbox med alle nødvendige inputs ud fra en overskrift
     *
     * @param problemId         idet af den problemstilling som chunken tilhøre
     * @param section           den section som chunken tilhøre
     * @param answerMap         mappet hvor container objektet bliver gemt
     * @param startingContainer den container der tages udgangspunkt i
     * @return en vbox
     */
    private static VBox buildFunkChunk(int problemId, Section section, Map<Integer, FunkNodeContainer> answerMap, FunkNodeContainer startingContainer) {
        int hGap = 50;
        int vGap = 10;

        VBox chunk = new VBox();
        chunk.setAlignment(Pos.TOP_CENTER);
        chunk.setId("chunkVbox");

        GridPane gridpane = new GridPane();
        gridpane.setAlignment(Pos.CENTER);

        gridpane.addRow(0, buildFunkLeftBox(hGap, vGap, startingContainer), buildFunkRightBox(hGap, vGap, startingContainer));
        gridpane.addRow(1, new Label("Fagligt Notat"), new Label("Borgerens Ønsker og mål"));
        gridpane.addRow(2, startingContainer.getFagTextArea(), startingContainer.getCitizenTextArea());

        GridPane.setHalignment(gridpane, HPos.CENTER);
        GridPane.setValignment(gridpane, VPos.CENTER);
        gridpane.setHgap(hGap);
        gridpane.setVgap(vGap);

        Label headerLabel = new Label(section.getProblemidTitleMap().get(problemId));
        headerLabel.setId("funkHeader");
        Label obsLabel = new Label("Observations Notat");
        TextArea obsTextArea = startingContainer.getObsTextArea();
        chunk.getChildren().addAll(headerLabel, gridpane, obsLabel, obsTextArea);

        answerMap.put(problemId, startingContainer);
        return chunk;
    }

    /**
     * bygger den højre del af en chunk
     *
     * @param hGap        horizontal spacing
     * @param vGap        vertical spacing
     * @param chunkAnswer container objektet som har input felterne
     * @return et gridPane
     */
    private static GridPane buildFunkRightBox(int hGap, int vGap, FunkNodeContainer chunkAnswer) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(hGap);
        gridPane.setVgap(vGap);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.addRow(1, new Label("Udførelse"), new Label("Betydning af udførelse"));
        gridPane.addRow(2, chunkAnswer.getUdførelseComboBox(), chunkAnswer.getBetydningComboBox());
        return gridPane;
    }

    /**
     * bygger den venstre del af en chunk
     *
     * @param hGap        horizontal spacing
     * @param vGap        vertical spacing
     * @param chunkAnswer container objektet som har input felterne
     * @return et gridPane
     */
    private static GridPane buildFunkLeftBox(int hGap, int vGap, FunkNodeContainer chunkAnswer) {
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

    private static TextArea createTextArea() {
        int width = 600;
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setMaxWidth(width);
        return textArea;
    }

    /**
     * skaber en tab med information i sig
     * @param section den section tabben tilhøre
     * @param funkNodeMap map hvori container objekter gemmes
     * @param funkInfo map der indeholder et funkResult objekt til at udfylde hver container objekt
     * @param isDisabled true hvis det ikke skal være muligt at redigere
     * @return en tab
     */
    public static Tab buildFunkTabWithInfo(Section section, Map<Integer, FunkNodeContainer> funkNodeMap, Map<Integer, FunkResult> funkInfo, boolean isDisabled) {
        Tab tab = new Tab(section.getSectionTitle());
        VBox contentVBox = new VBox(spacing);
        contentVBox.setDisable(isDisabled);
        contentVBox.setAlignment(Pos.TOP_CENTER);
        contentVBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        contentVBox.getStylesheets().add("/dk/easv/CSS/Skabelon.css");
        contentVBox.setPadding(padding);
        contentVBox.setId("VBOX");
        for (int key : section.getProblemidTitleMap().keySet()) {
            FunkNodeContainer startingContainer = new FunkNodeContainer(
                    createNiveauComboBox(imgList),
                    createNiveauComboBox(imgList),
                    createUdførelseComboBox(),
                    createBetydningComboBox(),
                    createTextArea(),
                    createTextArea(),
                    createTextArea());
            FunkResult info = funkInfo.get(key);
            startingContainer.setCitizenString(info.getCitizenString());
            startingContainer.setTechnicalString(info.getTechnical());
            startingContainer.setObsString(info.getObservation());
            startingContainer.setCurrentIndex(info.getCurrent());
            startingContainer.setExecutionIndex(info.getCurrent());
            startingContainer.setImportanceIndex(info.getImportance());
            startingContainer.setTargetIndex(info.getTarget());
            contentVBox.getChildren().addAll(buildFunkChunk(key, section, funkNodeMap, startingContainer));
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(contentVBox);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);

        return tab;
    }
}
