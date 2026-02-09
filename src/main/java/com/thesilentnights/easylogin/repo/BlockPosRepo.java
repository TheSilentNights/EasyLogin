package com.thesilentnights.easylogin.repo;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.minecraft.core.BlockPos;

import java.time.Duration;

public class BlockPosRepo {

    private static final Cache<String, BlockPos> blockPosCache = Caffeine.newBuilder()
            .maximumSize(60)
            .expireAfterAccess(Duration.ofSeconds(20))
            .build();

    /**
     * Retrieves the cached BlockPos for the given key.
     * If not present, stores and returns the provided {@code current} BlockPos.
     *
     * @param key     the cache key (e.g., player UUID or name)
     * @param current the BlockPos to store if the key is not in cache
     * @return the cached or newly stored BlockPos, or null if loading failed
     */
    public static BlockPos getBlockPos(String key, BlockPos current) {
        return blockPosCache.get(key, k -> current);
    }
}
