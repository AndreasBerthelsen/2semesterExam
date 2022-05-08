package dk.easv.gui.teacher.controller;

import dk.easv.be.Citizen;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class SkabelonerViewController implements Initializable {
    public TableView<Citizen> templateTV;
    public TableColumn<Citizen, String> nameTC;
    private CitizenModel cM;

    public SkabelonerViewController() throws IOException {
        cM = new CitizenModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTemplateTV();
    }

    private void setTemplateTV() {
        nameTC.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        try{
            templateTV.setItems(cM.getObservableTemplates());
        }catch (Exception e){
            //handle e
        }


    }

    public void handleNySkabelonbtn(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/view/NySkabelonMain.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Ny Skabelon");
        stage.setResizable(true);
        stage.setScene(scene);
        //stage.setFullScreen(true); Giver en F11 fullscreen
        stage.showAndWait();
    }

    public void handleDeletebtn(ActionEvent actionEvent) throws SQLException {
        //TODO TEST MIG
        Citizen citizen = templateTV.getSelectionModel().getSelectedItem();
        try {
            cM.deleteTemplate(citizen);
        } catch (Exception e) {
            //error here
            System.out.println("catch");
        }


    }


}
