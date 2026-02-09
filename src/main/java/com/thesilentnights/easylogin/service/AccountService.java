package com.thesilentnights.easylogin.service;

import com.thesilentnights.easylogin.pojo.PlayerAccount;
import com.thesilentnights.easylogin.pojo.SqlColumnDefinition;
import com.thesilentnights.easylogin.sql.DataSource;

import java.util.Optional;
import java.util.UUID;

public class AccountService {

    private final DataSource dataSource;

    public AccountService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean hasAccount(UUID uuid) {
        return dataSource.getAuthByUUID(uuid).isPresent();
    }

    public boolean hasAccount(String username) {
        return dataSource.getAuthByName(username).isPresent();
    }

    public Optional<PlayerAccount> getAccount(UUID uuid) {
        return dataSource.getAuthByUUID(uuid);
    }

    public Optional<PlayerAccount> getAccount(String username) {
        return dataSource.getAuthByName(username);
    }

    public boolean updateSingleColumn(SqlColumnDefinition key, String value, UUID uuid) {
        return dataSource.updateColumn(key, value, uuid);
    }

    public void updateAccount(PlayerAccount account) {
        if (hasAccount(account.getUuid())) {
            dataSource.updateAccount(account);
        } else {
            dataSource.insertAccount(account);
        }
    }
}