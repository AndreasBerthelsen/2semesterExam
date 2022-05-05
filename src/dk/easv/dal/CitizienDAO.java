package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.Citizen;
import dk.easv.dal.interfaces.ICitizienDAO;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
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
        try (Connection connection = dc.getConnection()){
            String sql = "SELECT * FROM Borger";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            while (resultSet.next()){
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
            preparedStatement.setDate(3, birthDate);;
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
