package cn.thesilentnights.easylogin.data.serializers;

import cn.thesilentnights.easylogin.data.SqlGenerator;
import cn.thesilentnights.easylogin.data.connections.ConnectionProvider;
import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class PasswordDataSerializer implements DataSerializer<PlayerPasswordData> {

        private static final String TABLE = "passwords";
        private static final String PK = "uuid";

        private static final LinkedHashMap<String, String> COLUMNS = new LinkedHashMap<>();
        private static final List<String> COLUMN_NAMES;
        private static final List<String> SELECT_COLUMNS;

        static {
                COLUMNS.put("uuid", "TEXT PRIMARY KEY");
                COLUMNS.put("password", "TEXT NOT NULL");
                COLUMN_NAMES = List.copyOf(COLUMNS.keySet());
                SELECT_COLUMNS = List.of("password");
        }

        private ConnectionProvider provider;

        @Override
        public void init(ConnectionProvider provider) throws Exception {
                this.provider = provider;
                Connection conn = provider.getConnection();
                try (Statement stmt = conn.createStatement()) {
                        stmt.execute(SqlGenerator.createTable(TABLE, COLUMNS));
                }
        }

        @Override
        public PlayerPasswordData get(String uuid) throws Exception {
                String sql = SqlGenerator.select(TABLE, SELECT_COLUMNS, PK);
                Connection conn = provider.getConnection();
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, uuid);
                        try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                        String password = rs.getString("password");
                                        return new PlayerPasswordData(UUID.fromString(uuid), password);
                                }
                        }
                }
                return null;
        }

        @Override
        public void save(PlayerPasswordData data) throws Exception {
                String sql = SqlGenerator.updateOrInsert(TABLE, COLUMN_NAMES, PK);
                Connection conn = provider.getConnection();
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, data.getUuid().toString());
                        ps.setString(2, data.getPassword());
                        ps.executeUpdate();
                }
        }

        @Override
        public void delete(String uuid) throws Exception {
                String sql = SqlGenerator.delete(TABLE, PK);
                Connection conn = provider.getConnection();
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, uuid);
                        ps.executeUpdate();
                }
        }
}
