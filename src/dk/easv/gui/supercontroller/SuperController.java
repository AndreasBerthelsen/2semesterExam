package dk.easv.gui.supercontroller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.gui.teacher.Interfaces.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public abstract class SuperController {

    /**
     * gets the firstname connected to the Textfield
     * @param firstNameField The firstNameField associated with the user
     * @return
     */
    public String getFirstName(TextField firstNameField) {
        if (!firstNameField.getText().isEmpty()) {
            return firstNameField.getText().trim();
        }
        else{
            errorMessage("Indtast et gyldigt fornavn");
        }
        return null;
    }

    /**
     * gets the firstname connected to the Textfield
     * @param lastNameField The lastNameField associated with the user
     * @return
     */
    public String getLastName(TextField lastNameField) {
        if (!lastNameField.getText().isEmpty()) {
            return lastNameField.getText().trim();
        }
        else{
            errorMessage("Indtast et gyldigt efternavn");
        }
        return null;
    }


    /**
     * gets the username connected to the Textfield
     * @param usernameField the username-field associated with the user
     * @return
     */

    public String getUsername(TextField usernameField) {
        if (!usernameField.getText().isEmpty()) {
            return usernameField.getText();
        }
        else{
            errorMessage("Indtast et gyldigt brugernavn");
        }
        return null;
    }

    /**
     * gets the password connected to the TextField
     * @param passwordField
     * @return
     */

    public String getPassword(TextField passwordField) {
        if (!passwordField.getText().isEmpty()) {
            return passwordField.getText();
        }
        else{
            errorMessage("Indtast en adgangskode");
        }
        return null;
    }

    public Integer getSchool(TextField textField){
        if (!textField.getText().isEmpty()){
            return Integer.parseInt(textField.getText());
        }
        else {
            errorMessage("Indtast gyldig skole ");
        }
        return null;
    }

    public String getPasswordUpdate(TextField password){
        if (!password.getText().isEmpty()){
            return password.getText();
        }
        return null;
    }


    public void openSceneAsUser(User user, String fxmlPath, String Title, ActionEvent actionEvent) throws IOException, SQLServerException {
        FXMLLoader root = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root.load());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);

        IController controller = root.getController();
        controller.setUserInfo(user);

        stage.setTitle(Title);
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * Shows error message
     * @param errorTxt
     */
    public void errorMessage(String errorTxt) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Advarsel");
        alert.setHeaderText(errorTxt);
        alert.showAndWait();
    }

    /**
     * The scene gets closed
     * @param anyButton
     */
    public void closeWindow(Button anyButton){
        Stage stage = (Stage) anyButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Metode anvendt til at åbne de forskellige vinduer i programmet
     * @param pathToFXML - Stien til FXML vinduet
     * @param showAndWait - et boolean parameter der bestemmer om der skal anvendes showAndWait() eller show() metoden
     * @param title - titlen på FXML vinduet
     * @param resizable - et boolean parameter der bestemmer om et vindue kan redigeres i størrelsen
     * @throws IOException
     */
    public void openScene(String pathToFXML, boolean showAndWait, String title, boolean resizable) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(pathToFXML));
        Stage stage = new Stage();
        Scene scene = new Scene(root);


        stage.setTitle(title);
        stage.setResizable(resizable);

        stage.setScene(scene);
        if(showAndWait){
            stage.showAndWait();
        }

        if(!showAndWait){
            stage.show();
        }
    }

    public Optional<ButtonType> confirmationBox(String string){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,string,ButtonType.YES,ButtonType.NO);
        return alert.showAndWait();
    }

    public void openNewSceneAsUser2(User user,String fxmlPath, String Title) throws IOException, SQLServerException {
        FXMLLoader root = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root.load());
        Stage stage = new Stage();
        stage.setScene(scene);

        IController controller = root.getController();
        controller.setUserInfo(user);

        stage.setTitle(Title);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.showAndWait();
    }
}
