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
        IUserDAO iUserDAO = new UserDAO();
        ICitizienDAO iCitizienDAO = new CitizienDAO();
        IGenInfoDAO iGenInfoDAO = new GenInfoDAO();
        IFunktionsDAO iFunktionsDAO = new FunktionsDAO();
        IHealthDAO iHealthDAO = new HealthDAO();
        ITemplateDAO iTemplateDAO = new TemplateDAO();

        Facade.createInstance(iLoginDAO, iCitizienDAO, iUserDAO, iGenInfoDAO, iFunktionsDAO, iHealthDAO, iTemplateDAO);

        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/dk/easv/gui/login/view/LoginView.fxml")));

        Scene scene = new Scene(parent);
        stage.setTitle("SOSU FS3");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.setMaximized(false);
        //stage.setFullScreen(true); Giver en F11 fullscreen
        stage.show();
    }
}
