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
                    "FROM Borger";

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
            int id = createCitizenToCopy(citizen, connection);
            createFunkTilCopy(id, connection);
            createHelbTilCopy(id, connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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


    private void createHelbTilCopy(int id, Connection connection) throws SQLException {
        String SQL = "INSERT INTO HelbredsJournal(problemID, value, relevans)\n" +
                "SELECT problemID, [value], relevans\n" +
                "FROM Helbredsjournal\n" +
                "WHERE borgerID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }


    private void createFunkTilCopy(int id, Connection connection) throws SQLException {
        String sql = "INSERT INTO FunktionsJournal(problemID, nuVurdering, målVurdering, note)\n" +
                "SELECT problemID, nuVurdering, målVurdering, note\n" +
                "FROM FunktionsJournal\n" +
                "WHERE borgerID = ?\n";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

}
