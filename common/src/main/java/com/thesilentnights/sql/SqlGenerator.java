package com.thesilentnights.sql;

import org.apache.ibatis.jdbc.SQL;

public class SqlGenerator {
    public static String selectPersonSql =
        new SQL() {{
            SELECT("*");
            FROM("accounts");
            WHERE("username=#{username}");
        }}.toString();

}
