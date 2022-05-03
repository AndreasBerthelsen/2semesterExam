package dk.easv.gui.teacher.controller;

import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
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

    CitizenModel sM = new CitizenModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGeneralInfo(sM.getGeneralinfoFields());

        setupFunkTab();
    }

    private void setupGeneralInfo(ArrayList<String> fieldList) {
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
        List<Image> imgList = new ArrayList<>();
        //5 ikke en god ide, ændre til funktion mængder??
        for (int i = 0; i <= 5; i++) {
            Image image = new Image("/dk/easv/Images/funktion" + i + ".png");
            imgList.add(image);
        }
        int index = 0;
        for (int key : tilstandsList.keySet()) {
            List<ImageView> imageList = createImageViews(imgList);


            Tab tab = new Tab(tilstandsList.get(key));
            GridPane gridPane = new GridPane();
            tab.setContent(gridPane);

            Label headerLabel = new Label(tilstandsList.get(key));
            gridPane.addRow(index, headerLabel, imageList.get(0), imageList.get(1), imageList.get(2), imageList.get(3), imageList.get(4), imageList.get(5));
            index++;
            //under titler
            for (String string : problemMap.get(key)) {
                Label subLabel = new Label(string);
                List<RadioButton> bL = createRadiobuttons(string, 6);
                gridPane.addRow(index, subLabel, bL.get(0), bL.get(1), bL.get(2), bL.get(3), bL.get(4), bL.get(5));
                index++;
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


    private List<RadioButton> createRadiobuttons(String groupName, int amount) {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.setUserData(groupName);
        List<RadioButton> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            RadioButton radioButton = new RadioButton();
            radioButton.setId(groupName + i);
            radioButton.setToggleGroup(toggleGroup);
            list.add(radioButton);
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
