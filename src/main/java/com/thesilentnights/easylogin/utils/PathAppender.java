package com.thesilentnights.easylogin.utils;


import com.thesilentnights.easylogin.repo.CommonStaticRepo;

public class PathAppender {
    public static String append(String path) {
        return CommonStaticRepo.GAME_DIR + path;

    }
}