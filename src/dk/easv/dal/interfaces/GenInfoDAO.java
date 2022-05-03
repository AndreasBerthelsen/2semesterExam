package dk.easv.dal.interfaces;

import dk.easv.dal.DatabaseConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GenInfoDAO implements IGenInfoDAO{
    DatabaseConnector dc;

    public GenInfoDAO() throws IOException {
        this.dc  = new DatabaseConnector();
    }

    public ArrayList<String> getGeneralinfoFields(){
        ArrayList<String> fieldList = new ArrayList<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "select COLUMN_NAME \n" +
                    "from INFORMATION_SCHEMA.COLUMNS \n" +
                    "where TABLE_NAME = N'Generelinfo'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String currentColumn = resultSet.getString("COLUMN_NAME");
                if (!currentColumn.toLowerCase().endsWith("id")) {
                    fieldList.add(currentColumn);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return fieldList;
    }
}
