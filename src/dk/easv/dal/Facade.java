package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.dal.interfaces.*;

import dk.easv.be.Category;
import dk.easv.dal.interfaces.ICitizienDAO;
import dk.easv.dal.interfaces.IHealthReport;
import dk.easv.dal.interfaces.ILoginDAO;
import dk.easv.dal.interfaces.ITeacherDAO;

import java.sql.SQLException;
import java.util.List;
import dk.easv.dal.interfaces.IGenInfoDAO;


import java.util.ArrayList;
import java.util.HashMap;

public class Facade {

    private static Facade instance;
    private ILoginDAO iLoginDAO;
    private ITeacherDAO iTeacherDAO;
    private ICitizienDAO iCitizienDAO;
    private IHealthReport iHealthReport;
    private IGenInfoDAO iGenInfoDAO;
    private IFunktionsDAO iFunktionsDAO;

    private Facade(ILoginDAO iLoginDAO, ICitizienDAO iCitizienDAO, ITeacherDAO iTeacherDAO, IGenInfoDAO iGenInfoDAO, IFunktionsDAO iFunktionsDAO) {
        this.iLoginDAO = iLoginDAO;
        this.iTeacherDAO = iTeacherDAO;
        this.iCitizienDAO = iCitizienDAO;
        this.iGenInfoDAO = iGenInfoDAO;
        this.iFunktionsDAO = iFunktionsDAO;
    }

    public static void createInstance(ILoginDAO iLoginDAO, ICitizienDAO iCitizienDAO, ITeacherDAO iTeacherDAO, IGenInfoDAO iGenInfoDAO, IFunktionsDAO iFunktionsDAO) {
        if (instance == null) {
            instance = new Facade(iLoginDAO, iCitizienDAO, iTeacherDAO, iGenInfoDAO, iFunktionsDAO);
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

    private List<Category> getAllTitles() throws SQLServerException {
        return iHealthReport.getAllTitle();
    }

    private List<String> getAllSubTitles(Category category) throws SQLServerException {
        return iHealthReport.getSubTitles(category);

    }

    public HashMap<Integer, ArrayList<String>> getFunktionsVandskligheder() {
        return iFunktionsDAO.getFunktionsVandskligheder();
    }

    public User loginUser(String username, String password) throws SQLException {
        return iLoginDAO.loginUser(username, password);
    }
}