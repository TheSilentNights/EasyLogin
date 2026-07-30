package cn.thesilentnights.easylogin.data.serializers;

import cn.thesilentnights.easylogin.data.connections.ConnectionProvider;

public interface DataSerializer<T> {
        void init(ConnectionProvider provider) throws Exception;

        T get(String uuid) throws Exception;

        void save(T data) throws Exception;

        void delete(String uuid) throws Exception;
}
