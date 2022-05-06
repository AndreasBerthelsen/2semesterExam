package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.dal.DatabaseConnector;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemplateDAO implements ITemplateDAO{
   DatabaseConnector dc;

    public TemplateDAO() throws IOException {
        dc = new DatabaseConnector();
    }

    @Override
    public void createTemplate(Citizen citizen) {
        try (Connection connection = dc.getConnection()) {
            int id = createCitizenTemplate(citizen,connection);
            createFunktionsJournalTemplate(citizen,id,connection);
            createHealthJournalTemplate(citizen,id,connection);
            createGenInfoTemplate(citizen,id,connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private int createCitizenTemplate(Citizen citizen, Connection connection) throws SQLException {
        String sql = "INSERT INTO Borger (fname, lname, dato, isTemplate) VALUES (?,?,?,?)";
        PreparedStatement psB = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        psB.setString(1, citizen.getFirstname());
        psB.setString(2, citizen.getLastname());
        psB.setDate(3, citizen.getbDate());
        psB.setInt(4,1);
        psB.execute();

        ResultSet idKey = psB.getGeneratedKeys();
        idKey.next();
        return idKey.getInt(1);
    }
    private void createGenInfoTemplate(Citizen citizen, int id, Connection connection) throws SQLException {
        HashMap<String, String> genInfoMap = citizen.getGenInfoText();

        String geninfoFields = genInfoMap.keySet().toString().replace(" ", "").replace("[", "").replace("]", "");
        StringBuilder sb = new StringBuilder();
        for (String s : geninfoFields.split(",")) {
            sb.append("?,");
        }
        String genMarks = sb.deleteCharAt(sb.length() - 1).toString();

        String genSql = "Insert into Generelinfo (borgerId," + geninfoFields + ") values(?," + genMarks + ")";
        PreparedStatement psGen = connection.prepareStatement(genSql);
        int index = 2;
        psGen.setInt(1, id);
        for (String s : genInfoMap.values()) {
            psGen.setString(index++, s);
        }
        psGen.addBatch();
    }
    private void createFunktionsJournalTemplate(Citizen citizen,int id, Connection connection) throws SQLException {
        HashMap<Integer, Integer> funkCurrentCombo = citizen.getCurrentCombo();
        HashMap<Integer, Integer> funkTargetCombo = citizen.getTargetCombo();
        HashMap<Integer, String> funkInfo = citizen.getFunkInfo();

        List<Integer> funkMasterKeys = Stream.of(funkCurrentCombo.keySet(), funkTargetCombo.keySet(), funkInfo.keySet())
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        String sqlF = "insert into FunktionsJournal (borgerid, problemID, nuVurdering, målvurdering, note) values(?,?,?,?,?)";
        PreparedStatement psF = connection.prepareStatement(sqlF);
        for (int key : funkMasterKeys) {
            psF.setInt(1, id);
            psF.setInt(2, key);
            psF.setInt(3, funkCurrentCombo.get(key));
            psF.setInt(4, funkTargetCombo.get(key));
            psF.setString(5, funkInfo.get(key));
            psF.addBatch();
        }
        psF.executeBatch();
    }
    private void createHealthJournalTemplate(Citizen citizen,int id,Connection connection) throws SQLException {
        HashMap<Integer,String> healthInfoMap= citizen.getHelbredInfo();
        HashMap<Integer,Integer> healthRelevansMap = citizen.getRelevansMap();
        //helbred
        List<Integer> healthMasterKeys = Stream.of(healthInfoMap.keySet(), healthRelevansMap.keySet())
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        String sqlH = "insert into Helbredsjournal(borgerID,problemID,value,relevans) values(?,?,?,?)";
        PreparedStatement psH = connection.prepareStatement(sqlH);
        for (int key : healthMasterKeys) {
            psH.setInt(1, id);
            psH.setInt(2, key);
            psH.setString(3, healthInfoMap.get(key));
            psH.setInt(4, healthRelevansMap.get(key));
            psH.addBatch();
        }
        psH.executeBatch();
    }

}
