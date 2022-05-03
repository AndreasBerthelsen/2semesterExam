package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;

import java.sql.SQLException;

public interface ILoginDAO {

    User loginUser(String username, String password) throws SQLException;

    void createUser(String username, String hashedPassword, String salt) throws SQLException;
}