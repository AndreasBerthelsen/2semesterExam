package dk.easv.gui.teacher.controller;

import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

    private void setupHelbredTab(){
        //gen tabs -> gridpane -> header + radiobuttons -> textArea (Radio buttons event handler til inactive textArea)
        HashMap<Integer, String> tilstandsMap = sM.getHelbredsTilstande();
        HashMap<Integer, ArrayList<String>> vansklighedsMap= sM.getHelbredVanskligheder();



    }

    private void setupFunkTab() {
        HashMap<Integer, String> tilstandsList = sM.getFunktionsTilstande();
        HashMap<Integer, ArrayList<String>> problemMap = sM.getFunktionsVandskligheder();
        List<Image> imgList = new ArrayList<>();
        //5 ikke en god ide, ændre til funktion mængder??
        for (int i = 0; i <= 5; i++) {
            Image image = new Image("/dk/easv/Images/funktion" + i + ".png");
            imgList.add(image);
        }

        int index = 0;
        for (int key : tilstandsList.keySet()) {
            //tab for hver afdeling
            List<ImageView> imageList = createImageViews(imgList);
            Tab tab = new Tab(tilstandsList.get(key));
            GridPane gridPane = new GridPane();


            for (String string : problemMap.get(key)) {
                //underpunkter
            }
            funktionInnerTabPane.getTabs().add(tab);
        }

    }

    private ArrayList<ImageView> createImageViews(List<Image> imageList) {
        ArrayList<ImageView> list = new ArrayList<>();
        for (Image i : imageList) {
            ImageView imageView = new ImageView(i);
            list.add(imageView);
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
