package dk.easv.dal;

import dk.easv.be.Citizen;
import dk.easv.be.FunkResult;
import dk.easv.be.HealthResult;
import dk.easv.dal.interfaces.ITemplateDAO;

import java.io.IOException;
import java.sql.Date;
import java.sql.*;
import java.util.*;

public class TemplateDAO implements ITemplateDAO {
    DatabaseConnector dc;

    public TemplateDAO() throws IOException {
        dc = new DatabaseConnector();
    }


    @Override
    public void createTemplate(String fName, String lName, Date birthDate, String description, Map<String, String> saveGeninfo, Map<Integer, FunkResult> saveFunk, Map<Integer, HealthResult> saveHealth, Date obsDate) {
        try (Connection connection = dc.getConnection()) {
            int id = createCitizenTemplate(fName, lName, birthDate, description, connection);
            createFunktionsJournalTemplate(saveFunk, id, obsDate, connection);
            createHealthJournalTemplate(saveHealth, id, obsDate, connection);
            createGenInfoTemplate(saveGeninfo, id, connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void createGenInfoTemplate(Map<String, String> genInfoMap, int id, Connection connection) throws SQLException {
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


    private int createCitizenTemplate(String fname, String lName, Date date, String description, Connection connection) throws SQLException {
        String sql = "INSERT INTO Borger (fname, lname, dato, isTemplate,description,lastChanged) VALUES (?,?,?,?,?,?)";
        PreparedStatement psB = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        psB.setString(1, fname);
        psB.setString(2, lName);
        psB.setDate(3, date);
        psB.setInt(4, 1);
        psB.setString(5, description);
        psB.setDate(6, new Date(Calendar.getInstance().getTime().getTime()));

        psB.execute();
        ResultSet idKey = psB.getGeneratedKeys();
        idKey.next();
        return idKey.getInt(1);
    }

    private void createFunktionsJournalTemplate(Map<Integer, FunkResult> answerMap, int id, Date obsDate, Connection connection) throws SQLException {
        String sqlF = "insert into FunktionsJournal (borgerid, problemID, nuVurdering, målvurdering, technicalNote, execution, importanceOfExecution, goalNote,date,obsNote) " +
                "values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement psF = connection.prepareStatement(sqlF);
        for (int key : answerMap.keySet()) {
            FunkResult answer = answerMap.get(key);
            psF.setInt(1, id);
            psF.setInt(2, key);
            psF.setInt(3, answer.getCurrent());
            psF.setInt(4, answer.getTarget());
            psF.setString(5, answer.getTechnical());
            psF.setInt(6, answer.getExecution());
            psF.setInt(7, answer.getImportance());
            psF.setString(8, answer.getCitizenString());
            psF.setDate(9, obsDate);
            psF.setString(10, answer.getObservation());
            psF.addBatch();
        }
        psF.executeBatch();
    }


    private void createHealthJournalTemplate(Map<Integer, HealthResult> answerMap, int id, Date obsDate, Connection connection) throws SQLException {
        String sqlH = "insert into Helbredsjournal(borgerID,problemID,technicalNote,relevans,currentEval,expectedCondition,ObservationNote,Date) values(?,?,?,?,?,?,?,?)";
        PreparedStatement psH = connection.prepareStatement(sqlH);
        for (int key : answerMap.keySet()) {
            HealthResult answer = answerMap.get(key);
            psH.setInt(1, id);
            psH.setInt(2, key);
            psH.setString(3, answer.getTechnical());
            psH.setInt(4, answer.getToggleId());
            psH.setString(5, answer.getCurrent());
            psH.setInt(6, answer.getExpectedIndex());
            psH.setString(7, answer.getObservation());
            psH.setDate(8, Date.valueOf(String.valueOf(obsDate)));
            psH.addBatch();
        }
        psH.executeBatch();
    }

    @Override
    public void deleteTemplate(int citizenId) throws SQLException {
        //delete grupper -> journaler x3 -> borger
        try (Connection connection = dc.getConnection()) {
            String sql = "delete from FunktionsJournal where borgerID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, citizenId);
            ps.execute();

            String sql2 = "delete from helbredsJournal where borgerID = ?";
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.setInt(1, citizenId);
            ps2.execute();

            String sql3 = "delete from generelinfo where borgerID = ?";
            PreparedStatement ps3 = connection.prepareStatement(sql3);
            ps3.setInt(1, citizenId);
            ps3.execute();

            String sql4 = "delete from borger where borgerID = ?";
            PreparedStatement ps4 = connection.prepareStatement(sql4);
            ps4.setInt(1, citizenId);
            ps4.execute();

        }
    }

    @Override
    public List<Citizen> getAllTemplates() throws SQLException {
        //TODO WORK IN PROGRESS
        List<Citizen> list = new ArrayList<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT borgerID, fName, lName, dato, description\n" +
                    "FROM Borger where isTemplate = 1";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("borgerID");
                String fName = resultSet.getString("fName");
                String lName = resultSet.getString("lName");
                Date date = resultSet.getDate("dato");
                String description = resultSet.getString("description");
                list.add(new Citizen(id, fName, lName, date, description));
            }
        }
        return list;
    }

    @Override
    public Map<Integer, HealthResult> loadHealthInfo(int citizenId) {
        Map<Integer, HealthResult> resultMap = new LinkedHashMap<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "select [problemID]" +
                    "      ,[technicalNote]" +
                    "      ,[relevans]" +
                    "      ,[currentEval]" +
                    "      ,[expectedCondition]" +
                    "      ,[observationNote]" +
                    "from Helbredsjournal where borgerId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, citizenId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String technical = rs.getString("technicalNote");
                String observation = rs.getString("observationNote");
                String current = rs.getString("currentEval");
                int expectedIndex = rs.getInt("expectedCondition");
                int toggleId = rs.getInt("relevans");
                HealthResult info = new HealthResult(toggleId, expectedIndex, current, observation, technical);
                int problemid = rs.getInt("problemId");
                resultMap.put(problemid, info);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public Map<Integer, FunkResult> loadFunkInfo(int id) {
        Map<Integer, FunkResult> resultMap = new LinkedHashMap<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "Select [problemID]\n" +
                    "      ,[nuVurdering]\n" +
                    "      ,[målVurdering]\n" +
                    "      ,[technicalNote]\n" +
                    "      ,[execution]\n" +
                    "      ,[importanceOfExecution]\n" +
                    "      ,[goalNote]\n" +
                    "      ,[obsNote] from funktionsjournal where borgerid = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int importance = rs.getInt("importanceOfExecution");
                String citizenString = rs.getString("goalNote");
                String technical = rs.getString("TechnicalNote");
                String observation = rs.getString("obsNote");
                int execution = rs.getInt("execution");
                int target = rs.getInt("målVurdering");
                int current = rs.getInt("nuVurdering");

                int problemId = rs.getInt("problemId");
                FunkResult funkResult = new FunkResult(importance, citizenString, technical, observation, execution, target, current);
                resultMap.put(problemId, funkResult);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public Map<String, String> loadGenInfo(int id, List<String> fieldList) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        try (Connection connection = dc.getConnection()) {
            String geninfoFields = fieldList.toString().replace(" ", "").replace("[", "").replace("]", "");
            String sql = "select " + geninfoFields + " from GenerelInfo where borgerId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            for (String fieldName : fieldList) {
                resultMap.put(fieldName, rs.getString(fieldName));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public void updateTemplate(Citizen updatedCitizen, Map<String, String> genResultMap, Map<Integer, FunkResult> funkResultMap, Map<Integer, HealthResult> healthResultMap, Date obsDate) {
        try (Connection connection = dc.getConnection()) {
            int id = updatedCitizen.getId();
            updateCitizen(updatedCitizen, connection);
            updateGenInfo(id, genResultMap, connection);
            updateFunktion(id, funkResultMap, obsDate, connection);
            updateHealth(id, healthResultMap, obsDate, connection);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void updateHealth(int id, Map<Integer, HealthResult> healthResultMap, Date obsDate, Connection connection) throws SQLException {
        String sql = "UPDATE Helbredsjournal set technicalNote=?,relevans=?,currentEval=?" +
                ",expectedCondition=?,observationNote=?,[Date]=? where borgerId = ? and problemId = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        for (int problemid : healthResultMap.keySet()) {
            HealthResult result = healthResultMap.get(problemid);
            ps.setString(1, result.getTechnical());
            ps.setInt(2, result.getToggleId());
            ps.setString(3, result.getCurrent());
            ps.setInt(4, result.getExpectedIndex());
            ps.setString(5, result.getObservation());
            ps.setDate(6, obsDate);
            ps.setInt(7, id);
            ps.setInt(8, problemid);
            ps.addBatch();
        }
        ps.executeBatch();
    }

    private void updateFunktion(int id, Map<Integer, FunkResult> funkResultMap, Date obsDate, Connection connection) throws SQLException {
        String sql = "UPDATE funktionsJournal set nuVurdering = ?,målVurdering =?,technicalNote=?,execution =?" +
                ",importanceOfExecution =?,goalNote=?,[date]=?,obsNote =? where borgerId = ? and problemId = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        for (int problemId : funkResultMap.keySet()) {
            FunkResult result = funkResultMap.get(problemId);
            ps.setInt(1, result.getCurrent());
            ps.setInt(2, result.getTarget());
            ps.setString(3, result.getTechnical());
            ps.setInt(4, result.getExecution());
            ps.setInt(5, result.getImportance());
            ps.setString(6, result.getCitizenString());
            ps.setDate(7, obsDate);
            ps.setString(8, result.getObservation());
            ps.setInt(9, id);
            ps.setInt(10, problemId);
            ps.addBatch();
        }
        ps.executeBatch();
    }

    private void updateGenInfo(int id, Map<String, String> genResultMap, Connection connection) throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (String key : genResultMap.keySet()) {
            sb.append(key).append("=?,");
        }
        String columns = sb.deleteCharAt(sb.length() - 1).toString();
        String sql = "Update generelInfo set " + columns + " where borgerId = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        int index = 1;
        for (String column : genResultMap.keySet()) {
            ps.setString(index++, genResultMap.get(column));
        }
        ps.setInt(index, id);
        ps.execute();
    }

    private void updateCitizen(Citizen updatedCitizen, Connection connection) throws SQLException {
        String sql = "Update Borger set fName =?,lName=?,dato=?,description=? where borgerid = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, updatedCitizen.getFirstname());
        ps.setString(2, updatedCitizen.getLastname());
        ps.setDate(3, updatedCitizen.getbDate());
        ps.setString(4, updatedCitizen.getDescription());
        ps.setInt(5, updatedCitizen.getId());
        ps.execute();
    }
}

