package cn.thesilentnights.easylogin.data;

import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

@Deprecated
public class PasswordData extends SavedData {
        private static PasswordData instance;
        private Map<UUID, PlayerPasswordData> passwords = new ConcurrentHashMap<>();

        public PasswordData() {

        }

        public PasswordData(Map<UUID, PlayerPasswordData> passwords) {
                this.passwords = passwords;
        }

        public static final Codec<Map<UUID, PlayerPasswordData>> MAP_CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, PlayerPasswordData.CODEC);

        public static final Codec<PasswordData> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        MAP_CODEC.fieldOf("passwords").forGetter(
                                obj -> {
                                        if (obj instanceof PasswordData data) {
                                                return data.passwords;
                                        } else {
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

        @Deprecated
        public static PlayerPasswordData getAccount(UUID uuid) {
                return instance.passwords.get(uuid);
        }

        @Deprecated
        public static void updateAccount(UUID uuid, PlayerPasswordData account) {
                instance.passwords.put(uuid, account);
                instance.setDirty();
        }

        @Deprecated
        public static void registerAccount(PlayerPasswordData account) {
                instance.passwords.put(account.getUuid(), account);
                instance.setDirty();
        }

        @Deprecated
        public static void invalidate() {
                instance = null;
        }

}
