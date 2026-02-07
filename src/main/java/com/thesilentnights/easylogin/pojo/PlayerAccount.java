package com.thesilentnights.easylogin.pojo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class PlayerAccount {

    private final UUID uuid;
    private String username;
    private String password;
    private String lastLoginIp;
    private double lastLoginX;
    private double lastLoginY;
    private double lastLoginZ;
    private String lastLoginWorld;
    private String email;
    private long loginTimestamp;

    public PlayerAccount(UUID uuid, String username, String password, String lastLoginIp,
                         double lastLoginX, double lastLoginY, double lastLoginZ,
                         String lastLoginWorld, String email, long loginTimestamp) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.lastLoginIp = lastLoginIp;
        this.lastLoginX = lastLoginX;
        this.lastLoginY = lastLoginY;
        this.lastLoginZ = lastLoginZ;
        this.lastLoginWorld = lastLoginWorld;
        this.email = email;
        this.loginTimestamp = loginTimestamp;
    }

    public static PlayerAccount fromResultSet(ResultSet set) throws SQLException {
        String uuidColumn = SqlColumnDefinition.UUID.toString().toLowerCase(Locale.getDefault());
        if (set.getString(uuidColumn) == null) {
            return null;
        }

        return new PlayerAccount(
                UUID.fromString(set.getString(uuidColumn)),
                set.getString(SqlColumnDefinition.USERNAME.toString().toLowerCase(Locale.getDefault())),
                set.getString(SqlColumnDefinition.PASSWORD.toString().toLowerCase(Locale.getDefault())),
                set.getString(SqlColumnDefinition.LASTLOGIN_IP.toString().toLowerCase(Locale.getDefault())),
                set.getDouble(SqlColumnDefinition.LASTLOGIN_X.toString().toLowerCase(Locale.getDefault())),
                set.getDouble(SqlColumnDefinition.LASTLOGIN_Y.toString().toLowerCase(Locale.getDefault())),
                set.getDouble(SqlColumnDefinition.LASTLOGIN_Z.toString().toLowerCase(Locale.getDefault())),
                set.getString(SqlColumnDefinition.LASTLOGIN_WORLD.toString().toLowerCase(Locale.getDefault())),
                set.getString(SqlColumnDefinition.EMAIL.toString().toLowerCase(Locale.getDefault())),
                set.getLong(SqlColumnDefinition.LOGIN_TIMESTAMP.toString().toLowerCase(Locale.getDefault()))
        );
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public double getLastLoginX() {
        return lastLoginX;
    }

    public void setLastLoginX(double lastLoginX) {
        this.lastLoginX = lastLoginX;
    }

    public double getLastLoginY() {
        return lastLoginY;
    }

    public void setLastLoginY(double lastLoginY) {
        this.lastLoginY = lastLoginY;
    }

    public double getLastLoginZ() {
        return lastLoginZ;
    }

    public void setLastLoginZ(double lastLoginZ) {
        this.lastLoginZ = lastLoginZ;
    }

    public String getLastLoginWorld() {
        return lastLoginWorld;
    }

    public void setLastLoginWorld(String lastLoginWorld) {
        this.lastLoginWorld = lastLoginWorld;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(long loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerAccount that = (PlayerAccount) o;
        return Double.compare(that.lastLoginX, lastLoginX) == 0 &&
                Double.compare(that.lastLoginY, lastLoginY) == 0 &&
                Double.compare(that.lastLoginZ, lastLoginZ) == 0 &&
                loginTimestamp == that.loginTimestamp &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(lastLoginIp, that.lastLoginIp) &&
                Objects.equals(lastLoginWorld, that.lastLoginWorld) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username, password, lastLoginIp, lastLoginX, lastLoginY, lastLoginZ, lastLoginWorld, email, loginTimestamp);
    }

    @Override
    public String toString() {
        return "PlayerAccount{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", lastLoginX=" + lastLoginX +
                ", lastLoginY=" + lastLoginY +
                ", lastLoginZ=" + lastLoginZ +
                ", lastLoginWorld='" + lastLoginWorld + '\'' +
                ", email='" + email + '\'' +
                ", loginTimestamp=" + loginTimestamp +
                '}';
    }
}