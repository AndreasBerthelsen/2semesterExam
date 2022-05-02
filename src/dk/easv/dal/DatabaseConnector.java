package dk.easv.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

    private static final String PROP_FILE = "src/database.txt";
    private SQLServerDataSource ds;


    /**
     * Logger ind på vores databse ved brug af informationerne omkring
     * vores login i et .txt fil
     * @throws IOException
     */
    public DatabaseConnector() throws IOException
    {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(PROP_FILE));
        ds = new SQLServerDataSource();
        ds.setServerName(databaseProperties.getProperty("Server"));
        ds.setDatabaseName(databaseProperties.getProperty("Database"));
        ds.setUser(databaseProperties.getProperty("User"));
        ds.setPassword(databaseProperties.getProperty("Password"));
    }

    /**
     * Danner en forbindelse med vores database
     * @return forbindelsen
     * @throws SQLServerException
     */
    public Connection getConnection() throws SQLServerException {
        return ds.getConnection();
    }

    public static void main(String[] args) throws SQLException, IOException {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        Connection connection = databaseConnector.getConnection();

        System.out.println("Did it open? " + !connection.isClosed());
        connection.close();
    }
}
