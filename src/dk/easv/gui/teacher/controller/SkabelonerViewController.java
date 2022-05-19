package dk.easv.gui.teacher.controller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.teacher.Interfaces.ICitizenSelector;
import dk.easv.gui.teacher.Interfaces.IController;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class SkabelonerViewController extends SuperController implements Initializable, IController {
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
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/view/NySkabelonMain.fxml")));
        loader.setController(new NySkabelonMainController());
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Ny Skabelon");
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.showAndWait();

        templateTV.setItems(cM.getObservableTemplates());
    }

    public void handleDeletebtn(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
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

    public void handleEditSkabelonbtn(ActionEvent actionEvent) {
        try{
            Citizen citizen = templateTV.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/teacher/view/NySkabelonMain.fxml")));
            loader.setController(new EditSkabelonViewController());

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            ICitizenSelector controller = loader.getController();
            controller.setCitizen(citizen);
            stage.setMaximized(true);
            stage.showAndWait();
            templateTV.setItems(cM.getObservableTemplates());
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("vælg skabelon error");
        }
    }

    public void handleKopierSkabelon(ActionEvent actionEvent) throws SQLException, IOException {
    Citizen selectedCitizen = templateTV.getSelectionModel().getSelectedItem();
    openNewSceneWithCitizen(selectedCitizen, "/dk/easv/gui/teacher/view/copyChooseName.fxml", "Vælg navn til din nye Skabelon");
    templateTV.setItems(cM.getObservableTemplates());
    }

    public void setDescription(Citizen citizen) throws SQLException {
        descriptionTextArea.setText(citizen.getDescription());
        descriptionTextArea.setWrapText(true);
    }

    public void handleSetDescription(MouseEvent mouseEvent) throws SQLException {
        try {
            setDescription(templateTV.getSelectionModel().getSelectedItem());
        }catch (Exception ignored){

        }

    }

    @Override
    public void setUserInfo(User user) {

    }
}
