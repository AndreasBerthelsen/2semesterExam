package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;

public interface ITemplateDAO {
    void createTemplate(Citizen citizen) throws SQLServerException;
}
