package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.dal.interfaces.ICitizienDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

            String sql = "SELECT borgerID, fName, lName, dato\n" +
                    "FROM Borger WHERE isTemplate = 1";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("borgerID");
                String fname = resultSet.getString("fName");
                String lName = resultSet.getString("lName");
                Date date = resultSet.getDate("dato");
                allTemp.add(new Citizen(id, fname, lName, date));
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
            String citizenSQL = "SELECT borgerID, fName, lName, dato FROM Borger\n" +
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
                citizensFromUser.add(new Citizen(id, fName, lName, dato));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return citizensFromUser;
    }

    @Override
    public void createCopyCitizen(Citizen citizen) {
        try(Connection connection = dc.getConnection()) {
            int newID = createCitizenToCopy(citizen, connection);
            int oldID = citizen.getId();
            createFunkTilCopy(oldID, connection, newID);
            createHelbTilCopy(oldID, connection, newID);
            createGenInfoCopy(oldID, connection, newID);
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

    private void createHelbTilCopy(int oldId,  Connection connection, int newID) throws SQLException {
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
        String sql = "INSERT INTO Generelinfo(Mestring, Motivation, Ressourcer, Roller, Vaner, Uddannelse_og_job, Livshistorie, Netvaerk, Helbredsoplysninger, Hjaelpemidler, Bolig, borgerID)\n"+
                "SELECT Mestring, Motivation, Ressourcer, Roller, Vaner, Uddannelse_og_job, Livshistorie, Netvaerk, Helbredsoplysninger, Hjaelpemidler, Bolig, ? \n"
                + "FROM Generelinfo \n"
                + "WHERE borgerID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, newID);
        preparedStatement.setInt(2, oldId);
        preparedStatement.execute();

    }

}
