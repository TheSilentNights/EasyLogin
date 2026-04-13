package cn.thesilentnights.easylogin.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.world.phys.Vec3;

public class PositionRepo {
    private static final Map<UUID, Vec3> cacheMap = new HashMap<>();

    public static void addPos(UUID uuid, Vec3 pos) {
        cacheMap.put(uuid, pos);
    }

    public static Vec3 getPos(UUID uuid, Vec3 pos) {
        if (cacheMap.containsKey(uuid)) {
            return cacheMap.get(uuid);
        }
        cacheMap.put(uuid, pos);
        return pos;
    }

}
