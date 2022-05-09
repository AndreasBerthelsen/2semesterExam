package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;

import java.sql.SQLException;
import java.util.List;

public interface ITemplateDAO {
    void createTemplate(Citizen citizen) throws SQLServerException;
    void deleteTemplate(int citizenId) throws SQLException;

    List<Citizen> getAllTemplates() throws SQLException;

    void updateCitizen(Citizen citizen);

    Citizen loadTemplate(Citizen citizen);

    void updateTemplate(Citizen citizen, int id) throws SQLServerException;
}
