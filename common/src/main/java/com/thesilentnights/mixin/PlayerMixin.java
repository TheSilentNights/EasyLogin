package com.thesilentnights.mixin;


import com.thesilentnights.repo.BlockPosRepo;
import com.thesilentnights.service.PlayerLoginService;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Slf4j
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        //log.info("tick");
        if ((Object) this instanceof ServerPlayer serverPlayer) {
            if (PlayerLoginService.shouldCancelEvent(serverPlayer)) {
                Optional<BlockPos> blockPos = BlockPosRepo.getBlockPos(serverPlayer.getGameProfile().getName());
                if (blockPos.isPresent()){
                    serverPlayer.teleportTo(serverPlayer.getLevel(),blockPos.get().getX(), blockPos.get().getY(), blockPos.get().getZ(),0,0);
                }else{
                    BlockPosRepo.setBlockPos(serverPlayer.getGameProfile().getName(), serverPlayer.blockPosition());
                }
            }
        }
    }


}
