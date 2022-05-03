package dk.easv.gui.teacher.controller;

import dk.easv.gui.teacher.model.SkabelonModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    public ScrollPane funkScrollPane;

    SkabelonModel sM = new SkabelonModel();

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
        System.out.println("Tilstands ist ----------------------------------");
        System.out.println(tilstandsList);
        HashMap<Integer, ArrayList<String>> problemMap = sM.getFunktionsVandskligheder();
        System.out.println("problem list --------------------");
        for (ArrayList<String> a : problemMap.values()) {
            System.out.println(a.toString());
        }

        //GRIDPANE??
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);

        Label label = new Label("test");
        gridPane.addRow(1,label,new RadioButton(),new RadioButton(),new RadioButton(),new RadioButton(),new RadioButton());

        Label label2 = new Label("test2222222222222");
        gridPane.addRow(2,label2,new RadioButton(),new RadioButton(),new RadioButton(),new RadioButton(),new RadioButton());

/*
        //skab labels
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        for (int key : tilstandsList.keySet()) {
            Label headerLabel = new Label(tilstandsList.get(key));
            vBox.getChildren().add(headerLabel);
            for (String string : problemMap.get(key)) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                Label label = new Label(string);
                hBox.getChildren().add(label);


                HBox radioBox = new HBox(10);

                radioBox.prefWidth(5000);
                radioBox.setAlignment(Pos.CENTER_RIGHT);
                for(RadioButton r : createRadiobuttons(string,5)){
                    radioBox.getChildren().add(r);
                }
                hBox.getChildren().add(radioBox);
                vBox.getChildren().add(hBox);

            }
        }
*/
        funkScrollPane.setContent(gridPane);

    }

    private List<RadioButton> createRadiobuttons(String toggleGroupName, int amount) {
        ToggleGroup toggleGroup = new ToggleGroup();
        List<RadioButton> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            RadioButton radioButton = new RadioButton();
            radioButton.setToggleGroup(toggleGroup);
            list.add(radioButton);
        }
        return list;
    }


    public void handleGembtn(ActionEvent actionEvent) {
        VBox vBox = (VBox) genScrollPane.getContent();
        for (Node n : vBox.getChildren()) {
            if (n instanceof TextArea) {
                TextArea ta = (TextArea) n;
                System.out.println(n.getId() + ": " + ta.getText());
            }
        }
    }
}
