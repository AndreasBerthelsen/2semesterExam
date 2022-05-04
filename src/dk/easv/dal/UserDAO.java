package dk.easv.dal;

import dk.easv.be.UserType;
import dk.easv.dal.interfaces.IUserDAO;
import dk.easv.dal.interfaces.IUserDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO implements IUserDAO {
    private DatabaseConnector dc;


    public UserDAO() throws IOException {
        dc = new DatabaseConnector();
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
