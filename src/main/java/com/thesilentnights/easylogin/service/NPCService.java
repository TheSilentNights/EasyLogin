package com.thesilentnights.easylogin.service;

import com.thesilentnights.easylogin.compact.CurtainCompact;
import com.thesilentnights.easylogin.compact.NpcCompactor;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class NPCService {

    private static final List<NpcCompactor> checkList = new ArrayList<>();

    static {
        if (ModList.get().isLoaded("curtain")) {
            checkList.add(new CurtainCompact());
        }
    }

    public static boolean isNPC(LivingEntity entity) {
        if (checkList.isEmpty()) {
            return false;
        }
        return checkList.stream().anyMatch(compactor -> compactor.isNpc(entity));
    }
}