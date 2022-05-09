package dk.easv.gui.teacher.model;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Category;
import dk.easv.bll.HealthReportManager;

import java.util.List;

public class HealthReportModel {

    private HealthReportManager healthReportManager;

    public HealthReportModel() {
        healthReportManager = new HealthReportManager();
    }

    public List<Category> getAllTitles() throws SQLServerException {
        return healthReportManager.getAllTitles();
    }

    public List<String> getAllSubTitles(Category category) throws SQLServerException {
        return healthReportManager.getAllSubTitles(category);
    }
}
