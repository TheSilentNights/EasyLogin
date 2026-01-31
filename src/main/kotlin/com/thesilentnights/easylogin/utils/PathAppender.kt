package com.thesilentnights.easylogin.utils

import net.minecraftforge.fml.loading.FMLPaths
import kotlin.io.path.absolute
import kotlin.io.path.pathString

object PathAppender {
    fun append(path: String): String{
        return FMLPaths.GAMEDIR.relative().absolute().pathString + path
    }
}