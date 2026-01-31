package com.thesilentnights.easylogin.service

import com.thesilentnights.easylogin.pojo.PlayerAccount
import com.thesilentnights.easylogin.sql.DataSource
import java.util.*


object AccountService {
    private var provider: DataSource? = null

    fun init(databaseProvider: DataSource) {
        provider = databaseProvider
    }

    fun hasAccount(uuid: UUID): Boolean {
        return provider!!.getAuthByUUID(uuid).isPresent
    }

    fun hasAccount(username: String?): Boolean {
        return provider!!.getAuthByName(username).isPresent
    }

    fun getAccount(uuid: UUID): Optional<PlayerAccount> {
        return provider!!.getAuthByUUID(uuid)
    }

    fun getAccount(username: String?): Optional<PlayerAccount> {
        return provider!!.getAuthByName(username)
    }

    fun updateAccount(account: PlayerAccount) {
        if (hasAccount(account.uuid)) {
            provider!!.updateAccount(account)
        }else{
            provider!!.insertAccount(account)
        }
    }
}
