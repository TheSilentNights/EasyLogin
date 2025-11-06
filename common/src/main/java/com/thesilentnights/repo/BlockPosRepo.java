package com.thesilentnights.repo;

import net.minecraft.core.BlockPos;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BlockPosRepo {
    private static ConcurrentMap<String, BlockPos> blockPosMap = new ConcurrentHashMap<>();

    public static Optional<BlockPos> getBlockPos(String key) {
        return Optional.ofNullable(blockPosMap.get(key));
    }

    public static void removeBlockPos(String key) {
        blockPosMap.remove(key);
    }

    public static void setBlockPos(String key, BlockPos blockPos) {
        blockPosMap.put(key, blockPos);
    }
}
