package dk.easv.dal.interfaces;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.be.User;
import dk.easv.be.UserType;

import java.sql.SQLException;

public interface ILoginDAO {
    /**
     * logger en bruger ind
     * @param username
     * @param password
     * @return et user objekt hvis der kan findes et match af username og password i databasen
     * @throws SQLException
     */
    User loginUser(String username, String password) throws SQLException;
}