package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Category;
import dk.easv.dal.interfaces.IHealthReport;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HealthDAO implements IHealthReport {

    DatabaseConnector dc;
    public HealthDAO() throws IOException {
        dc = new DatabaseConnector();
    }

    @Override
    public List<Category> getAllTitle() throws SQLServerException {
        List<Category> allTitles = new ArrayList<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT titel\n, tilstandsID \n" +
                    "FROM dbo.HelbredsTilstande";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("titel");
                int id = resultSet.getInt("tilstandsID");
                Category category = new Category(id, name);
                allTitles.add(category);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allTitles;
    }



    @Override
    public List<String> getSubTitles(Category category) throws SQLServerException {
        List<String> subTitles = new ArrayList<>();
        try (Connection connection = dc.getConnection()){
            String sql = "SELECT guiTitel\n" +
                    "FROM HelbredsVanskligheder\n" +
                    "INNER JOIN HelbredsTilstande on HelbredsVanskligheder.tilstandsID = HelbredsTilstande.tilstandsID\n" +
                    "WHERE HelbredsVanskligheder.tilstandsID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, category.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String guiTitel = resultSet.getString("guiTitel");
                subTitles.add(guiTitel);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return subTitles;
    }

}
