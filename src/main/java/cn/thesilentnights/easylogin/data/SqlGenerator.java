package cn.thesilentnights.easylogin.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringJoiner;

public class SqlGenerator {

        public static String createTable(String table, LinkedHashMap<String, String> columns) {
                StringJoiner joiner = new StringJoiner(", ");
                for (var entry : columns.entrySet()) {
                        joiner.add(entry.getKey() + " " + entry.getValue());
                }
                return "CREATE TABLE IF NOT EXISTS " + table + " (" + joiner + ")";
        }

        public static String select(String table, List<String> columns, String whereColumn) {
                StringJoiner joiner = new StringJoiner(", ");
                for (String col : columns) {
                        joiner.add(col);
                }
                return "SELECT " + joiner + " FROM " + table + " WHERE " + whereColumn + " = ?";
        }

        public static String updateOrInsert(String table, List<String> columns, String primaryKey) {
                StringJoiner colJoiner = new StringJoiner(", ");
                StringJoiner valJoiner = new StringJoiner(", ");
                StringJoiner updateJoiner = new StringJoiner(", ");
                for (String col : columns) {
                        colJoiner.add(col);
                        valJoiner.add("?");
                        if (!col.equals(primaryKey)) {
                                updateJoiner.add(col + " = excluded." + col);
                        }
                }
                return "INSERT INTO " + table + " (" + colJoiner + ") VALUES (" + valJoiner + ") " +
                        "ON CONFLICT(" + primaryKey + ") DO UPDATE SET " + updateJoiner;
        }

        public static String delete(String table, String whereColumn) {
                return "DELETE FROM " + table + " WHERE " + whereColumn + " = ?";
        }
}
