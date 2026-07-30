package cn.thesilentnights.easylogin.data.connections;

import java.sql.Connection;

public interface ConnectionProvider {
        void init(String connectionString) throws Exception;

        Connection getConnection() throws Exception;

        void close() throws Exception;
}
