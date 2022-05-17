package dk.easv.gui.teacher.controller;

import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.gui.teacher.Interfaces.ICitizenSelector;
import dk.easv.gui.teacher.Interfaces.IController;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class SkabelonerViewController implements Initializable, IController {
    public TableView<Citizen> templateTV;
    public TableColumn<Citizen, String> lNameTC;
    public TableColumn<Citizen, String> fNameTC;
    @FXML
    private Label descriptionLbl;
    @FXML
    private TextArea descriptionTextArea;
    private CitizenModel cM;

    public SkabelonerViewController() throws IOException {
        cM = new CitizenModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTemplateTV();
    }

    private void setTemplateTV() {
        fNameTC.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lNameTC.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        try {
            templateTV.setItems(cM.getObservableTemplates());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleNySkabelonbtn(ActionEvent actionEvent) throws IOException, SQLException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/view/NySkabelonMain.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Ny Skabelon");
        stage.setResizable(true);
        stage.setScene(scene);
        //stage.setFullScreen(true); Giver en F11 fullscreen
        stage.showAndWait();
        templateTV.setItems(cM.getObservableTemplates());
    }

    public void handleDeletebtn(ActionEvent actionEvent) throws SQLException {
        //TODO TEST MIG
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Citizen citizen = templateTV.getSelectionModel().getSelectedItem();
            try {
                cM.deleteTemplate(citizen.getId());
                templateTV.getItems().remove(citizen);
            } catch (Exception e) {
                //error here
                e.printStackTrace();
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }







    }

    public void handleEditSkabelonbtn(ActionEvent actionEvent) throws IOException, SQLException {
        Citizen citizen = templateTV.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/view/TeacherEditSkabelonView.fxml")));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        ICitizenSelector controller = loader.getController();
        controller.setCitizen(citizen);
        stage.showAndWait();
        templateTV.setItems(cM.getObservableTemplates());
    }

    public void handleKopierSkabelon(ActionEvent actionEvent) {
    }

    public void setDescription(Citizen citizen) throws SQLException {
        descriptionTextArea.setText(citizen.getDescription());
        descriptionTextArea.setWrapText(true);
    }

    public void handleSetDescription(MouseEvent mouseEvent) throws SQLException {
        setDescription(templateTV.getSelectionModel().getSelectedItem());
    }

    @Override
    public void setUserInfo(User user) {

    }
}
