package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.be.User;
import dk.easv.dal.interfaces.ICitizienDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


}
