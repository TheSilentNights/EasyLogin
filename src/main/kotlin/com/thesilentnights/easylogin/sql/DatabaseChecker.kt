package com.thesilentnights.easylogin.sql

import cn.hutool.core.io.resource.ResourceUtil
import com.thesilentnights.easylogin.pojo.SqlColumnDefinition
import com.thesilentnights.easylogin.repo.CommonStaticRepo.TABLE_NAME
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.SQLException
import java.util.*

class DatabaseChecker(private val provider: DataSource?) {
    private val log: Logger= LogManager.getLogger(DatabaseChecker::class.java)

    fun checkDatabase() {
        if (provider == null) {
            return
        }

        try {
            provider.getConnection().use { connection ->
                //check table
                val metaData = connection.metaData
                if (!metaData.getTables(null, null, TABLE_NAME, arrayOf<String>("TABLE")).next()) {
                    createTable(connection)
                }
                checkColumns(connection)
                connection.commit()
            }
        } catch (e: SQLException) {
            log.error("error checking data base", e)
        }
    }

    /**
     * 创建accounts表
     */
    @Throws(SQLException::class)
    private fun createTable(connection: Connection) {
        var createTableSQL: String? = null
        try {
            createTableSQL = Files.readString(Path.of(ResourceUtil.getResource("sql.table_create.sql").getPath()))
        } catch (e: IOException) {
            log.error("error creating table", e)
            return
        }

        connection.createStatement().use { stmt ->
            stmt.execute(createTableSQL)
        }
        connection.commit()
        log.info("accounts created")
    }


    companion object {
        //only check the columns without checking the primary key
        @Throws(SQLException::class)
        private fun checkColumns(connection: Connection) {
            connection.createStatement().use { statement ->
                val metaData = connection.getMetaData()
                if (isColumnMissing(
                        metaData,
                        SqlColumnDefinition.LASTLOGIN_IP.toString().lowercase(Locale.getDefault())
                    )
                ) {
                    statement.executeUpdate(
                        "alter table $TABLE_NAME add column " + SqlColumnDefinition.LASTLOGIN_IP.toString()
                            .lowercase(Locale.getDefault()) + " varchar(255);"
                    )
                }

                if (isColumnMissing(
                        metaData,
                        SqlColumnDefinition.LASTLOGIN_X.toString().lowercase(Locale.getDefault())
                    )
                ) {
                    statement.executeUpdate(
                        "alter table $TABLE_NAME add column " + SqlColumnDefinition.LASTLOGIN_X.toString()
                            .lowercase(Locale.getDefault()) + " varchar(255);"
                    )
                }

                if (isColumnMissing(
                        metaData,
                        SqlColumnDefinition.LASTLOGIN_Y.toString().lowercase(Locale.getDefault())
                    )
                ) {
                    statement.executeUpdate(
                        "alter table $TABLE_NAME add column " + SqlColumnDefinition.LASTLOGIN_Y.toString()
                            .lowercase(Locale.getDefault()) + " varchar(255);"
                    )
                }

                if (isColumnMissing(
                        metaData,
                        SqlColumnDefinition.LASTLOGIN_Z.toString().lowercase(Locale.getDefault())
                    )
                ) {
                    statement.executeUpdate(
                        "alter table $TABLE_NAME add column " + SqlColumnDefinition.LASTLOGIN_Z.toString()
                            .lowercase(Locale.getDefault()) + " varchar(255);"
                    )
                }

                if (isColumnMissing(
                        metaData,
                        SqlColumnDefinition.LASTLOGIN_WORLD.toString().lowercase(Locale.getDefault())
                    )
                ) {
                    statement.executeUpdate(
                        "alter table $TABLE_NAME add column " + SqlColumnDefinition.LASTLOGIN_WORLD.toString()
                            .lowercase(Locale.getDefault()) + " varchar(255);"
                    )
                }

                if (isColumnMissing(
                        metaData,
                        SqlColumnDefinition.LOGIN_TIMESTAMP.toString().lowercase(Locale.getDefault())
                    )
                ) {
                    statement.executeUpdate(
                        "alter table $TABLE_NAME add column " + SqlColumnDefinition.LOGIN_TIMESTAMP.toString()
                            .lowercase(Locale.getDefault()) + " varchar(255);"
                    )
                }

                if (isColumnMissing(metaData, SqlColumnDefinition.EMAIL.toString().lowercase(Locale.getDefault()))) {
                    statement.executeUpdate(
                        "alter table $TABLE_NAME add column " + SqlColumnDefinition.EMAIL.toString()
                            .lowercase(Locale.getDefault()) + " varchar(255);"
                    )
                }
                if (isColumnMissing(metaData, SqlColumnDefinition.USERNAME.toString().lowercase(Locale.getDefault()))) {
                    statement.executeUpdate(
                        "alter table $TABLE_NAME add column " + SqlColumnDefinition.USERNAME.toString()
                            .lowercase(Locale.getDefault()) + " varchar(255);"
                    )
                }
            }
        }

        @Throws(SQLException::class)
        private fun isColumnMissing(metaData: DatabaseMetaData, columnName: String?): Boolean {
            val columns = metaData.getColumns(null, null, TABLE_NAME, columnName)
            return !columns.next()
        }
    }
}
