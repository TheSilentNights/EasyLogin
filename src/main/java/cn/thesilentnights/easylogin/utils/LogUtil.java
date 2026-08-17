package cn.thesilentnights.easylogin.utils;

import cn.thesilentnights.easylogin.repo.CommonStaticRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
        static Logger logger = LogManager.getLogger(CommonStaticRepo.MOD_ID);

        public static Logger getLogger() {
                return logger;
        }
}
