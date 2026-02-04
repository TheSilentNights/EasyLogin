package com.thesilentnights.easylogin.sql

import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.pojo.SqlColumnDefinition
import com.thesilentnights.easylogin.repo.CommonStaticRepo.TABLE_NAME
import com.thesilentnights.easylogin.utils.logError
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import java.util.function.Supplier


class DataSource(dataSourceSupplier: Supplier<HikariDataSource>) {
    private val dataSource: HikariDataSource = dataSourceSupplier.get()

    fun getAuthByName(name: String?): Optional<PlayerAccount> {
        try {
            dataSource.connection.use { connection ->
                val sql = "select * from " + TABLE_NAME + " where name=?"
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
                val sql = "select * from $TABLE_NAME where uuid= ?"
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
        val stmt = connection.prepareStatement("UPDATE $TABLE_NAME SET ?=? where uuid=?")
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
        val stmt = connection.prepareStatement("update $TABLE_NAME set ?=? where uuid=?")
        stmt.setString(1, key.name)
        stmt.setString(2, value)
        stmt.setString(3, uuid.toString())
        return stmt.execute()
    }

    fun updateAccount(account: PlayerAccount): Boolean {
        try {
            getConnection().use { connection ->
                val stmt =
                    connection.prepareStatement("update $TABLE_NAME set password=?, lastlogin_x=?, lastlogin_y=?, lastlogin_z=?, lastlogin_ip=?, lastlogin_world=?, username=?, email=?, login_timestamp=? where uuid=?")
                stmt.setString(1, account.password)
                stmt.setDouble(2, account.lastlogin_x)
                stmt.setDouble(3, account.lastlogin_y)
                stmt.setDouble(4, account.lastlogin_z)
                stmt.setString(5, account.lastlogin_ip)
                stmt.setString(6, account.lastlogin_world)
                stmt.setString(7, account.username)
                stmt.setString(8, account.email)
                stmt.setLong(9, account.login_timstamp)
                val updated: Int = stmt.executeUpdate()
                connection.commit()
                return updated > 0
            }
        } catch (e: SQLException) {
            logError(DataSource::class,"sqlerror in updateAccount", e)
            return false
        }
    }

    fun insertAccount(account: PlayerAccount): Boolean {
        try {
            getConnection().use { connection ->
                val stmt =
                    connection.prepareStatement("INSERT INTO $TABLE_NAME (uuid, password, lastlogin_x, lastlogin_y, lastlogin_z, lastlogin_ip, lastlogin_world, username, email, login_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")
                stmt.setString(1, account.uuid.toString())
                stmt.setString(2, account.password)
                stmt.setDouble(3, account.lastlogin_x)
                stmt.setDouble(4, account.lastlogin_y)
                stmt.setDouble(5, account.lastlogin_z)
                stmt.setString(6, account.lastlogin_ip)
                stmt.setString(7, account.lastlogin_world)
                stmt.setString(8, account.username)
                stmt.setString(9, account.email)
                stmt.setLong(10, account.login_timstamp)
                val updated: Int = stmt.executeUpdate()
                connection.commit()
                return updated > 0
            }

        } catch (e: SQLException) {
            logError(this::class,"sqlerror in updateAccount", e)
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