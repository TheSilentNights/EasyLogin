package com.thesilentnights.easylogin.repo

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import net.minecraft.core.BlockPos
import java.time.Duration
import java.util.function.Function

object BlockPosRepo {
    private val blockPosCache: Cache<String, BlockPos> = Caffeine.newBuilder().maximumSize(60).expireAfterAccess(
        Duration.ofSeconds(20)
    ).build()

    @JvmStatic
    fun getBlockPos(key: String, current: BlockPos): BlockPos? {
        return blockPosCache.get(key, Function { s: String -> current })
    }
}
