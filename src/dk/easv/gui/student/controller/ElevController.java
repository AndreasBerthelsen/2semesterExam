package dk.easv.gui.student.controller;

import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.gui.teacher.Interfaces.ICitizenSelector;
import dk.easv.gui.teacher.Interfaces.IController;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ElevController implements IController, Initializable {
    public Label nameLabel;
    public TableView<Citizen> tableView;

    public TableColumn<Citizen,String> firstNameTC;
    public TableColumn<Citizen,String> lastNameTC;
    private User student;
    private CitizenModel cM;

    public ElevController() throws IOException {
        cM = new CitizenModel();
    }

    @Override
    public void setUserInfo(User user) {
        this.student = user;
        nameLabel.setText(student.getFirstname() + " " + student.getLastname());
        tableView.setItems(cM.getAllCitizenFromUserObservable(user));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstNameTC.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastNameTC.setCellValueFactory(new PropertyValueFactory<>("lastname"));
    }

    public void handleInspect(ActionEvent actionEvent) throws IOException {
        Citizen citizen = tableView.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/student/view/StudentEditCitizenView.fxml")));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        ICitizenSelector controller = loader.getController();
        controller.setCitizen(citizen);
        stage.showAndWait();
    }
}
