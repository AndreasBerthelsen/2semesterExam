package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.FunkResult;
import dk.easv.be.HealthResult;
import dk.easv.be.User;
import dk.easv.dal.interfaces.ICitizienDAO;

import java.io.IOException;
import java.sql.Date;
import java.sql.*;
import java.util.*;

public class CitizienDAO implements ICitizienDAO {
    private DatabaseConnector dc;

    public CitizienDAO() throws IOException {
        dc = new DatabaseConnector();
    }


    @Override
    public List<Citizen> getAllCitizens() {
        ArrayList<Citizen> allCitizen = new ArrayList<>();
        try (Connection connection = dc.getConnection()) {

            String sql = "SELECT borgerID, fName, lName, dato\n" +
                    "FROM Borger WHERE isTemplate = 0";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("borgerID");
                String fname = resultSet.getString("fName");
                String lName = resultSet.getString("lName");
                Date date = resultSet.getDate("dato");
                allCitizen.add(new Citizen(id, fname, lName, date));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allCitizen;
    }

    @Override
    public List<Citizen> getAllTemplatesOfCitizens() {
        ArrayList<Citizen> allTemp = new ArrayList<>();
        try (Connection connection = dc.getConnection()) {

            String sql = "SELECT borgerID, fName, lName, dato, description\n" +
                    "FROM Borger WHERE isTemplate = 1";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("borgerID");
                String fname = resultSet.getString("fName");
                String lName = resultSet.getString("lName");
                Date date = resultSet.getDate("dato");
                String description = resultSet.getString("description");
                allTemp.add(new Citizen(id, fname, lName, date, description));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allTemp;
    }

    @Override
    public void createCitizen(String fName, String lName, Date birthDate) {
        try (Connection connection = dc.getConnection()) {
            String sql = "INSERT INTO Borger (fname, lname, dato) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setDate(3, birthDate);
            ;
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void addUserToCitizen(User user, Citizen citizen) {
        try (Connection connection = dc.getConnection()) {
            String sql = "INSERT INTO CitUser (citFK, userFK) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, citizen.getId());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteCitizenFromUser(Citizen citizenToBeDeleted, User user) {
        try (Connection connection = dc.getConnection()) {
            String sql = "DELETE FROM CitUser WHERE citFK = ? AND userFK = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, citizenToBeDeleted.getId());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public List<Citizen> getAllCitizensFromUser(User user) {
        List<Citizen> citizensFromUser = new ArrayList<>();
        try (Connection connection = dc.getConnection()) {
            String citizenSQL = "SELECT borgerID, fName, lName, dato, lastChanged FROM Borger\n" +
                    "INNER JOIN CitUser ON Borger.borgerID = CitUser.citFK\n" +
                    "WHERE CitUser.userFK = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(citizenSQL);
            preparedStatement.setInt(1, user.getId());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("borgerID");
                String fName = rs.getString("fName");
                String lName = rs.getString("lName");
                Date dato = rs.getDate("dato");
                Date lastChanged = rs.getDate("lastChanged");
                citizensFromUser.add(new Citizen(id, fName, lName, dato, lastChanged));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return citizensFromUser;
    }

    public void updateLastEdited(Citizen citizen) throws SQLException {
        java.util.Date date = new java.util.Date();
        java.sql.Date lastChanged = new java.sql.Date(date.getTime());

        try (Connection connection = dc.getConnection()) {
            String dateSQL = "UPDATE Borger SET lastChanged =? WHERE borgerID =?";
            PreparedStatement preparedStatement = connection.prepareStatement(dateSQL);
            preparedStatement.setDate(1, lastChanged);
            preparedStatement.setInt(2, citizen.getId());
            preparedStatement.execute();
        }
    }


    @Override
    public void createCopyCitizen(Citizen citizen) {
        try (Connection connection = dc.getConnection()) {
            int newID = createCitizenToCopy(citizen, connection);
            int oldID = citizen.getId();
            createFunkTilCopy(oldID, connection, newID);
            createHelbTilCopy(oldID, connection, newID);
            createGenInfoCopy(oldID, connection, newID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void createCopyCase(Citizen citizen, String fName, String lName) throws SQLServerException {
        try (Connection connection = dc.getConnection()) {
            int newID = createCaseToCopy(citizen, connection, fName, lName);
            int oldID = citizen.getId();
            createFunkTilCopyCase(oldID, connection, newID);
            createHelbTilCopyCase(oldID, connection, newID);
            createGenInfoCopyCase(oldID, connection, newID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public void deleteCitizen(int citizenId) throws SQLException {
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

            String sql4 = "delete from CitUser where citFK = ?";
            PreparedStatement ps4 = connection.prepareStatement(sql4);
            ps4.setInt(1, citizenId);
            ps4.execute();

            String sql5 = "delete from borger where borgerID = ?";
            PreparedStatement ps5 = connection.prepareStatement(sql5);
            ps5.setInt(1, citizenId);
            ps5.execute();
        }
    }

    private int createCaseToCopy(Citizen citizen, Connection connection, String fName, String lName) throws SQLException {
        String sql = "INSERT INTO Borger (fName, lName, dato, isTemplate)\n" +
                "SELECT ?, ?, dato, 1 \n" +
                "FROM Borger\n" +
                "WHERE borgerID = ? ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, fName);
        preparedStatement.setString(2, lName);
        preparedStatement.setInt(3, citizen.getId());
        preparedStatement.execute();

        ResultSet idKey = preparedStatement.getGeneratedKeys();
        idKey.next();
        return idKey.getInt(1);
    }

    private int createCitizenToCopy(Citizen citizen, Connection connection) throws SQLException {
        String sql = "INSERT INTO Borger (fName, lName, dato, isTemplate)\n" +
                "SELECT fName, lName, dato, 0 \n" +
                "FROM Borger\n" +
                "WHERE borgerID = ? ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, citizen.getId());
        preparedStatement.execute();

        ResultSet idKey = preparedStatement.getGeneratedKeys();
        idKey.next();
        return idKey.getInt(1);
    }

    private void createHelbTilCopy(int oldId, Connection connection, int newID) throws SQLException {
        String SQL = "INSERT INTO HelbredsJournal(borgerID, problemID, value, relevans)\n" +
                "SELECT ?,problemID, [value], relevans\n" +
                "FROM Helbredsjournal\n" +
                "WHERE borgerID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setInt(1, newID);
        preparedStatement.setInt(2, oldId);
        preparedStatement.execute();
    }

    private void createFunkTilCopy(int oldId, Connection connection, int newID) throws SQLException {
        String sql = "INSERT INTO FunktionsJournal(borgerID,problemID, nuVurdering, målVurdering, note)\n" +
                "SELECT ?,problemID, nuVurdering, målVurdering, note\n" +
                "FROM FunktionsJournal\n" +
                "WHERE borgerID = ?\n";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, newID);
        preparedStatement.setInt(2, oldId);
        preparedStatement.execute();
    }

    //TODO gør så den scaler
    private void createGenInfoCopy(int oldId, Connection connection, int newID) throws SQLException {
        String sql = "INSERT INTO Generelinfo(Mestring, Motivation, Ressourcer, Roller, Vaner, Uddannelse_og_job, Livshistorie, Netværk, Helbredsoplysninger, Hjælpemidler, Bolig, borgerID)\n" +
                "SELECT Mestring, Motivation, Ressourcer, Roller, Vaner, Uddannelse_og_job, Livshistorie, Netværk, Helbredsoplysninger, Hjælpemidler, Bolig, ? \n"
                + "FROM Generelinfo \n"
                + "WHERE borgerID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, newID);
        preparedStatement.setInt(2, oldId);
        preparedStatement.execute();

    }

    private void createHelbTilCopyCase(int oldId, Connection connection, int newID) throws SQLException {
        String SQL = "INSERT INTO HelbredsJournal(borgerID, problemID, technicalNote, relevans, currentEval, expectedCondition, observationNote, [Date])\n" +
                "SELECT ?, problemID, technicalNote, relevans, currentEval, expectedCondition, observationNote, [Date]\n" +
                "FROM Helbredsjournal\n" +
                "WHERE borgerID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setInt(1, newID);
        preparedStatement.setInt(2, oldId);
        preparedStatement.execute();
    }

    private void createFunkTilCopyCase(int oldId, Connection connection, int newID) throws SQLException {
        String sql = "INSERT INTO FunktionsJournal(borgerID, problemID, nuVurdering, målVurdering, technicalNote, execution, importanceOfexecution, goalNote, [date], obsNote)\n" +
                "SELECT ?, problemID, nuVurdering, målVurdering, technicalNote, execution, importanceOfexecution, goalNote, [date], obsNote\n" +
                "FROM FunktionsJournal\n" +
                "WHERE borgerID = ?\n";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, newID);
        preparedStatement.setInt(2, oldId);
        preparedStatement.execute();
    }

    //TODO gør så den scaler
    private void createGenInfoCopyCase(int oldId, Connection connection, int newID) throws SQLException {
        String sql = "INSERT INTO Generelinfo(Mestring, Motivation, Ressourcer, Roller, Vaner, Uddannelse_og_job, Livshistorie, Netværk, Helbredsoplysninger, Hjælpemidler, Boligens_indretning, borgerID)\n" +
                "SELECT Mestring, Motivation, Ressourcer, Roller, Vaner, Uddannelse_og_job, Livshistorie, Netværk, Helbredsoplysninger, Hjælpemidler, Boligens_indretning, ? \n"
                + "FROM Generelinfo \n"
                + "WHERE borgerID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, newID);
        preparedStatement.setInt(2, oldId);
        preparedStatement.execute();
    }

    //TODO Test om jeg virker
    @Override
    public void saveCitizen(Citizen citizen, java.sql.Date newDate, Map<Integer, FunkResult> funkMap, Map<Integer, HealthResult> healthMap, Map<String, String> genInfoMap) throws SQLException {
        try (Connection connection = dc.getConnection()) {
            saveFunk(connection, newDate, funkMap, citizen);
            saveHelbred(connection, newDate, healthMap, citizen);
            saveGenInfo(connection, genInfoMap, citizen);
        }
    }


    private void saveFunk(Connection connection, java.sql.Date newDate, Map<Integer, FunkResult> funkMap, Citizen citizen) throws SQLException {
        String sql = "insert into FunktionsJournal (borgerid, problemID, nuVurdering, målvurdering, technicalNote, execution, importanceOfExecution, goalNote,date,obsNote) " +
                "values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int key : funkMap.keySet()) {
            FunkResult funkResult = funkMap.get(key);
            preparedStatement.setInt(1, citizen.getId());
            preparedStatement.setInt(2, key);
            preparedStatement.setInt(3, funkResult.getCurrent());
            preparedStatement.setInt(4, funkResult.getTarget());
            preparedStatement.setString(5, funkResult.getTechnical());
            preparedStatement.setInt(6, funkResult.getExecution());
            preparedStatement.setInt(7, funkResult.getImportance());
            preparedStatement.setString(8, funkResult.getCitizenString());
            preparedStatement.setDate(9, newDate);
            preparedStatement.setString(10, funkResult.getObservation());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    private void saveHelbred(Connection connection, java.sql.Date newDate, Map<Integer, HealthResult> healthMap, Citizen citizen) throws SQLException {
        String sql = "insert into Helbredsjournal(borgerID,problemID,technicalNote,relevans,currentEval,expectedCondition,ObservationNote,Date) values(?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int key : healthMap.keySet()) {
            HealthResult healthResult = healthMap.get(key);
            preparedStatement.setInt(1, citizen.getId());
            preparedStatement.setInt(2, key);
            preparedStatement.setString(3, healthResult.getTechnical());
            preparedStatement.setInt(4, healthResult.getToggleId());
            preparedStatement.setString(5, healthResult.getCurrent());
            preparedStatement.setInt(6, healthResult.getExpectedIndex());
            preparedStatement.setString(7, healthResult.getObservation());
            preparedStatement.setDate(8, newDate);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    private void saveGenInfo(Connection connection, Map<String, String> genInfoMap, Citizen citizen) throws SQLException {
        String genFields = genInfoMap.keySet().toString().replace(" ", "").replace("[", "").replace("]", "");
        StringBuilder sb = new StringBuilder();
        for (String s : genFields.split(",")) {
            sb.append(s + "=?,");
        }
        String genMarks = sb.deleteCharAt(sb.length() - 1).toString();
        String sql = "UPDATE GenerelInfo SET " + genMarks + " where borgerID=?,";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int index = 2;
        preparedStatement.setInt(1, citizen.getId());
        for (String s : genInfoMap.values()) {
            preparedStatement.setString(index++, s);
        }
        preparedStatement.execute();
    }

    @Override
    public Collection<Date> getLogDates(int id) {
        List<Date> list = new ArrayList<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "select DISTINCT [date] from FunktionsJournal where borgerID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getDate("date"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    @Override
    public Map<Integer, HealthResult> loadHealthInfoFromDate(int id, Date date) throws SQLException {
        Map<Integer, HealthResult> resultMap = new LinkedHashMap<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "select [problemID]" +
                    "      ,[technicalNote]" +
                    "      ,[relevans]" +
                    "      ,[currentEval]" +
                    "      ,[expectedCondition]" +
                    "      ,[observationNote]" +
                    "from Helbredsjournal where borgerId = ? and date = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setDate(2,date);

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
            return resultMap;
        }
    }

    @Override
    public Map<Integer, FunkResult> loadFunkInfoFromDate(int id, Date date) {
        Map<Integer, FunkResult> resultMap = new LinkedHashMap<>();
        try (Connection connection = dc.getConnection()) {
            String sql = "Select [problemID]\n" +
                    "      ,[nuVurdering]\n" +
                    "      ,[målVurdering]\n" +
                    "      ,[technicalNote]\n" +
                    "      ,[execution]\n" +
                    "      ,[importanceOfExecution]\n" +
                    "      ,[goalNote]\n" +
                    "      ,[obsNote] from funktionsjournal where borgerid = ? and Date = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setDate(2,date);
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
}
