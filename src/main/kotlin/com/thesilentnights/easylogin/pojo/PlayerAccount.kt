package com.thesilentnights.easylogin.pojo

import java.sql.ResultSet
import java.sql.SQLException
import java.util.*


data class PlayerAccount(
    val uuid: UUID,
    var username: String,
    var password: String,
    var lastLoginIp: String,
    var lastLoginX: Double,
    var lastLoginY: Double,
    var lastLoginZ: Double,
    var lastLoginWorld: String?,
    var email: String?,
    var loginTimestamp: Long,
) {


    companion object {
        @Throws(SQLException::class)
        fun fromResultSet(set: ResultSet): PlayerAccount? {
            //没有对应结果
            if (set.getString(SqlColumnDefinition.UUID.toString().lowercase(Locale.getDefault())) == null) {
                return null
            }
            return PlayerAccount(
                UUID.fromString(set.getString(SqlColumnDefinition.UUID.toString().lowercase(Locale.getDefault()))),
                set.getString(SqlColumnDefinition.USERNAME.toString().lowercase(Locale.getDefault())),
                set.getString(SqlColumnDefinition.PASSWORD.toString().lowercase(Locale.getDefault())),
                set.getString(SqlColumnDefinition.LASTLOGIN_IP.toString().lowercase(Locale.getDefault())),
                set.getDouble(SqlColumnDefinition.LASTLOGIN_X.toString().lowercase(Locale.getDefault())),
                set.getDouble(SqlColumnDefinition.LASTLOGIN_Y.toString().lowercase(Locale.getDefault())),
                set.getDouble(SqlColumnDefinition.LASTLOGIN_Z.toString().lowercase(Locale.getDefault())),
                set.getString(SqlColumnDefinition.LASTLOGIN_WORLD.toString().lowercase(Locale.getDefault())),
                set.getString(SqlColumnDefinition.EMAIL.toString().lowercase(Locale.getDefault())),
                set.getLong(SqlColumnDefinition.LOGIN_TIMESTAMP.toString().lowercase(Locale.getDefault()))
            )
        }
    }
}
