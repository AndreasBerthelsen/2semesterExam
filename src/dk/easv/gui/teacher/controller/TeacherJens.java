package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Category;
import dk.easv.gui.teacher.model.HealthReportModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TeacherJens implements Initializable {

    @FXML
    private TabPane tabPane;
    private HealthReportModel hpModel;

    public TeacherJens(){
        hpModel = new HealthReportModel();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            generateTabPane();
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
    }


    public void generateTabPane() throws SQLServerException {
        tabPane = new TabPane();

        List<Category> categoryList = hpModel.getAllTitles();
        System.out.println(categoryList);

        Tab tab1 = new Tab("Planes", new Label("Show all planes available"));
        Tab tab2 = new Tab("Cars"  , new Label("Show all cars available"));
        Tab tab3 = new Tab("Boats" , new Label("Show all boats available"));

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);

        VBox vBox = new VBox(tabPane);
        Scene scene = new Scene(vBox);
    }
}
