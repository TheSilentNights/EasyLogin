package com.thesilentnights.easylogin.sql;

import cn.hutool.core.io.resource.ResourceUtil;
import com.thesilentnights.easylogin.pojo.SqlColumnDefinition;
import com.thesilentnights.easylogin.repo.CommonStaticRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class DatabaseChecker {

    private final DataSource provider;
    private final Logger log = LogManager.getLogger(DatabaseChecker.class);

    public DatabaseChecker(DataSource provider) {
        this.provider = provider;
    }

    // Companion object methods as static
    private static void checkColumns(Connection connection) {
        try (var statement = connection.createStatement()) {
            DatabaseMetaData metaData = connection.getMetaData();

            if (isColumnMissing(metaData, SqlColumnDefinition.LASTLOGIN_IP.toString().toLowerCase(Locale.getDefault()))) {
                statement.executeUpdate(
                        "alter table " + CommonStaticRepo.TABLE_NAME + " add column " +
                                SqlColumnDefinition.LASTLOGIN_IP.toString().toLowerCase(Locale.getDefault()) + " varchar(255);"
                );
            }

            if (isColumnMissing(metaData, SqlColumnDefinition.LASTLOGIN_X.toString().toLowerCase(Locale.getDefault()))) {
                statement.executeUpdate(
                        "alter table " + CommonStaticRepo.TABLE_NAME + " add column " +
                                SqlColumnDefinition.LASTLOGIN_X.toString().toLowerCase(Locale.getDefault()) + " varchar(255);"
                );
            }

            if (isColumnMissing(metaData, SqlColumnDefinition.LASTLOGIN_Y.toString().toLowerCase(Locale.getDefault()))) {
                statement.executeUpdate(
                        "alter table " + CommonStaticRepo.TABLE_NAME + " add column " +
                                SqlColumnDefinition.LASTLOGIN_Y.toString().toLowerCase(Locale.getDefault()) + " varchar(255);"
                );
            }

            if (isColumnMissing(metaData, SqlColumnDefinition.LASTLOGIN_Z.toString().toLowerCase(Locale.getDefault()))) {
                statement.executeUpdate(
                        "alter table " + CommonStaticRepo.TABLE_NAME + " add column " +
                                SqlColumnDefinition.LASTLOGIN_Z.toString().toLowerCase(Locale.getDefault()) + " varchar(255);"
                );
            }

            if (isColumnMissing(metaData, SqlColumnDefinition.LASTLOGIN_WORLD.toString().toLowerCase(Locale.getDefault()))) {
                statement.executeUpdate(
                        "alter table " + CommonStaticRepo.TABLE_NAME + " add column " +
                                SqlColumnDefinition.LASTLOGIN_WORLD.toString().toLowerCase(Locale.getDefault()) + " varchar(255);"
                );
            }

            if (isColumnMissing(metaData, SqlColumnDefinition.LOGIN_TIMESTAMP.toString().toLowerCase(Locale.getDefault()))) {
                statement.executeUpdate(
                        "alter table " + CommonStaticRepo.TABLE_NAME + " add column " +
                                SqlColumnDefinition.LOGIN_TIMESTAMP.toString().toLowerCase(Locale.getDefault()) + " varchar(255);"
                );
            }

            if (isColumnMissing(metaData, SqlColumnDefinition.EMAIL.toString().toLowerCase(Locale.getDefault()))) {
                statement.executeUpdate(
                        "alter table " + CommonStaticRepo.TABLE_NAME + " add column " +
                                SqlColumnDefinition.EMAIL.toString().toLowerCase(Locale.getDefault()) + " varchar(255);"
                );
            }

            if (isColumnMissing(metaData, SqlColumnDefinition.USERNAME.toString().toLowerCase(Locale.getDefault()))) {
                statement.executeUpdate(
                        "alter table " + CommonStaticRepo.TABLE_NAME + " add column " +
                                SqlColumnDefinition.USERNAME.toString().toLowerCase(Locale.getDefault()) + " varchar(255);"
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isColumnMissing(DatabaseMetaData metaData, String columnName) throws SQLException {
        try (ResultSet columns = metaData.getColumns(null, null, CommonStaticRepo.TABLE_NAME, columnName)) {
            return !columns.next();
        }
    }

    public void checkDatabase() {
        try (Connection connection = provider.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, CommonStaticRepo.TABLE_NAME, new String[]{"TABLE"})) {
                if (!tables.next()) {
                    createTable(connection);
                }
            }
            checkColumns(connection);
            connection.commit();
        } catch (SQLException e) {
            log.error("error checking data base", e);
        }
    }

    private void createTable(Connection connection) {
        String createTableSQL = null;
        try {
            createTableSQL = Files.readString(Path.of(ResourceUtil.getResource("sql.table_create.sql").getPath()));
        } catch (IOException e) {
            log.error("error creating table", e);
            return;
        }

        try (var stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        log.info("accounts created");
    }
}