package cn.thesilentnights.easylogin.data.connections;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteConnectionProvider implements ConnectionProvider {

    private Connection connection;

    @Override
    public void init(String connectionString) throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite:" + connectionString);
    }

    @Override
    public Connection getConnection() throws Exception {
        return connection;
    }

    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
