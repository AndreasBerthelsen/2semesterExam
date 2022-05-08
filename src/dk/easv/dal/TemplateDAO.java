package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.dal.interfaces.ITemplateDAO;

import java.io.IOException;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemplateDAO implements ITemplateDAO {
    DatabaseConnector dc;

    public TemplateDAO() throws IOException {
        dc = new DatabaseConnector();
    }


    @Override
    public void createTemplate(Citizen citizen) {
        try (Connection connection = dc.getConnection()) {
            int id = createCitizenTemplate(citizen, connection);
            createFunktionsJournalTemplate(citizen, id, connection);
            createHealthJournalTemplate(citizen, id, connection);
            createGenInfoTemplate(citizen, id, connection);
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
        psB.setInt(4, 1);
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
        psGen.execute();
    }

    private void createFunktionsJournalTemplate(Citizen citizen, int id, Connection connection) throws SQLException {
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

    private void createHealthJournalTemplate(Citizen citizen, int id, Connection connection) throws SQLException {
        HashMap<Integer, String> healthInfoMap = citizen.getHelbredInfo();
        HashMap<Integer, Integer> healthRelevansMap = citizen.getRelevansMap();
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

    @Override
    public void deleteTemplate(Citizen citizen) throws SQLException {
        //delete grupper -> journaler x3 -> borger
        try (Connection connection = dc.getConnection()) {
            String sql = "delete from FunktionsJournal where borgerID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, citizen.getId());
            ps.execute();

            String sql2 = "delete from helbredsJournal where borgerID = ?";
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.setInt(1, citizen.getId());
            ps2.execute();

            String sql3 = "delete from generelinfo where borgerID = ?";
            PreparedStatement ps3 = connection.prepareStatement(sql3);
            ps3.setInt(1, citizen.getId());
            ps3.execute();

        }
    }

    @Override
    public List<Citizen> getAllTemplates() throws SQLException {
        //TODO WORK IN PROGRESS
        List<Citizen> list = new ArrayList<>();

        try (Connection connection = dc.getConnection()) {
            String sql = "Select borgerId,fname,lname,dato from Borger where isTemplate = 1";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("borgerid");
                String fName = rs.getString("fname");
                String lName = rs.getString("lname");
                Date date = rs.getDate("dato");

                //gen info
                LinkedHashMap<String, String> genInfoText = loadGenInfoFromId(id, connection);

                //funk
                //todo convert id'er til combobox indexes
                Map<Integer, Integer> currentCombo = loadCurrentComboFromId(id, connection);
                Map<Integer, Integer> targetCombo = loadTargetCombo(id, connection);
                Map<Integer, String> funkInfo = loadFunkInfo(id, connection);

                //health
                //todo convert id'er til combobox indexes | repalce med enums?
                Map<Integer, Integer> relevansMap = loadRelevansMap(id, connection);

                Map<Integer, String> helbredInfo = loadHealthInfo(id,connection);


                // list.add(new Citizen(id,fName,lName,date));
            }
        }
        return list;
    }

    private Map<Integer, String> loadHealthInfo(int id, Connection connection) throws SQLException {
        Map<Integer,String> map = new LinkedHashMap<>();
        String sql = "select [value], problemid from helbredsjournal where borgerid = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1 , id);

        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            map.put(rs.getInt("problemid"),rs.getString("value"));
        }

        return map;
    }

    private Map<Integer, Integer> loadRelevansMap(int id, Connection connection) throws SQLException {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT problemid, relevans from Helbredsjournal where borgerID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            map.put(rs.getInt("problemid"),rs.getInt("relevans"));
        }
        return map;
    }

    private Map<Integer, String> loadFunkInfo(int id, Connection connection) throws SQLException {
        Map<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT problemid, note from FunktionsJournal where borgerID = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            map.put(rs.getInt("problemid"), rs.getString("note"));
        }
        return map;
    }

    private Map<Integer, Integer> loadTargetCombo(int id, Connection connection) throws SQLException {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        String sql = "select problemid, målVurdering from FunktionsJournal inner join targetvurdering on målvurdering = targetid  where borgerid = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            map.put(rs.getInt("problemid"), rs.getInt("målVurdering"));
        }
        return map;
    }

    //hashmap<String,string> // <Titel,value>
    private Map<Integer, Integer> loadCurrentComboFromId(int id, Connection connection) throws SQLException {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        String sql = "select problemid, fNiveau " +
                "from FunktionsJournal INNER join Vurdering on vurderingsID = nuVurdering\n" +
                "where borgerid = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            map.put(rs.getInt("problemid"), rs.getInt("fNiveau"));
        }
        return map;
    }

    private LinkedHashMap<String, String> loadGenInfoFromId(int id, Connection connection) throws SQLException {
        //find coloumns
        String sql = "select COLUMN_NAME \n" +
                "from INFORMATION_SCHEMA.COLUMNS \n" +
                "where TABLE_NAME = N'Generelinfo'";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        //byg sql
        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            String currentColumn = rs.getString("COLUMN_NAME");
            if (!currentColumn.toLowerCase().endsWith("id")) {
                sb.append(currentColumn).append(",");
            }
        }

        String columnTitles = sb.deleteCharAt(sb.length() - 1).toString();
        String sql2 = "Select " + columnTitles + " from generelInfo where borgerid = ?";
        PreparedStatement ps2 = connection.prepareStatement(sql2);
        ps2.setInt(1, id);

        ResultSet rs2 = ps2.executeQuery();
        rs2.next();
        LinkedHashMap<String, String> genInfoMap = new LinkedHashMap<>();
        for (String column : columnTitles.split(",")) {
            genInfoMap.put(column, rs2.getString(column));
        }

        return genInfoMap;
    }

    public void updateCitizen(Citizen citizen) {
        try (Connection connection = dc.getConnection()) {
            String sql = "UPDATE Borger SET fName=?, lName=?, dato=? WHERE borgerID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, citizen.getFirstname());
            preparedStatement.setString(2, citizen.getLastname());
            preparedStatement.setDate(3, citizen.getbDate());
            preparedStatement.setInt(4, citizen.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        TemplateDAO templateDAO = new TemplateDAO();
        DatabaseConnector dc = new DatabaseConnector();
        Connection connection = dc.getConnection();
        System.out.println(templateDAO.loadHealthInfo(38,connection));
    }
}
