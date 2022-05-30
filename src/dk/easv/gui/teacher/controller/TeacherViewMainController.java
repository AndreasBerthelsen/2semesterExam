package dk.easv.gui.teacher.controller;

import dk.easv.be.User;
import dk.easv.gui.supercontroller.SuperController;
import dk.easv.gui.Interfaces.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class TeacherViewMainController extends SuperController implements IController {
    public BorderPane borderpane;
    public Label velkommenLabel;
    @FXML
    private Button exitBtn;
    private User teacher;

    /**
     * Denne metode åbner TeacherSkabelon view vinduet med en lærer
     * @param actionEvent
     * @throws IOException
     */
    public void handleSkabelonerbtn(ActionEvent actionEvent) throws IOException {
        setBorderpaneContent("/dk/easv/gui/teacher/view/TeacherSkabelonerView.fxml",teacher);
    }
    /**
     * Denne metode åbner CitizenTeacher view vinduet med en lærer
     * @param actionEvent
     * @throws IOException
     */
    public void handleBorgererbtn(ActionEvent actionEvent) throws IOException {
        setBorderpaneContent("/dk/easv/gui/teacher/view/CitizenTeacherView.fxml",teacher);
    }
    /**
     * Denne metode åbner AdminstrateStudents view vinduet med en lærer
     * @param actionEvent
     * @throws IOException
     */
    public void handleEleverbtn(ActionEvent actionEvent) throws IOException {
        setBorderpaneContent("/dk/easv/gui/teacher/view/AdminstrateStudents.fxml",teacher);
    }

    /**
     * Denne metode sætter alt content der er inde i de respektive panes.
     * @param fxml - FXML'en der bliver anvendt
     * @param user - Useren der bliver logget ind
     * @throws IOException
     */
    private void setBorderpaneContent(String fxml, User user) throws IOException {
        FXMLLoader root = new FXMLLoader(getClass().getResource(fxml));
        Pane pane = root.load();
        IController controller = root.getController();
        controller.setUserInfo(user);
        borderpane.setCenter(pane);
    }

    /**
     * Denne metode sætter læreren informationer
     * @param user
     */
    @Override
    public void setUserInfo(User user) {
        this.teacher = user;
        velkommenLabel.setText("Velkommen, " + user.getFirstname()+" "+user.getLastname());
    }

    /**
     * Den metode logger ud, og går tilbage til login vinduet
     * @param actionEvent
     * @throws IOException
     */
    public void handleSignOut(ActionEvent actionEvent) throws IOException {
        closeWindow(exitBtn);
        openScene("/dk/easv/gui/login/view/loginview.fxml",false, "Log ind som lærer eller elev", false);
    }
}
