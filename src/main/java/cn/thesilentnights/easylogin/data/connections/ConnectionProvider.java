package cn.thesilentnights.easylogin.data.connections;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {
        Connection getConnection() throws SQLException;

        void close() throws SQLException;
}
