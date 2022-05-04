package dk.easv.gui.teacher.controller;

import dk.easv.gui.teacher.model.CitizenModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class NySkabelonMainController implements Initializable {
    public BorderPane borderpane;
    public javafx.scene.control.ScrollPane genScrollPane;
    public TabPane funktionInnerTabPane;
    public TabPane helbredsInnerTabPane;

    CitizenModel sM = new CitizenModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGeneralInfo();
        setupFunkTab();
        setupHelbredTab();
    }

    private void setupGeneralInfo() {
        ArrayList<String> fieldList = sM.getGeneralinfoFields();
        VBox vBox = new VBox();
        for (String field : fieldList) {
            Label label = new Label(field);
            TextArea textArea = new TextArea();
            textArea.setId(field);
            vBox.getChildren().add(label);
            vBox.getChildren().add(textArea);
        }
        genScrollPane.setContent(vBox);
    }

    private void setupFunkTab() {
        HashMap<Integer, String> tilstandsList = sM.getFunktionsTilstande();
        HashMap<Integer, ArrayList<String>> problemMap = sM.getFunktionsVandskligheder();
        List<Image> imageList = createImages();
        for (int key : tilstandsList.keySet()) {
            //tab for hver afdeling
            Tab tab = new Tab(tilstandsList.get(key));

            VBox contentVBox = new VBox(80);
            contentVBox.setPrefWidth(800);

            for (String string : problemMap.get(key)) {
                //underpunkter
                VBox section = new VBox();
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
                TextArea textArea = new TextArea();
                textArea.setId(string+"TextArea");
                textArea.setWrapText(true);

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
        //TODO STOP MED A LOADE ALT FOR MANGE BILLDER APP TAGER 3 SEC OM AT ÅBNE
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

    private void setupHelbredTab() {
        //gen tabs -> gridpane -> header + radiobuttons -> textArea (Radio buttons event handler til inactive textArea)
        HashMap<Integer, String> tilstandsMap = sM.getHelbredsTilstande();
        HashMap<Integer, ArrayList<String>> vansklighedsMap = sM.getHelbredVanskligheder();


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


    public void handleGembtn(ActionEvent actionEvent) {
        /*   TJEKKER ALLE TEXT AREAS I GEN INFO
        VBox vBox = (VBox) genScrollPane.getContent();
        for (Node n : vBox.getChildren()) {
            if (n instanceof TextArea) {
                TextArea ta = (TextArea) n;
                System.out.println(n.getId() + ": " + ta.getText());
            }
        }
         */

        // Giga forloop for at tjekke alle inputs i funktioninnerpane
        for (Tab tab : funktionInnerTabPane.getTabs()) {
            ScrollPane scrollPane = (ScrollPane) tab.getContent();
            VBox contentVBox = (VBox) scrollPane.getContent();
            List<Node> list = contentVBox.getChildren();
            for (Node n : list) {
                if (n instanceof VBox section) {
                    for (Node sN : section.getChildren()) {
                        //3 cases nu combobox, ønsket combobox og textarea
                        if (sN instanceof GridPane gridPane){
                            for(Node gN : gridPane.getChildren()){
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
                        }else if (sN instanceof TextArea textArea){
                            String string = textArea.getText();
                            if(!string.isBlank()){
                                System.out.println(textArea.getId() + string);
                            }
                        }
                    }
                }
            }
        }
    }
}
