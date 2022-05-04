package dk.easv.dal.interfaces;

import dk.easv.be.UserType;

import java.sql.SQLException;

public interface IUserDAO {
    void createUser(String firstName, String lastName, String username, String hashedPassword, String salt, UserType userType) throws SQLException;

}
