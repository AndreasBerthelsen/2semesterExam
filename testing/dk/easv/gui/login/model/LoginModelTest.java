package dk.easv.gui.login.model;

import dk.easv.dal.*;
import dk.easv.dal.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

class LoginModelTest {
    ILoginDAO iLoginDAO = new LoginDAO();
    IUserDAO iUserDAO = new UserDAO();
    ICitizienDAO iCitizienDAO = new CitizienDAO();
    IGenInfoDAO iGenInfoDAO = new GenInfoDAO();
    IFuncDAO iFunktionsDAO = new FunctionDAO();
    IHealthDAO iHealthDAO = new HealthDAO();
    ITemplateDAO iTemplateDAO = new TemplateDAO();

    LoginModelTest() throws IOException {
        Facade.createInstance(iLoginDAO, iCitizienDAO, iUserDAO, iGenInfoDAO, iFunktionsDAO, iHealthDAO, iTemplateDAO);
    }


    @DisplayName("Correct login info")
    @Test
    void correctLogin() throws SQLException {
        LoginModel loginModel = new LoginModel();

        String teacherUsername = "OleR";
        String teacherPassword = "123456";

        Assertions.assertNotNull(loginModel.loginUser(teacherUsername,teacherPassword));
    }

    @DisplayName("wrong username")
    @Test
    void wrongUsername() throws SQLException {
        LoginModel loginModel = new LoginModel();

        String teacherUsername = "nonexistentUsername";
        String teacherPassword = "123456";

        Assertions.assertNull(loginModel.loginUser(teacherUsername,teacherPassword));

    }

    @DisplayName("wrong password")
    @Test
    void wrongPassword() throws SQLException {
        LoginModel loginModel = new LoginModel();
        String teacherUsername = "OleR";
        String teacherPassword = "WrongPassword";

        Assertions.assertNull(loginModel.loginUser(teacherUsername,teacherPassword));
    }

    @DisplayName("wrong username and password")
    @Test
    void wrongUAndP() throws SQLException {
        LoginModel loginModel = new LoginModel();
        String teacherUsername = "WrongUsername";
        String teacherPassword = "WrongPassword";

        Assertions.assertNull(loginModel.loginUser(teacherUsername,teacherPassword));
    }


    @DisplayName("blank username")
    @Test
    void blankUsername() throws SQLException {
        LoginModel loginModel = new LoginModel();
        String teacherUsername = "";
        String teacherPassword = "WrongPassword";

        Assertions.assertNull(loginModel.loginUser(teacherUsername,teacherPassword));
    }

    @DisplayName("blank password")
    @Test
    void blankPassword() throws SQLException {
        LoginModel loginModel = new LoginModel();
        String teacherUsername = "OleR";
        String teacherPassword = "";

        Assertions.assertNull(loginModel.loginUser(teacherUsername,teacherPassword));
    }

    @DisplayName("blank username and password")
    @Test
    void blankUAndP() throws SQLException {
        LoginModel loginModel = new LoginModel();
        String teacherUsername = "";
        String teacherPassword = "";

        Assertions.assertNull(loginModel.loginUser(teacherUsername,teacherPassword));
    }

}