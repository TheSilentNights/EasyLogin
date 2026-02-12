package com.thesilentnights.easylogin.service;

import com.thesilentnights.easylogin.pojo.PlayerAccount;
import com.thesilentnights.easylogin.sql.DataSource;

import java.util.Optional;
import java.util.UUID;

public class AccountService {

    private static DataSource dataSource;

    public static void init(DataSource dataSource) {
        AccountService.dataSource = dataSource;
    }

    public static boolean hasAccount(UUID uuid) {
        return dataSource.getAuthByUUID(uuid).isPresent();
    }

    public static boolean hasAccount(String username) {
        return dataSource.getAuthByName(username).isPresent();
    }

    public static Optional<PlayerAccount> getAccount(UUID uuid) {
        return dataSource.getAuthByUUID(uuid);
    }

    public static Optional<PlayerAccount> getAccount(String username) {
        return dataSource.getAuthByName(username);
    }

    public static boolean updatePassword(String value, UUID uuid) {
        return dataSource.updatePassword(value, uuid);
    }

    public static void updateAccount(PlayerAccount account) {
        if (hasAccount(account.getUuid())) {
            dataSource.updateAccount(account);
        } else {
            dataSource.insertAccount(account);
        }
    }
}