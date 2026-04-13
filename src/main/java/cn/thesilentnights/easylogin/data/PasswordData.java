package cn.thesilentnights.easylogin.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.mojang.serialization.Codec;

import cn.thesilentnights.easylogin.pojo.PlayerAccount;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class PasswordData extends SavedData {
    private static PasswordData instance;
    private Map<UUID, PlayerAccount> passwords = new ConcurrentHashMap<>();

       private PasswordData() {}

    public static void refreshLevel(ServerLevel level) {
        PasswordData.instance = level.getDataStorage().computeIfAbsent(
                PasswordData::load,
                PasswordData::new,
                "easylogin-passwords");
    }

    public static PlayerAccount getAccount(UUID uuid) {
        return instance.passwords.get(uuid);
    }

    public static void updateAccount(UUID uuid, PlayerAccount account) {
        instance.passwords.put(uuid, account);
        instance.setDirty();
    }

    public static void registerAccount(PlayerAccount account) {
        instance.passwords.put(account.getUuid(), account);
        instance.setDirty();
    }

    public static void invalidate() {
        instance = null;
    }

    public static PasswordData load(CompoundTag pTag) {
        PasswordData passwordData = new PasswordData();

        ListTag passwords = pTag.getList("passwords", Tag.TAG_COMPOUND);

        for (int i = 0; i < passwords.size(); i++) {
            CompoundTag passwordTag = passwords.getCompound(i);

            UUID uuid = passwordTag.getUUID("uuid");
            String username = passwordTag.getString("username");
            String password = passwordTag.getString("password");
            String lastLoginIp = passwordTag.getString("lastLoginIp");
            double lastLoginX = passwordTag.getDouble("lastLoginX");
            double lastLoginY = passwordTag.getDouble("lastLoginY");
            double lastLoginZ = passwordTag.getDouble("lastLoginZ");
            String lastLoginWorld = passwordTag.getString("lastLoginWorld");
            long loginTimestamp = passwordTag.getLong("loginTimestamp");
            PlayerAccount account = new PlayerAccount(uuid, username, password, lastLoginIp,
                    lastLoginX, lastLoginY, lastLoginZ, lastLoginWorld, loginTimestamp);
            passwordData.passwords.put(uuid, account);

        }

        return passwordData;
    }

    @Override
    public CompoundTag save(CompoundTag pTag) {
        ListTag listTag = new ListTag();
        // 保存密码
        for (Map.Entry<UUID, PlayerAccount> entry : passwords.entrySet()) {
            UUID uuid = entry.getKey();
            PlayerAccount account = entry.getValue();

            CompoundTag passwordTag = new CompoundTag();
            passwordTag.putUUID("uuid", uuid);
            passwordTag.putString("username", account.getUsername());
            passwordTag.putString("lastLoginIp", account.getLastLoginIp());
            passwordTag.putDouble("lastLoginX", account.getLastLoginX());
            passwordTag.putDouble("lastLoginY", account.getLastLoginY());
            passwordTag.putDouble("lastLoginZ", account.getLastLoginZ());
            passwordTag.putString("lastLoginWorld", account.getLastLoginWorld());
            passwordTag.putString("password", account.getPassword());
            passwordTag.putLong("loginTimestamp", account.getLoginTimestamp());
            listTag.add(passwordTag);
        }

        pTag.put("passwords", listTag);
        return pTag;
    }

}
