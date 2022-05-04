import dk.easv.dal.*;
import dk.easv.dal.interfaces.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) throws IOException {
         ILoginDAO iLoginDAO = new LoginDAO();
         ITeacherDAO iTeacherDAO = new TeacherDAO();
         ICitizienDAO iCitizienDAO = new CitizienDAO();
         IGenInfoDAO iGenInfoDAO = new GenInfoDAO();
         IFunktionsDAO iFunktionsDAO = new FunktionsDAO();
         IHealthDAO iHealthDAO = new HealthDAO();

        Facade.createInstance(iLoginDAO,iCitizienDAO,iTeacherDAO,iGenInfoDAO,iFunktionsDAO, iHealthDAO);

        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/Teacher/view/NySkabelonMain.fxml")));
        Scene scene = new Scene(parent);
        stage.setTitle("SOSU FS3");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.setMaximized(true);
        //stage.setFullScreen(true); Giver en F11 fullscreen
        stage.show();
    }
}
