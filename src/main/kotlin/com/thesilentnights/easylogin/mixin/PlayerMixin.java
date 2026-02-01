package com.thesilentnights.easylogin.mixin;


import com.thesilentnights.easylogin.repo.BlockPosRepo;
import com.thesilentnights.easylogin.service.ActionCheckService;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    @Unique
    private static final Logger easyLogin$log = LogManager.getLogger(PlayerMixin.class);

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        easyLogin$log.info("tick");
        if ((Object) this instanceof ServerPlayer serverPlayer) {
            if (ActionCheckService.shouldCancelEvent(serverPlayer)) {
                BlockPos blockPos = BlockPosRepo.getBlockPos(this.stringUUID, this.blockPosition());
                this.teleportTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            }
        }
    }


}
