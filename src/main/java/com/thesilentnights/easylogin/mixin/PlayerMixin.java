package com.thesilentnights.easylogin.mixin;


import com.thesilentnights.easylogin.repo.BlockPosRepo;
import com.thesilentnights.easylogin.service.ActionCheckService;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
<<<<<<< HEAD
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
=======
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
>>>>>>> parent of 2b3d94a (remove deps)
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

<<<<<<< HEAD
@Mixin(Player.class)
=======
@Mixin(ServerPlayer.class)
>>>>>>> parent of 2b3d94a (remove deps)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
<<<<<<< HEAD
        if ((Object) this instanceof ServerPlayer serverPlayer) {
            if (ActionCheckService.shouldCancelEvent(serverPlayer)) {
                BlockPos blockPos = BlockPosRepo.getBlockPos(this.stringUUID, this.blockPosition());
                this.teleportTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            }
=======
        if (ActionCheckService.shouldCancelEvent((ServerPlayer) (Object) this)) {
            System.out.println("tick");
            BlockPos blockPos = BlockPosRepo.getBlockPos(this.stringUUID, this.blockPosition());
            assert blockPos != null;
            this.teleportTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
>>>>>>> parent of 2b3d94a (remove deps)
        }
    }


}
