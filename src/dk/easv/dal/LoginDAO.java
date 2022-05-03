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
           String sql = "SELECT userID , fname, lname, username, roleID, password\n" +
                   "FROM [User]\n" +
                   "WHERE username = ? ";
           PreparedStatement preparedStatement = connection.prepareStatement(sql);
           preparedStatement.setString(1, username);
           ResultSet resultSet = preparedStatement.executeQuery();
           while (resultSet.next()){
               String hashed =resultSet.getString("password");

               if (BCrypt.checkpw(password, hashed)){
                   System.out.println("hurra");
               }
               else{
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
    public void createUser(String username, String hashedPassword, String salt) {
        try (Connection connection = dc.getConnection()){
            String sql = "INSERT INTO [USER] (fname, lname, username, password, roleID, salt) VALUES ('hEJSA','Hejsa',?,?,1,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, salt);
            preparedStatement.execute();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
