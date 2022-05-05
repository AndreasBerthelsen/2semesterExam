package dk.easv.gui.student.controller;

import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.lang.model.element.NestingKind;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RadioButtonViewController implements Initializable {
    @FXML
    public BorderPane borderPaneInTabPane;
    @FXML
    public TabPane innerTabPane;

    CitizenModel citizenModel = new CitizenModel();


    public RadioButtonViewController() throws IOException {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    loadInformation();
    }

    public void loadInformation(){
        HashMap<Integer, String> tilstandsMap = citizenModel.getFunktionsTilstande();
        HashMap<Integer, ArrayList<String>> problemMap = citizenModel.getFunktionsVandskligheder();

        for (int key : tilstandsMap.keySet()) {
            Tab tab = new Tab(tilstandsMap.get(key));
            tab.setClosable(false);
            int index = 0;
            GridPane gridPane = new GridPane();


            List<ImageView> imageList = new ArrayList<>();
            for (int i = 0; i <= 5; i++){
                Image image = new Image("/dk/easv/Images/funktion"+i+".png");
                ImageView imageView = new ImageView(image);
                imageList.add(imageView);
            }

            gridPane.addRow(index++, imageList.get(0), imageList.get(1), imageList.get(2), imageList.get(3), imageList.get(4), imageList.get(5));

            for (String s : problemMap.get(key)){
                gridPane.addRow(index, new Label(s), new RadioButton("0"), new RadioButton("1"), new RadioButton("2"), new RadioButton("3"),new RadioButton("4"),new RadioButton("9"));

                index++;

            }
            tab.setContent(gridPane);
            innerTabPane.getTabs().add(tab);


        }



    }


    public void handleSaveButton(ActionEvent actionEvent) {


    }

    public void handleCloseButton(ActionEvent actionEvent) {
        System.out.println("Program closed!");
        System.exit(0);

    }
}
