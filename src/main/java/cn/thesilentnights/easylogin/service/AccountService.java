package cn.thesilentnights.easylogin.service;

import java.util.Optional;
import java.util.UUID;

import cn.thesilentnights.easylogin.data.DataManager;
import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;
import cn.thesilentnights.easylogin.utils.PasswordHasher;

public class AccountService {

    public static boolean hasAccount(UUID uuid) {
        try {
            return DataManager.password().get(uuid.toString()) != null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to query account for " + uuid, e);
        }
    }

    public static Optional<PlayerPasswordData> getAccount(UUID uuid) {
        try {
            return Optional.ofNullable(DataManager.password().get(uuid.toString()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to query account for " + uuid, e);
        }
    }

    public static boolean updatePassword(String rawPassword, UUID uuid) {
        try {
            PlayerPasswordData previous = DataManager.password().get(uuid.toString());
            if (previous == null) {
                return false;
            }
            previous.setPassword(PasswordHasher.hash(rawPassword));
            DataManager.password().save(previous);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update password for " + uuid, e);
        }
    }

    public static void updateAccount(PlayerPasswordData account) {
        try {
            DataManager.password().save(account);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save account for " + account.getUuid(), e);
        }
    }
}
