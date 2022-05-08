package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.*;
import dk.easv.dal.interfaces.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Facade {
    private static Facade instance;
    private ILoginDAO iLoginDAO;
    private IUserDAO iUserDAO;
    private ICitizienDAO iCitizienDAO;
    private IHealthDAO iHealthDAO;
    private IGenInfoDAO iGenInfoDAO;
    private IFunktionsDAO iFunktionsDAO;
    private ITemplateDAO iTemplateDAO;

    private Facade(ILoginDAO iLoginDAO, ICitizienDAO iCitizienDAO, IUserDAO iUserDAO, IGenInfoDAO iGenInfoDAO, IFunktionsDAO iFunktionsDAO, IHealthDAO iHealthDAO, ITemplateDAO itemplateDAO) {
        this.iLoginDAO = iLoginDAO;
        this.iUserDAO = iUserDAO;
        this.iCitizienDAO = iCitizienDAO;
        this.iGenInfoDAO = iGenInfoDAO;
        this.iFunktionsDAO = iFunktionsDAO;
        this.iHealthDAO = iHealthDAO;
        this.iTemplateDAO = itemplateDAO;
    }

    public static void createInstance(ILoginDAO iLoginDAO, ICitizienDAO iCitizienDAO, IUserDAO iUserDAO, IGenInfoDAO iGenInfoDAO, IFunktionsDAO iFunktionsDAO, IHealthDAO iHealthDAO, ITemplateDAO itemplateDAO) {
        if (instance == null) {
            instance = new Facade(iLoginDAO, iCitizienDAO, iUserDAO, iGenInfoDAO, iFunktionsDAO, iHealthDAO,itemplateDAO);
        }
    }

    public static Facade getInstance() {
        return instance;
    }

    public ArrayList<String> getGeneralinfoFields() {
        return iGenInfoDAO.getGeneralinfoFields();
    }


    public HashMap<Integer, String> getFunktionsTilstande() {
        return iFunktionsDAO.getFunktionsTilstande();
    }

    public List<Category> getAllTitles() throws SQLServerException {
        return iHealthDAO.getAllTitle();
    }

    public List<String> getAllSubTitles(Category category) throws SQLServerException {
        return iHealthDAO.getSubTitles(category);

    }

    public HashMap<Integer, ArrayList<String>> getFunktionsVandskligheder() {
        return iFunktionsDAO.getFunktionsVandskligheder();
    }

    public User loginUser(String username, String password) throws SQLException {
        return iLoginDAO.loginUser(username, password);
    }

    public void createUser(String firstName, String lastName, String username, String hashedPassword, String salt, UserType userType) throws SQLException {
        iUserDAO.createUser(firstName, lastName, username, hashedPassword, salt, userType);
    }

    public HashMap<Integer, String> getHelbredsTilstande() {
        return iHealthDAO.getHelbredsTilstande();
    }

    public HashMap<Integer, ArrayList<String>> getHelbredVanskligheder() {
        return iHealthDAO.getHelbredsVanskligheder();
    }

    public List<User> getAllUsers(UserType userType) throws SQLServerException {
        return iUserDAO.getAllUsers(userType);
    }

    public void deleteUser(User user) {
        iUserDAO.deleteUser(user);
    }

    public void updateUser(User user) {
        iUserDAO.updateUser(user);
    }

    public List<Citizen> getAllCitiziens() {
        return iCitizienDAO.getAllCitizens();
    }


    public List<Citizen> getAllCitiziensFromUser(User user) {
        return iCitizienDAO.getAllCitizensFromUser(user);
    }

    public void createCitizen(String fname, String lname, Date birthDay) {
        iCitizienDAO.createCitizen(fname, lname, birthDay);
    }

    public void addUserToCitizen(Citizen citizen, User user) {
        iCitizienDAO.addUserToCitizen(user, citizen);
    }

    public List<Section> getFunkSections() {
        return iFunktionsDAO.getFunkSections();
    }

    public List<Section> getHealthSections() {
        return iHealthDAO.getHealthSections();
    }


    public void saveTemplate(Citizen citizen) throws SQLServerException {
        iTemplateDAO.createTemplate(citizen);
    }

    public void updatePassword(User user, String hashPassword, String salt) throws SQLServerException {
        iUserDAO.updatePassword(user, hashPassword, salt);
    }

    public void deleteTemplate(Citizen citizen) throws SQLException {
     iTemplateDAO.deleteTemplate(citizen);
    }

    public List<Citizen> getAllTemplates() throws SQLException {
        return iTemplateDAO.getAllTemplates();
    }

    public void updateCitizen(Citizen citizen) {
        iTemplateDAO.updateCitizen(citizen);
    }
}
