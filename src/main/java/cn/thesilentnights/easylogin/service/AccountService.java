package cn.thesilentnights.easylogin.service;

import java.util.Optional;
import java.util.UUID;

import cn.thesilentnights.easylogin.data.PasswordData;
import cn.thesilentnights.easylogin.pojo.PlayerAccount;
import cn.thesilentnights.easylogin.utils.PasswordHasher;

public class AccountService {

    public static boolean hasAccount(UUID uuid) {
        return PasswordData.getAccount(uuid) != null;
    }

    public static Optional<PlayerAccount> getAccount(UUID uuid) {
        return Optional.ofNullable(PasswordData.getAccount(uuid));
    }

    public static boolean updatePassword(String rawPassword, UUID uuid) {
        PlayerAccount previous = PasswordData.getAccount(uuid);
        if (previous == null) {
            return false;
        }
        previous.setPassword(PasswordHasher.hash(rawPassword));
        PasswordData.updateAccount(uuid, previous);

        return true;
    }

    public static void updateAccount(PlayerAccount account) {
        if (hasAccount(account.getUuid())) {
            PasswordData.updateAccount(account.getUuid(), account);
        } else {
            PasswordData.registerAccount(account);
        }
    }
}