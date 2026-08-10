package cn.thesilentnights.easylogin.services.data;

import java.util.Optional;
import java.util.UUID;

import cn.thesilentnights.easylogin.pojo.PlayerExtraData;
import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;
import net.minecraft.server.level.ServerPlayer;

public interface DataService {
    boolean hasAccount(UUID uuid);
    Optional<PlayerPasswordData> getPlayerPasswordData(UUID uuid);
    Optional<PlayerExtraData> getPlayerExtraData(UUID uuid);
    boolean updatePassword(UUID uuid, String rawPassword);
    boolean updatePlayerExtraData(PlayerExtraData data);
    void recordPlayer(ServerPlayer serverPlayer);
}
