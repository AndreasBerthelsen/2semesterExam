package dk.easv.gui.student.controller;

import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.gui.student.model.StudentModel;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.Interfaces.ICitizenSelector;
import dk.easv.gui.Interfaces.IController;
import dk.easv.gui.teacher.model.CitizenModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class StudentController extends SuperController implements IController, Initializable {
    public Label nameLabel;
    public TableView<Citizen> tableView;

    public TableColumn<Citizen,String> firstNameTC;
    public TableColumn<Citizen,String> lastNameTC;
    public Button exitBtn;
    public TableColumn<Citizen, Date> lastChangedCol;
    private User student;
    private CitizenModel cM;
    private StudentModel studentModel;

    public StudentController() throws IOException {
        cM = new CitizenModel();
        studentModel = new StudentModel();
    }

    /**
     * Sætter studenten info, og sætter alle de citiziens som tilhører studenten ved anvendelse af metoden
     * getAllCitizenFromUser().
     * @param user
     */
    @Override
    public void setUserInfo(User user) {
        this.student = user;
        nameLabel.setText(student.getFirstname() + " " + student.getLastname());
        tableView.setItems(cM.getAllCitizenFromUserObservable(user));
    }

    /**
     * Initistaliserer de forskellige informationer fra de citizens som studenten er tilknyttet til. Dette er
     * fornavnet, efternavnet og hvornår borgeeren sidst blev ændret.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstNameTC.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastNameTC.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        lastChangedCol.setCellValueFactory(new PropertyValueFactory<>("lastChanged"));
    }

    /**
     * Denne metode gør således, at når en student vælger en citizen, kommer de ind til scenen hvori at de kan redigere
     * en citizens informationer
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void handleInspect(ActionEvent actionEvent) throws IOException, SQLException {
        Citizen citizen = tableView.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/student/view/StudentEditCitizenView.fxml")));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        ICitizenSelector controller = loader.getController();

        controller.setCitizen(citizen);
        studentModel.lastChanged(citizen);
        stage.setMaximized(true);
        stage.showAndWait();
    }

    /**
     * Denne knap håndterer exit knappen i vores program, således at man kommer tilbage til loginvinduet hvis man vælger at logge
     * ud som en student
     * @param actionEvent
     * @throws IOException
     */
    public void handleExit(ActionEvent actionEvent) throws IOException {
        closeWindow(exitBtn);
        openScene("/dk/easv/gui/login/view/loginview.fxml", false, "log ind som lærer eller elev", false);
    }
}
