package cn.thesilentnights.easylogin.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cn.thesilentnights.easylogin.pojo.PlayerAccount;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class PasswordData extends SavedData {
    private static PasswordData instance;
    private Map<UUID, PlayerAccount> passwords = new ConcurrentHashMap<>();

    public PasswordData() {

    }

    public PasswordData(Map<UUID, PlayerAccount> passwords) {
        this.passwords = passwords;
    }

    public static final Codec<Map<UUID, PlayerAccount>> MAP_CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, PlayerAccount.CODEC);

    public static final Codec<PasswordData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            MAP_CODEC.fieldOf("passwords").forGetter(
                obj -> {
                    if(obj instanceof PasswordData data){
                        return data.passwords;
                    }else{
                        return null;
                    }
                }
            )
        ).apply(instance, PasswordData::new)
    );

    public static void refreshLevel(ServerLevel level) {
        SavedDataType<PasswordData> dataType = new SavedDataType<PasswordData>(
                Identifier.fromNamespaceAndPath("easylogin", "passwords"),
                        PasswordData::new,
                        CODEC
                );

        PasswordData.instance = level.getDataStorage().computeIfAbsent(dataType);
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

}
