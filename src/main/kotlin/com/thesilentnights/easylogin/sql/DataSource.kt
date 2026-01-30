package com.thesilentnights.easylogin.sql

import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.pojo.SqlColumnDefinition
import com.thesilentnights.easylogin.repo.CommonStaticRepo
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import java.util.function.Supplier


class DataSource(dataSourceSupplier: Supplier<HikariDataSource>) {
    private val dataSource: HikariDataSource = dataSourceSupplier.get()
    private var log: Logger = LoggerFactory.getLogger(this::class.java)

    fun getAuthByName(name: String?): Optional<PlayerAccount> {
        try {
            dataSource.connection.use { connection ->
                val sql = "select * from " + CommonStaticRepo.TABLE_NAME + " where name=?"
                connection.prepareStatement(sql).use { statement ->
                    statement.setString(1, name)
                    return Optional.ofNullable(PlayerAccount.fromResultSet(statement.executeQuery()))
                }
            }
        } catch (e: Exception) {
            return Optional.empty()
        }
    }


    fun getAuthByUUID(uuid: UUID): Optional<PlayerAccount> {
        try {
            dataSource.connection.use { connection ->
                val sql = "select * from " + CommonStaticRepo.TABLE_NAME + " where uuid= ?"
                connection.prepareStatement(sql).use { statement ->
                    statement.setString(1, uuid.toString())
                    return Optional.ofNullable(PlayerAccount.fromResultSet(statement.executeQuery()))
                }
            }
        } catch (e: SQLException) {
            return Optional.empty()
        }
    }

    @Throws(SQLException::class)
    fun updateColumn(key: SqlColumnDefinition, value: String, uuid: UUID): Boolean {
        val connection: Connection = getConnection()
        val stmt = connection.prepareStatement("UPDATE " + CommonStaticRepo.TABLE_NAME + " SET ?=? where uuid=?")
        stmt.setString(1, key.name)
        stmt.setString(2, value)
        stmt.setString(3, uuid.toString())
        if (stmt.executeUpdate() <= 0) {
            return false
        } else {
            connection.commit()
            return true
        }
    }


    @Throws(SQLException::class)
    private fun transactionalUpdate(
        connection: Connection,
        key: SqlColumnDefinition,
        value: String?,
        uuid: UUID
    ): Boolean {
        val stmt = connection.prepareStatement("update " + CommonStaticRepo.TABLE_NAME + " set ?=? where uuid=?")
        stmt.setString(1, key.name)
        stmt.setString(2, value)
        stmt.setString(3, uuid.toString())
        return stmt.execute()
    }

    fun updateAll(entries: Map<SqlColumnDefinition, String>, uuid: UUID): Boolean {
        try {
            val connection: Connection = getConnection()
            for ((key, value) in entries) {
                transactionalUpdate(connection, key, value, uuid)
            }
            connection.commit()
            return true
        }catch (e: SQLException) {
            log.error("sqlerror in updateAll", e)
            log.info("obj{}",entries.toString())
            return false
        }
    }


    fun removeAuth(uuid: UUID?): Boolean {
        return false
    }


    @Throws(SQLException::class)
    fun getConnection(): Connection {
        return dataSource.connection
    }

}