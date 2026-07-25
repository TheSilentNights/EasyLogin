package cn.thesilentnights.easylogin.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cn.thesilentnights.easylogin.pojo.PlayerAccount;
import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class PasswordData extends SavedData {
    private static PasswordData instance;
    private Map<UUID, PlayerAccount> passwords = new ConcurrentHashMap<>();

       private PasswordData() {}

    public static final Codec<Map<UUID, PlayerAccount>> MAP_CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, PlayerAccount.CODEC);

    public static final Codec<PasswordData> CODEC = Codec.of(
            pTag -> load(pTag),
            passwordData -> save(passwordData)
    );

    public static void refreshLevel(ServerLevel level) {
        PasswordData.instance = level.getDataStorage().computeIfAbsent(
                new SavedDataType<PasswordData>(
                        "password_data",
                        PasswordData::new,
                )
        );
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

        ListTag passwords = pTag.getList("playerData", Tag.TAG_COMPOUND);

        for (int i = 0; i < passwords.size(); i++) {
            CompoundTag passwordTag = passwords.getCompound(i);

            UUID uuid = passwordTag.getUUID("uuid");
            String username = passwordTag.getString("username");
            String password = passwordTag.getString("password");
            PlayerAccount account = new PlayerAccount(uuid, username, password);
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
            passwordTag.putString("password", account.getPassword());
            listTag.add(passwordTag);
        }

        pTag.put("playerData", listTag);
        return pTag;
    }

}
