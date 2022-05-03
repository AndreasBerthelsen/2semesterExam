package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.dal.interfaces.ICitizienDAO;
import dk.easv.dal.interfaces.IHealthReport;
import dk.easv.dal.interfaces.ILoginDAO;
import dk.easv.dal.interfaces.ITeacherDAO;

import java.util.List;
import dk.easv.dal.interfaces.IGenInfoDAO;
import dk.easv.dal.interfaces.ILoginDAO;
import dk.easv.dal.interfaces.ITeacherDAO;

import java.util.ArrayList;

public class Facade {

    private static Facade instance;
    private ILoginDAO iLoginDAO;
    private ITeacherDAO iTeacherDAO;
    private ICitizienDAO iCitizienDAO;
    private IHealthReport iHealthReport;
    private IGenInfoDAO iGenInfoDAO;

    private Facade(ILoginDAO iLoginDAO, ICitizienDAO iCitizienDAO, ITeacherDAO iTeacherDAO, IGenInfoDAO iGenInfoDAO) {
        this.iLoginDAO = iLoginDAO;
        this.iTeacherDAO = iTeacherDAO;
        this.iCitizienDAO = iCitizienDAO;
        this.iGenInfoDAO = iGenInfoDAO;
    }

    public static void createInstance(ILoginDAO iLoginDAO, ICitizienDAO iCitizienDAO, ITeacherDAO iTeacherDAO, IGenInfoDAO iGenInfoDAO) {
        if (instance == null) {
            instance = new Facade(iLoginDAO, iCitizienDAO, iTeacherDAO, iGenInfoDAO);
        }
    }

    public static Facade getInstance() {
        return instance;
    }

    public ArrayList<String> getGeneralinfoFields() {
        return iGenInfoDAO.getGeneralinfoFields();
    }

    private List<String> getAllTitles() throws SQLServerException {
        return iHealthReport.getAllTitle();
    }
    private List<String> getAllSubTitles(){
        return iHealthReport.getSubTitles();
    }
}
