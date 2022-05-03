package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.be.UserType;
import dk.easv.dal.interfaces.ILoginDAO;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO implements ILoginDAO {
    private DatabaseConnector dc;

    public LoginDAO() throws IOException {
        dc = new DatabaseConnector();
    }

    public User loginUser(String username, String password) throws SQLException {
        try (Connection connection = dc.getConnection()) {
            String sql = "SELECT userID , fname, lname, username, type, password\n" +
                    "FROM [User] INNER JOIN [Role] ON [User].RoleID = [Role].roleID\n" +
                    "WHERE username = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserType type = UserType.valueOf(resultSet.getString("type"));
                int id = resultSet.getInt("userID");
                String hashed = resultSet.getString("password");
                String firstname = resultSet.getString("fname");
                String lastname = resultSet.getString("lname");
                String loginName = resultSet.getString("username");

                if (BCrypt.checkpw(password, hashed)) {
                    return new User(id, firstname, lastname, loginName, type);
                } else {
                    System.out.println("Big bummer");
                }

                }
            }
            return null;
        }
    /**
     * Ikke færdig, mangler input til navn og rollefordeling OG POTENTIELT ALT MULIGT ANDET SOM SKOLE OG KLASSE
     * @param username
     * @param hashedPassword
     */

    @Override
    public void createUser(String firstName,String lastName,String username, String hashedPassword, String salt, UserType userType) {
        try (Connection connection = dc.getConnection()){
            String sql = "INSERT INTO [USER] (fname, lname, username, password, roleID, salt) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, hashedPassword);
            preparedStatement.setInt(5, userType.getI());
            preparedStatement.setString(6, salt);
            preparedStatement.execute();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
