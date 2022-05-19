package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import dk.easv.be.Citizen;
import dk.easv.be.School;

import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.dal.interfaces.IUserDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    private DatabaseConnector dc;


    public UserDAO() throws IOException {
        dc = new DatabaseConnector();
    }

    /**
     * Ikke færdig, mangler input til navn og rollefordeling OG POTENTIELT ALT MULIGT ANDET SOM SKOLE OG KLASSE
     *
     * @param username
     * @param hashedPassword
     */

    @Override
    public void createUser(String firstName, String lastName, String username, String hashedPassword, String salt, UserType userType, int schoolID) throws SQLException {
            try (Connection connection = dc.getConnection()) {
                String sql = "INSERT INTO [USER] (fname, lname, username, password, roleID, salt, skole) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, hashedPassword);
                preparedStatement.setInt(5, userType.getI());
                preparedStatement.setString(6, salt);
                preparedStatement.setInt(7, schoolID);
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }


    @Override
    public List<User> getAllUsers(UserType userType) throws SQLServerException {
        ArrayList<User> allUsers = new ArrayList<>();
        try(Connection connection = dc.getConnection()) {
            String sql = "SELECT [User].userID, [User].fName, [User].lName, [User].username, [User].skole \n" +
                    "FROM [User]\n" +
                    "INNER JOIN [Role] ON [User].roleID = [Role].roleID WHERE [User].roleID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userType.getI());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("userID");
                String fName = resultSet.getString("fName");
                String lName = resultSet.getString("lName");
                String username = resultSet.getString("username");
                int schoolID = resultSet.getInt("skole");
                allUsers.add(new User(id, fName, lName, username, userType, schoolID));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allUsers;
    }

    public List<User> getAllUsersFromSchools(School school, UserType userType) {
       List<User> allUsers = new ArrayList<>();
       try(Connection con = dc.getConnection()) {
           String sql = "SELECT userID, fName, lName, username\n" +
                   "From [User] \n" +
                   "INNER JOIN [Role] on [User].[roleID] = [Role].roleID\n" +
                   "INNER JOIN Skole on [User].[skole] = Skole.ID\n" +
                   "WHERE [User].[skole] = ? AND [User].roleID = ?";
           PreparedStatement preparedStatement = con.prepareStatement(sql);
           preparedStatement.setInt(1, school.getId());
           preparedStatement.setInt(2, userType.getI());
           ResultSet resultSet = preparedStatement.executeQuery();
           while (resultSet.next()) {
               int id = resultSet.getInt("userID");
               String fName = resultSet.getString("fname");
               String lName = resultSet.getString("lName");
               String username = resultSet.getString("username");
               allUsers.add(new User(id, fName, lName, username, userType));
           }

       } catch (SQLException e) {
           e.printStackTrace();
       }
    return allUsers;
    }

    @Override
    public List<School> getAllSchools() throws SQLServerException {
        List<School> allSchools = new ArrayList<>();
        try(Connection connection = dc.getConnection()) {
            String sql ="SELECT Skole.ID, Skole.Navn\n" +
                    "FROM dbo.Skole\n";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String navn = resultSet.getString("navn");
                allSchools.add(new School(id, navn));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allSchools;
    }

    @Override
    public void deleteUser(User userToBeDeleted) {
        try (Connection connection = dc.getConnection()) {
            String sql = "DELETE FROM [User] WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userToBeDeleted.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try (Connection connection = dc.getConnection()) {
            String sql = "UPDATE [User] SET fName = ?, lName =?, username=? WHERE userID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updatePassword(User user, String hashPassword, String salt) throws SQLServerException {
        try(Connection con = dc.getConnection()) {
            String sql = "UPDATE [User] SET password = ?, salt = ? WHERE userID=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, hashPassword);
            preparedStatement.setString(2, salt);
            preparedStatement.setInt(3,user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUsername(String username) {
        try(Connection connection = dc.getConnection()) {
            String sql = "SELECT * FROM [User] WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }
}
