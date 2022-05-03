package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.dal.interfaces.ICitizienDAO;
import dk.easv.dal.interfaces.IHealthReport;
import dk.easv.dal.interfaces.ILoginDAO;
import dk.easv.dal.interfaces.ITeacherDAO;

import java.util.List;

public class Facade {

    private ILoginDAO iLoginDAO;
    private ITeacherDAO iTeacherDAO;
    private ICitizienDAO iCitizienDAO;
    private IHealthReport iHealthReport;

    public Facade(ILoginDAO iLoginDAO, ICitizienDAO iCitizienDAO, ITeacherDAO iTeacherDAO){
    }

    private void createCitizen(){
    }

    private List<String> getAllTitles() throws SQLServerException {
        return iHealthReport.getAllTitle();
    }
    private List<String> getAllSubTitles(){
        return iHealthReport.getSubTitles();
    }
}
