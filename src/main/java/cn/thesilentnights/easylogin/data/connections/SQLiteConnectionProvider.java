package cn.thesilentnights.easylogin.data.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnectionProvider implements ConnectionProvider {

        private Connection connection;

        public SQLiteConnectionProvider(String connectionString) throws SQLException, ClassNotFoundException {
                init(connectionString);
        }

        private void init(String connectionString) throws ClassNotFoundException, SQLException {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + connectionString);
        }

        @Override
        public Connection getConnection() throws SQLException {
                return connection;
        }

        @Override
        public void close() throws SQLException {
                if (connection != null && !connection.isClosed()) {
                        connection.close();
                }
        }
}
