package com.thesilentnights.easylogin.utils;

import com.thesilentnights.easylogin.repo.CommonStaticRepo;

public class LogUtil {
    public static void logInfo(Class<?> clazz, String message) {
        CommonStaticRepo.log.info("{} : {}", clazz.getSimpleName(), message);
    }


    public static void logError(Class<?> clazz, String message, Exception error) {
        CommonStaticRepo.log.error("{} : {}", clazz.getSimpleName(), message, error);
    }
}
