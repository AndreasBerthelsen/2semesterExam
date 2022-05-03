package dk.easv.gui.teacher.controller;

import dk.easv.gui.teacher.model.CitizenModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.bouncycastle.crypto.generators.SCrypt;

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
        int index = 0;
        for (int key : tilstandsList.keySet()) {
            //tab for hver afdeling
            Tab tab = new Tab(tilstandsList.get(key));

            VBox contentVBox = new VBox(20);
            contentVBox.prefHeight(Region.USE_COMPUTED_SIZE);
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

                ComboBox<ImageView> currentBox = createNiveauComboBox(); // måske replace med combobox factory
                ComboBox<String> targetBox = createTargetComboBox(); //Udføre selv | Udfører dele af aktiviteten | Udfører ikke selv aktiviteten | Ikke relevant
                gridPane.addRow(2, currentBox, targetBox);

                Label boxTitle = new Label("Borgerens Ønsker og mål");
                TextArea textArea = new TextArea();
                textArea.setWrapText(true);

                section.getChildren().addAll(headerBox, gridPane, boxTitle, textArea);
                contentVBox.getChildren().add(section);


                //TODO FIX SCROLLPANE SCROLL???
                ScrollPane scrollPane = new ScrollPane(contentVBox);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                scrollPane.prefHeight(11140);
                scrollPane.prefWidth(5000);
                tab.setContent(scrollPane);
            }
            funktionInnerTabPane.getTabs().add(tab);
        }

    }

    private ComboBox<String> createTargetComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().add("Udføre selv");
        comboBox.getItems().add("Udfører dele af aktiviteten");
        comboBox.getItems().add("Udfører ikke selv aktiviteten");
        comboBox.getItems().add("Ikke relevant");
        return comboBox;
    }

    private ComboBox<ImageView> createNiveauComboBox() {
        List<ImageView> imgViewList = new ArrayList<>();
        for(Image image : createImages()){
            imgViewList.add(new ImageView(image));
        }
        ComboBox<ImageView> comboBox = new ComboBox<>(
                FXCollections.observableArrayList(imgViewList)
        );
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


        for (Tab tab : funktionInnerTabPane.getTabs()) {
            GridPane gridPane = (GridPane) tab.getContent();
            for (Node n : gridPane.getChildren()) {
                if (n instanceof RadioButton radioButton) {
                    if (radioButton.isSelected()) {
                        System.out.println(radioButton.getId());
                    }
                }
            }
        }
    }
}
