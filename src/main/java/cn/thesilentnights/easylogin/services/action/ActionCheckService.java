package cn.thesilentnights.easylogin.services.action;

import java.util.UUID;

import net.minecraft.world.entity.LivingEntity;

public interface ActionCheckService {
    
    boolean shouldCancelEvent(LivingEntity entity);

    boolean isLoggedIn(UUID uuid);
}
