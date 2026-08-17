package cn.thesilentnights.easylogin.services.action;

import net.minecraft.world.entity.LivingEntity;

public interface ActionCheckService {

        boolean shouldCancelEvent(LivingEntity entity);

}
