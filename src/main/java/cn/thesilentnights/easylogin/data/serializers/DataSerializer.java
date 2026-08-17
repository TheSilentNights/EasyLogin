package cn.thesilentnights.easylogin.data.serializers;

import java.sql.SQLException;

public interface DataSerializer<T> {
        T get(String uuid) throws SQLException;

        void save(T data) throws SQLException;

        void delete(String uuid) throws SQLException;
}
