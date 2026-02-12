package com.thesilentnights.easylogin.sql;

import com.thesilentnights.easylogin.pojo.PlayerAccount;
import com.thesilentnights.easylogin.repo.CommonStaticRepo;
import com.thesilentnights.easylogin.utils.LogUtil;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class DataSource {

    private final HikariDataSource dataSource;

    public DataSource(Supplier<HikariDataSource> dataSourceSupplier) {
        this.dataSource = dataSourceSupplier.get();
    }

    public Optional<PlayerAccount> getAuthByName(String name) {
        String sql = "SELECT * FROM " + CommonStaticRepo.TABLE_NAME + " WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return Optional.ofNullable(PlayerAccount.fromResultSet(rs));
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<PlayerAccount> getAuthByUUID(UUID uuid) {
        String sql = "SELECT * FROM " + CommonStaticRepo.TABLE_NAME + " WHERE uuid = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                return Optional.ofNullable(PlayerAccount.fromResultSet(rs));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
    }


    public boolean updatePassword(String value, UUID uuid) {
        String sql = "UPDATE " + CommonStaticRepo.TABLE_NAME +
                " SET password = ? WHERE uuid = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, value);
            stmt.setString(2, uuid.toString());
            int rowsAffected = stmt.executeUpdate();
            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LogUtil.logError(DataSource.class, "sqlerror in updateColumn", e);
            return false;
        }
    }

    public boolean updateAccount(PlayerAccount account) {
        String sql = "UPDATE " + CommonStaticRepo.TABLE_NAME +
                " SET password = ?, lastlogin_x = ?, lastlogin_y = ?, lastlogin_z = ?, " +
                "lastlogin_ip = ?, lastlogin_world = ?, username = ?, email = ?, login_timestamp = ? " +
                "WHERE uuid = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getPassword());
            stmt.setDouble(2, account.getLastLoginX());
            stmt.setDouble(3, account.getLastLoginY());
            stmt.setDouble(4, account.getLastLoginZ());
            stmt.setString(5, account.getLastLoginIp());
            stmt.setString(6, account.getLastLoginWorld());
            stmt.setString(7, account.getUsername());
            stmt.setString(8, account.getEmail());
            stmt.setLong(9, account.getLoginTimestamp());
            stmt.setString(10, account.getUuid().toString());

            int updated = stmt.executeUpdate();
            conn.commit();
            return updated > 0;
        } catch (SQLException e) {
            LogUtil.logError(DataSource.class, "sqlerror in updateAccount", e);
            return false;
        }
    }

    public boolean insertAccount(PlayerAccount account) {
        String sql = "INSERT INTO " + CommonStaticRepo.TABLE_NAME +
                " (uuid, password, lastlogin_x, lastlogin_y, lastlogin_z, lastlogin_ip, lastlogin_world, username, email, login_timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, account.getUuid().toString());
            stmt.setString(2, account.getPassword());
            stmt.setDouble(3, account.getLastLoginX());
            stmt.setDouble(4, account.getLastLoginY());
            stmt.setDouble(5, account.getLastLoginZ());
            stmt.setString(6, account.getLastLoginIp());
            stmt.setString(7, account.getLastLoginWorld());
            stmt.setString(8, account.getUsername());
            stmt.setString(9, account.getEmail());
            stmt.setLong(10, account.getLoginTimestamp());

            int updated = stmt.executeUpdate();
            conn.commit();
            return updated > 0;
        } catch (SQLException e) {
            LogUtil.logError(DataSource.class, "sqlerror in insertAccount", e);
            return false;
        }
    }

    public boolean removeAuth(UUID uuid) {
        // TODO: 实现删除逻辑（当前返回 false）
        return false;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}