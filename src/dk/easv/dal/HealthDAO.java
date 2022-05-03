package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.dal.interfaces.IHealthReport;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HealthDAO implements IHealthReport {

    DatabaseConnector dc;
    public HealthDAO() throws IOException {
        dc = new DatabaseConnector();
    }

    @Override
    public List<String> getAllTitle() throws SQLServerException {
        List<String> allTitles = new ArrayList<>();
        try (Connection connection = dc.getConnection()) {
            String sql;


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    @Override
    public List<String> getSubTitles() {
        return null;
    }


}
