package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.*;
import javafx.scene.control.TextArea;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ITemplateDAO {
    void deleteTemplate(int citizenId) throws SQLException;

    List<Citizen> getAllTemplates() throws SQLException;

    Citizen loadTemplate(Citizen citizen);

    void updateTemplate(Citizen citizen, int id) throws SQLServerException;

    void createTemplate(String fName, String lName, Date birthDate, String description, Map<String, String> saveGeninfo, Map<Integer, FunkResult> saveFunk, Map<Integer, HealthResult> saveHealth, Date obsDate) throws SQLServerException;
}
