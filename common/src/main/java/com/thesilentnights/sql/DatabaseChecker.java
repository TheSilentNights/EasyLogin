package com.thesilentnights.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseChecker {

    private final Connection connection;

    public DatabaseChecker(Connection connection) {
        this.connection = connection;
    }

    /**
     * 检查并修复accounts表结构
     */
    public boolean checkAndRepairTable() {
        try {
            // 检查表是否存在
            if (!tableExists()) {
                System.out.println("accounts表不存在，创建新表...");
                createTable();
                return true;
            } else {
                System.out.println("accounts表已存在，检查表结构...");
                boolean repaired = repairTableStructure();
                return repaired;
            }
        } catch (SQLException e) {
            System.err.println("检查修复表结构时出错: " + e.getMessage());
            return false;
        }
    }

    /**
     * 检查表是否存在
     */
    private boolean tableExists() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "accounts", new String[]{"TABLE"});
        boolean exists = tables.next();
        tables.close();
        return exists;
    }

    /**
     * 创建accounts表
     */
    private void createTable() throws SQLException {
        String createTableSQL =
                "CREATE TABLE accounts (" +
                        "username TEXT PRIMARY KEY NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "token TEXT, " +
                        "lastlogin_x NUMERIC, " +
                        "lastlogin_y NUMERIC, " +
                        "lastlogin_z NUMERIC, " +
                        "lastlogin_world TEXT, " +
                        "uuid TEXT, " +
                        "email TEXT)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
        connection.commit();
        System.out.println("accounts表创建成功");
    }

    /**
     * 修复表结构，添加缺失的列
     */
    private boolean repairTableStructure() throws SQLException {
        Map<String, ColumnDefinition> requiredColumns = new HashMap<>();
        requiredColumns.put("username", new ColumnDefinition("TEXT", true, true));
        requiredColumns.put("password", new ColumnDefinition("TEXT", false, true));
        requiredColumns.put("token", new ColumnDefinition("TEXT", false, false));
        requiredColumns.put("lastlogin_x", new ColumnDefinition("NUMERIC", false, false));
        requiredColumns.put("lastlogin_y", new ColumnDefinition("NUMERIC", false, false));
        requiredColumns.put("lastlogin_z", new ColumnDefinition("NUMERIC", false, false));
        requiredColumns.put("lastlogin_world", new ColumnDefinition("TEXT", false, false));
        requiredColumns.put("uuid", new ColumnDefinition("TEXT", false, false));
        requiredColumns.put("email", new ColumnDefinition("TEXT", false, false));

        // 获取现有列信息
        List<String> existingColumns = getExistingColumns();
        boolean repaired = false;

        // 检查并添加缺失的列
        for (Map.Entry<String, ColumnDefinition> entry : requiredColumns.entrySet()) {
            String columnName = entry.getKey();
            ColumnDefinition columnDef = entry.getValue();

            if (!columnExists(existingColumns, columnName)) {
                System.out.println("添加缺失的列: " + columnName);
                addColumn(columnName, columnDef.getDataType());
                repaired = true;
            }
        }

        // 确保username是主键
        if (!isUsernamePrimaryKey()) {
            System.out.println("修复username主键约束...");
            makeUsernamePrimaryKey();
            repaired = true;
        }

        if (!repaired) {
            System.out.println("表结构完整，无需修复");
        }

        return repaired;
    }

    /**
     * 获取现有列名列表
     */
    private List<String> getExistingColumns() throws SQLException {
        List<String> columns = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();

        try (ResultSet rs = metaData.getColumns(null, null, "accounts", null)) {
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                columns.add(columnName.toLowerCase());
            }
        }
        return columns;
    }

    /**
     * 检查列是否存在
     */
    private boolean columnExists(List<String> existingColumns, String columnName) {
        return existingColumns.contains(columnName.toLowerCase());
    }

    /**
     * 添加列到表中
     */
    private void addColumn(String columnName, String dataType) throws SQLException {
        String alterSQL = "ALTER TABLE accounts ADD COLUMN " + columnName + " " + dataType;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(alterSQL);
        }
        connection.commit();
    }

    /**
     * 检查username是否是主键
     */
    private boolean isUsernamePrimaryKey() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        try (ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, "accounts")) {
            while (primaryKeys.next()) {
                String columnName = primaryKeys.getString("COLUMN_NAME");
                if ("username".equalsIgnoreCase(columnName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将username设置为主键
     */
    private void makeUsernamePrimaryKey() throws SQLException {
        // 这是一个复杂的操作，可能需要重建表
        // 这里简化处理，确保username有唯一约束
        try {
            // 先确保username非空
            String alterSQL = "ALTER TABLE accounts ALTER COLUMN username SET NOT NULL";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(alterSQL);
            }
        } catch (SQLException e) {
            // 如果上述语法不支持，尝试其他方式
            System.out.println("设置非空约束失败: " + e.getMessage());
        }

        // 创建唯一索引
        try {
            String createIndexSQL = "CREATE UNIQUE INDEX idx_username ON accounts(username)";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createIndexSQL);
            }
        } catch (SQLException e) {
            System.out.println("创建唯一索引失败（可能已存在）: " + e.getMessage());
        }

        connection.commit();
    }

    /**
     * 验证表结构是否正确
     */
    public boolean verifyTableStructure() {
        try {
            String[] requiredColumns = {
                    "username", "password", "token", "lastlogin_x",
                    "lastlogin_y", "lastlogin_z", "lastlogin_world", "uuid", "email"
            };

            List<String> existingColumns = getExistingColumns();

            // 检查所有必需列是否存在
            for (String column : requiredColumns) {
                if (!columnExists(existingColumns, column)) {
                    System.out.println("缺失列: " + column);
                    return false;
                }
            }

            // 检查主键
            if (!isUsernamePrimaryKey()) {
                System.out.println("username不是主键");
                return false;
            }

            System.out.println("表结构验证通过");
            return true;

        } catch (SQLException e) {
            System.err.println("验证表结构时出错: " + e.getMessage());
            return false;
        }
    }

    /**
     * 列定义内部类
     */
    private static class ColumnDefinition {
        private String dataType;
        private boolean isPrimaryKey;
        private boolean isNotNull;

        public ColumnDefinition(String dataType, boolean isPrimaryKey, boolean isNotNull) {
            this.dataType = dataType;
            this.isPrimaryKey = isPrimaryKey;
            this.isNotNull = isNotNull;
        }

        public String getDataType() {
            return dataType;
        }

        public boolean isPrimaryKey() {
            return isPrimaryKey;
        }

        public boolean isNotNull() {
            return isNotNull;
        }
    }

}
