package dk.easv.gui.supercontroller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.School;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.gui.Interfaces.ICitizenSelector;
import dk.easv.gui.Interfaces.IController;
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
     * Får fornavnet fra det valgte textfield
     * @param firstNameField tekstfeltet der er associeret med fornavnet
     * giver en fejlbesked, hvis tekstfeltet er null
     * @return En string med fornavnet i
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
     * Får efternavnet fra det valgte textfield
     * @param lastNameField tekstfeltet der er associeret med efternavnet
     *  giver en fejlbesked, hvis tekstfeltet er null
     * @return En string med efternavnet i
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
     * Får brugernavnet fra det valgte textfield
     * @param usernameField tekstfeltet der er associeret med brugernavnet
     * giver en fejlbesked, hvis tekstfeltet er null
     * @return En string med brugernavnet i
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
     * Får passwodet forbundet til det valgte tekstfelt
     * @param passwordField
     * giver en fejlbesked hvis itemet er null
     * @return en string med passwordet i
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

    /**
     * Får skoleID'et fra det valgte item i comboboxen
     * @param comboBox - comboboxen der er associeret med de forsklelige skoler i programmet
     * giver en fejlbesked hvis itemet er null
     * @return En Integer, der repræsenterer skoleID'et
     */
    public Integer getSchoolId(ComboBox<School> comboBox) {
        if (!(comboBox.getSelectionModel().getSelectedItem() == null)) {
            return comboBox.getValue().getId();
        }
        else {
            errorMessage("Vælg venligst en skole");
        }
        return null;
    }

    /**
     * Får userTypen fra det valgte item i comboboxen
     * @param comboBox - comboboxen der er associeret med de forsklelige userTypes i programmet
     * @return En Usertype, der repræsenterer den valgte usertype
     */
    public UserType getUsertype(ComboBox<UserType> comboBox) {
        if (!(comboBox.getSelectionModel().getSelectedItem() == null)) {
            return comboBox.getValue();
        } else {
            errorMessage("Vælg venligst en brugertype");
        }
        return null;
    }
    /**
     * Får passwodet forbundet til det valgte tekstfelt
     * @param password
     * @return en string med passwordet i
     */
    public String getPasswordUpdate(TextField password){
        if (!password.getText().isEmpty()){
            return password.getText();
        }
        return null;
    }

    /**
     * Denne metode åbner en scene, hvori der er tilknyttet en user
     * @param user - Useren der er tilknyttet åbningen af scenen
     * @param fxmlPath - Pathen til FXML'en den skal tilgå
     * @param Title - Titlen på FXML filen
     * @param actionEvent
     * @throws IOException
     * @throws SQLServerException
     */
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
     * Viser en fejl besked
     * @param errorTxt - Teksten på fejlen
     */
    public void errorMessage(String errorTxt) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Advarsel");
        alert.setHeaderText(errorTxt);
        alert.showAndWait();
    }

    /**
     * Lukker vinduet
     * @param anyButton - Knappen der skal trigger metoden
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

    /**
     * Denne metode laver en godkendelse popup
     * @param string - teksten der skal være på godkendelsesboksen
     * @return Optinal klasse, der er af typen ButtonType
     */
    public Optional<ButtonType> confirmationBox(String string){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,string,ButtonType.YES,ButtonType.NO);
        return alert.showAndWait();
    }

    /**
     * Denne metode åbner en scene, hvori der er tilknyttet en user
     * @param user - Useren der er tilknyttet åbningen af scenen
     * @param fxmlPath - Pathen til FXML'en den skal tilgå
     * @param Title - Titlen på FXML filen
     * Forskellen med denne metode og den anden openNewSceneAsUser, er at denne anvender showAndWait() metoden
     * i stedet for show()
     * @throws IOException
     * @throws SQLServerException
     */
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

    /**
     * Denne metode åbner en scene, hvori der er tilknyttet en citizen
     * @param citizen - Citizen der er tilknyttet åbningen af scenen
     * @param fxmlPath - Pathen til FXML'en den skal tilgå
     * @param Title - Titlen på FXML filen
     * @throws IOException
     * @throws SQLServerException
     */
    public void openNewSceneWithCitizen(Citizen citizen, String fxmlPath, String Title) throws IOException, SQLServerException {
        FXMLLoader root = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root.load());
        Stage stage = new Stage();
        stage.setScene(scene);

        ICitizenSelector controller = root.getController();
        controller.setCitizen(citizen);

        stage.setTitle(Title);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.showAndWait();
    }}
