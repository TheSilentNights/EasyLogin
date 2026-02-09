package com.thesilentnights.easylogin.service

import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.pojo.SqlColumnDefinition
import com.thesilentnights.easylogin.sql.DataSource
import java.util.*


object AccountService {
    private lateinit var dataSource: DataSource
    fun init(dataSource: DataSource) {
        this.dataSource = dataSource
    }

    fun hasAccount(uuid: UUID): Boolean {
        return dataSource.getAuthByUUID(uuid).isPresent
    }

    fun hasAccount(username: String?): Boolean {
        return dataSource.getAuthByName(username).isPresent
    }

    fun getAccount(uuid: UUID): Optional<PlayerAccount> {
        return dataSource.getAuthByUUID(uuid)
    }

    fun getAccount(username: String): Optional<PlayerAccount> {
        return dataSource.getAuthByName(username)
    }

    fun updateSingleColumn(key: SqlColumnDefinition, value: String, uuid: UUID): Boolean {
        return dataSource.updateColumn(key, value, uuid)
    }

    fun updateAccount(account: PlayerAccount) {
        if (hasAccount(account.uuid)) {
            dataSource.updateAccount(account)
        } else {
            dataSource.insertAccount(account)
        }
    }
}
