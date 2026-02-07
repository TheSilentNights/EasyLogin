package com.thesilentnights.easylogin.utils;

import net.minecraftforge.fml.loading.FMLPaths;


public class PathAppender {
    public static String append(String path) {
        return FMLPaths.GAMEDIR.relative().toAbsolutePath() + path;
    }
}