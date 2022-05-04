package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.dal.interfaces.*;

import dk.easv.be.Category;
import dk.easv.dal.interfaces.ICitizienDAO;
import dk.easv.dal.interfaces.IHealthDAO;
import dk.easv.dal.interfaces.ILoginDAO;
import dk.easv.dal.interfaces.IUserDAO;

import java.sql.SQLException;
import java.util.List;
import dk.easv.dal.interfaces.IGenInfoDAO;


import java.util.ArrayList;
import java.util.HashMap;

public class Facade {

    private static Facade instance;
    private ILoginDAO iLoginDAO;
    private IUserDAO iUserDAO;
    private ICitizienDAO iCitizienDAO;
    private IHealthDAO iHealthDAO;
    private IGenInfoDAO iGenInfoDAO;
    private IFunktionsDAO iFunktionsDAO;

    private Facade(ILoginDAO iLoginDAO, ICitizienDAO iCitizienDAO, IUserDAO iUserDAO, IGenInfoDAO iGenInfoDAO, IFunktionsDAO iFunktionsDAO, IHealthDAO iHealthDAO) {
        this.iLoginDAO = iLoginDAO;
        this.iUserDAO = iUserDAO;
        this.iCitizienDAO = iCitizienDAO;
        this.iGenInfoDAO = iGenInfoDAO;
        this.iFunktionsDAO = iFunktionsDAO;
        this.iHealthDAO = iHealthDAO;
    }

    public static void createInstance(ILoginDAO iLoginDAO, ICitizienDAO iCitizienDAO, IUserDAO iUserDAO, IGenInfoDAO iGenInfoDAO, IFunktionsDAO iFunktionsDAO, IHealthDAO iHealthDAO) {
        if (instance == null) {
            instance = new Facade(iLoginDAO, iCitizienDAO, iUserDAO, iGenInfoDAO, iFunktionsDAO, iHealthDAO);
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
        iUserDAO.createUser(firstName,lastName, username, hashedPassword, salt, userType);
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
    public void deleteUser(User user){
        iUserDAO.deleteUser(user);
    }
    public void updateUser(User user){
        iUserDAO.updateUser(user);
    }
}