package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.be.UserType;

import java.sql.SQLException;

public interface ILoginDAO {

    User loginUser(String username, String password) throws SQLException;

    void createUser(String firstName, String lastName, String username, String hashedPassword, String salt, UserType userType) throws SQLException;
}