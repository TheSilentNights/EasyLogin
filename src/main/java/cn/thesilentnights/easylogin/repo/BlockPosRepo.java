package cn.thesilentnights.easylogin.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.BlockPos;

public class BlockPosRepo {
    private static final Map<UUID, BlockPos> cacheMap = new HashMap<>();

    public static void addBlockPos(UUID uuid, BlockPos pos) {
        cacheMap.put(uuid, pos);
    }

    public static BlockPos getBlockPos(UUID uuid, BlockPos pos) {
        if (cacheMap.containsKey(uuid)) {
            return cacheMap.get(uuid);
        }
        cacheMap.put(uuid, pos);
        return pos;
    }

}
