package dk.easv.gui.supercontroller;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class SuperController {

    public void setUserInfo(User user) {
    }


    public void openScene(User user, String fxmlPath, String Title, ActionEvent actionEvent) throws IOException, SQLServerException {
        FXMLLoader root = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root.load());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);

        setUserInfo(user);

        stage.setTitle(Title);
        stage.centerOnScreen();
        stage.setResizable(false);
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
}
