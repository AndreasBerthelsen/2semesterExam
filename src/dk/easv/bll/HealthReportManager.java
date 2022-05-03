package dk.easv.bll;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Category;
import dk.easv.dal.Facade;

import java.util.List;

public class HealthReportManager {

    private Facade facade;
    public HealthReportManager(){
        facade = Facade.getInstance();
    }

    public List<Category> getAllTitles() throws SQLServerException {
        return facade.getAllTitles();
    }
    public List<String> getAllSubTitles(Category category) throws SQLServerException {
        return facade.getAllSubTitles(category);
    }
}
