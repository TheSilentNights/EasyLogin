package cn.thesilentnights.easylogin.service;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

import cn.thesilentnights.easylogin.compact.CurtainCompact;
import cn.thesilentnights.easylogin.compact.NpcCompactor;

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