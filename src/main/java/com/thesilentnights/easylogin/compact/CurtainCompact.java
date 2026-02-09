package com.thesilentnights.easylogin.compact;

import net.minecraft.world.entity.LivingEntity;

public class CurtainCompact implements NpcCompactor {
    private static final CurtainCompact INSTANCE = new CurtainCompact();

    private CurtainCompact() {
    }

    public static CurtainCompact getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isNpc(LivingEntity entity) {
        // TODO: Not yet implemented
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
