package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Category;
import dk.easv.be.Section;
import dk.easv.dal.interfaces.IHealthDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthDAO implements IHealthDAO {

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

    @Override
    public HashMap<Integer, String> getHelbredsTilstande() {
        HashMap<Integer, String> map = new HashMap<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT tilstandsID, titel from HelbredsTilstande ORDER by tilstandsID asc";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String s = resultSet.getString("titel");
                Integer i = resultSet.getInt("tilstandsID");
                map.put(i, s);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getHelbredsVanskligheder() {
        HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT TilstandsID, guititel from HelbredsVanskligheder ORDER by TilstandsID asc";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            //gen arraylist for hver ny key
            int currentKey = 0;
            ArrayList<String> currentArraylist = new ArrayList<>();
            while (resultSet.next()) {
                String s = resultSet.getString("guititel");
                int i = resultSet.getInt("TilstandsID");
                if (currentKey < i) {
                    currentArraylist = new ArrayList<>();
                    currentArraylist.add(s);
                    currentKey = i;
                } else {
                    currentArraylist.add(s);
                }
                map.put(i, currentArraylist);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return map;
    }

    @Override
    public List<Section> getHealthSections() {
        List<Section> sectionList = new ArrayList<>();

        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT problemid, HelbredsVanskligheder.tilstandsID, guititel, titel\n" +
                    "from HelbredsVanskligheder\n" +
                    "INNER join HelbredsTilstande on HelbredsVanskligheder.tilstandsID = HelbredsTilstande.tilstandsId\n" +
                    "ORDER by tilstandsID asc";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            HashMap<Integer, String> idTitelMap = new HashMap<>();
            int currentSection = 0;
            String sectionTitle = null;
            while (resultSet.next()) {
                int problemId = resultSet.getInt("problemid");
                String guiTitel = resultSet.getString("guititel");
                int ownerID = resultSet.getInt("tilstandsID");
                if (currentSection != ownerID) {
                    if (!idTitelMap.isEmpty()) {
                        sectionList.add(new Section(currentSection,sectionTitle,idTitelMap));
                        idTitelMap = new HashMap<>();
                        idTitelMap.put(problemId, guiTitel);
                    }
                    currentSection = ownerID;
                }
                sectionTitle = resultSet.getString("titel");
                idTitelMap.put(problemId, guiTitel);
            }
            //sidste add for at få den sidste sektion med
            sectionList.add(new Section(currentSection,sectionTitle,idTitelMap));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sectionList;
    }
}
