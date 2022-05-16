package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.FunkChunkAnswer;
import dk.easv.be.GenInfoAnswer;
import dk.easv.be.HealthChunkAnswer;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ITemplateDAO {
    void deleteTemplate(int citizenId) throws SQLException;

    List<Citizen> getAllTemplates() throws SQLException;

    Citizen loadTemplate(Citizen citizen);

    void updateTemplate(Citizen citizen, int id) throws SQLServerException;

    void createTemplate(String fName, String lName, Date date, String description, Map<Integer, GenInfoAnswer> genInfoMap, Map<Integer, FunkChunkAnswer> funkAnswerMap, Map<Integer, HealthChunkAnswer> healthAnswerMap, Date obsDate) throws SQLServerException;
}
