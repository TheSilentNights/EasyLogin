package cn.thesilentnights.easylogin.mixin;

import cn.thesilentnights.easylogin.repo.PositionRepo;
import cn.thesilentnights.easylogin.services.action.ActionCheckService;
import cn.thesilentnights.easylogin.utils.Dependencies;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerPacketListenerMix {

        @Unique
        private final ActionCheckService easyLogin$actionCheckService = Dependencies.getDependency(ActionCheckService.class);

        @Shadow
        public ServerPlayer player;

        @Inject(method = "handleMovePlayer", at = @At("RETURN") // 在原始逻辑执行完之后注入
        )
        private void afterHandleMove(ServerboundMovePlayerPacket packet, CallbackInfo ci) {

                if (easyLogin$actionCheckService.shouldCancelEvent(player)) {
                        Vec3 pos = player.position();

                        Vec3 lastSafePos = PositionRepo.getPos(player.getUUID(), pos);
                        player.connection.teleport(
                                lastSafePos.x,
                                lastSafePos.y,
                                lastSafePos.z,
                                0,
                                0
                        );
                }
        }

}
