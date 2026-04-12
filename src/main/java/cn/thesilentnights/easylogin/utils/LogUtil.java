package cn.thesilentnights.easylogin.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.thesilentnights.easylogin.repo.CommonStaticRepo;

public class LogUtil {
    static Logger logger = LogManager.getLogger(CommonStaticRepo.MOD_ID);

    public static Logger getLogger() {
        return logger;
    }
}
