package dk.easv.gui.teacher.controller;

import dk.easv.gui.teacher.model.SkabelonModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NySkabelonMainController implements Initializable {
    public BorderPane borderpane;
    public javafx.scene.control.ScrollPane genScrollPane;
    public ScrollPane funkScrollPane;

    SkabelonModel sM = new SkabelonModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGeneralInfo(sM.getGeneralinfoFields());
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
