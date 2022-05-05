package dk.easv.dal;

import dk.easv.be.Section;
import dk.easv.dal.interfaces.IFunktionsDAO;
import javafx.application.Platform;
import javafx.scene.control.RadioButton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunktionsDAO implements IFunktionsDAO {
    DatabaseConnector dc;

    public FunktionsDAO() throws IOException {
        dc = new DatabaseConnector();
    }

    @Override
    public HashMap<Integer, String> getFunktionsTilstande() {
        HashMap<Integer, String> map = new HashMap<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT funkid, titel from FunktionsTilstande ORDER by funkid asc";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String s = resultSet.getString("titel");
                Integer i = resultSet.getInt("funkid");
                map.put(i, s);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return map;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getFunktionsVandskligheder() {
        HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT fTilstandsID, guititel from FunktionsVanskligheder ORDER by fTilstandsID asc";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            //gen arraylist for hver ny key
            int currentKey = 0;
            ArrayList<String> currentArraylist = new ArrayList<>();
            while (resultSet.next()) {
                String s = resultSet.getString("guititel");
                int i = resultSet.getInt("fTilstandsID");
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
    public List<Section> getFunkSections() {
        List<Section> sectionList = new ArrayList<>();

        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT problemid, fTilstandsID, guititel, titel\n" +
                    "            from FunktionsVanskligheder\n" +
                    "            INNER join FunktionsTilstande on fTilstandsID = funkid\n" +
                    "            ORDER by fTilstandsID asc";


            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            HashMap<Integer, String> idTitelMap = new HashMap<>();
            int currentSection = 0;
            String sectionTitle = null;
            while (resultSet.next()) {
                int problemId = resultSet.getInt("problemid");
                String guiTitel = resultSet.getString("guititel");
                int ownerID = resultSet.getInt("fTilstandsID");
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
