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
            String sql = "Select userID, username, password, type, [fName], [lName] \n" +
                    "                    from [User] INNER JOIN [Role] ON [User].RoleID = [Role].roleID where username = ? and password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserType type = UserType.valueOf(rs.getString("type"));
                int id = rs.getInt("userID");
                String nameOfUser = rs.getString("username");
                String firstname = rs.getString("fName");
                String lastname = rs.getString("lName");
                String pass = rs.getString("password");
                switch (type) {
                    case TEACHER, STUDENT -> {
                        return new User(id, firstname, lastname,nameOfUser,pass,type);
                    }
                }
            }
        }
        return null;
    }
}
