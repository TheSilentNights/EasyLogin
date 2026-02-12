package com.thesilentnights.easylogin.compact;

import dev.dubhe.curtain.features.player.fakes.IEntity;
import dev.dubhe.curtain.features.player.fakes.IServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class CurtainCompact implements NpcCompactor {

    public boolean isNpc(LivingEntity entity) {
        return entity instanceof IEntity || entity instanceof IServerPlayer;
    }
}
