package dk.easv.dal;

import dk.easv.dal.interfaces.IFunktionsDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class FunktionsDAO implements IFunktionsDAO {
    DatabaseConnector dc;

    public FunktionsDAO() throws IOException {
        dc = new DatabaseConnector();
    }

    @Override
    public HashMap<Integer, String> getFunktionsTilstande() {
        HashMap<Integer, String> map = new HashMap<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT funkid, titel from FunktionsTilstande";
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
}
