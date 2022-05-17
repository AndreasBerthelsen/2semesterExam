package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.FunkNodeContainer;
import dk.easv.be.HealthNodeContainer;
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

    void createTemplate(String fName, String lName, Date date, String description, Map<String, TextArea> genInfoMap, Map<Integer, FunkNodeContainer> funkAnswerMap, Map<Integer, HealthNodeContainer> healthAnswerMap, Date obsDate) throws SQLServerException;
}
