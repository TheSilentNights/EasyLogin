package cn.thesilentnights.easylogin.data.serializers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import cn.thesilentnights.easylogin.data.SqlGenerator;
import cn.thesilentnights.easylogin.data.connections.ConnectionProvider;
import cn.thesilentnights.easylogin.pojo.PlayerExtraData;

public class ExtraDataSerializer implements DataSerializer<PlayerExtraData> {

    private static final String TABLE = "extra_data";
    private static final String PK = "uuid";

    private static final LinkedHashMap<String, String> COLUMNS = new LinkedHashMap<>();
    private static final List<String> COLUMN_NAMES;
    private static final List<String> SELECT_COLUMNS;

    static {
        COLUMNS.put("uuid", "TEXT PRIMARY KEY");
        COLUMNS.put("display_name", "TEXT");
        COLUMNS.put("last_login_x", "REAL");
        COLUMNS.put("last_login_y", "REAL");
        COLUMNS.put("last_login_z", "REAL");
        COLUMNS.put("last_login_dimension", "TEXT");
        COLUMNS.put("last_login_timestamp", "INTEGER");
        COLUMNS.put("last_login_ip", "TEXT");
        COLUMN_NAMES = List.copyOf(COLUMNS.keySet());
        SELECT_COLUMNS = COLUMN_NAMES.subList(1, COLUMN_NAMES.size());
    }

    private ConnectionProvider provider;

    @Override
    public void init(ConnectionProvider provider) throws Exception {
        this.provider = provider;
        try (Connection conn = provider.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(SqlGenerator.createTable(TABLE, COLUMNS));
        }
    }

    @Override
    public PlayerExtraData get(String uuid) throws Exception {
        String sql = SqlGenerator.select(TABLE, SELECT_COLUMNS, PK);
        try (Connection conn = provider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    String displayName = rs.getString("display_name");
                    double x = rs.getDouble("last_login_x");
                    double y = rs.getDouble("last_login_y");
                    double z = rs.getDouble("last_login_z");
                    String dimension = rs.getString("last_login_dimension");
                    long timestamp = rs.getLong("last_login_timestamp");
                    Long lastLoginTimestamp = rs.wasNull() ? null : timestamp;
                    String lastLoginIp = rs.getString("last_login_ip");

                    return new PlayerExtraData(UUID.fromString(uuid), displayName, x, y, z, dimension, lastLoginTimestamp, lastLoginIp);
                }
            }
        }
        return null;
    }

    @Override
    public void save(PlayerExtraData data) throws Exception {
        String sql = SqlGenerator.updateOrInsert(TABLE, COLUMN_NAMES, PK);
        try (
                Connection conn = provider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            //inject value
            ps.setString(1, data.getUuid().toString());
            ps.setString(2, data.getDisplayName());
            ps.setDouble(3, data.getLastLoginX());
            ps.setDouble(4, data.getLastLoginY());
            ps.setDouble(5, data.getLastLoginZ());
            ps.setString(6, data.getLastLoginDimension());
            if (data.getLastLoginTimestamp() != null) {
                ps.setLong(7, data.getLastLoginTimestamp());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            ps.setString(8, data.getLastLoginIp());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String uuid) throws Exception {
        String sql = SqlGenerator.delete(TABLE, PK);
        try (Connection conn = provider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        }
    }
}
