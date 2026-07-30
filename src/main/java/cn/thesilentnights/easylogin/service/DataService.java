package cn.thesilentnights.easylogin.service;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.data.DataManager;
import cn.thesilentnights.easylogin.pojo.PlayerExtraData;
import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;
import cn.thesilentnights.easylogin.utils.PasswordHasher;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;

public class DataService {

        public static boolean hasAccount(UUID uuid) {
                try {
                        return DataManager.password().get(uuid.toString()) != null;
                } catch (Exception e) {
                        throw new RuntimeException("Failed to query account for " + uuid, e);
                }
        }

        public static Optional<PlayerPasswordData> getPlayerPasswordData(UUID uuid) {
                try {
                        return Optional.ofNullable(DataManager.password().get(uuid.toString()));
                } catch (Exception e) {
                        throw new RuntimeException("Failed to query account for " + uuid, e);
                }
        }

        public static Optional<PlayerExtraData> getPlayerExtraData(UUID uuid) {
                try {
                        return Optional.ofNullable(DataManager.extra().get(uuid.toString()));
                } catch (Exception e) {
                        throw new RuntimeException("Failed to query account for " + uuid, e);
                }
        }

        public static boolean updatePassword(UUID uuid, String rawPassword) {
                try {
                        DataManager.password().save(new PlayerPasswordData(
                                uuid,
                                PasswordHasher.hash(rawPassword)
                        ));
                        return true;
                } catch (Exception e) {
                        return false;
                }
        }

        public static boolean updatePlayerExtraData(PlayerExtraData data) {
                try {
                        DataManager.extra().save(data);
                        return true;
                } catch (Exception e) {
                        return false;
                }
        }

        public static void recordPlayer(ServerPlayer serverPlayer) {
                if (EasyLoginConfig.enableExtraDataRecord.get()) {
                        updatePlayerExtraData(new PlayerExtraData(
                                serverPlayer.getUUID(),
                                serverPlayer.getDisplayName().getString(),
                                serverPlayer.getX(),
                                serverPlayer.getY(),
                                serverPlayer.getZ(),
                                serverPlayer.level().dimension().identifier().getPath(),
                                System.currentTimeMillis(),
                                serverPlayer.getIpAddress()
                        ));
                }
        }
}
