package com.thesilentnights.easylogin.configs;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesilentnights.easylogin.repo.CommonStaticRepo;
import com.thesilentnights.easylogin.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EasyLoginConfig {
    public static EasyLoginConfig INSTANCE;

    public String pathToDatabase;
    public boolean enableKickOther;
    public Integer loginTimeoutTick;
    public boolean enablePreLoginProtection;

    public EasyLoginConfig(String pathToDatabase, boolean enableKickOther, Integer loginTimeoutTick, boolean enablePreLoginProtection) {
        this.pathToDatabase = pathToDatabase;
        this.enableKickOther = enableKickOther;
        this.loginTimeoutTick = loginTimeoutTick;
        this.enablePreLoginProtection = enablePreLoginProtection;
        INSTANCE = this;
    }

    public EasyLoginConfig() {
        INSTANCE = this;
    }


    public static void readFromConfigFile() {
        File configFile = FileUtil.file(CommonStaticRepo.GAME_DIR, CommonStaticRepo.configPath);
        File readme = FileUtil.file(CommonStaticRepo.GAME_DIR, CommonStaticRepo.readmePath);
        // create readme if not exists
        if (!readme.exists()) {
            FileUtil.copyFile(ResourceUtil.getResourceObj(CommonStaticRepo.readmeResourcePath), readme);
        }

        // read from configs
        try {
            new ObjectMapper(CommonStaticRepo.ymlFactory).readValue(configFile, EasyLoginConfig.class);
        } catch (FileNotFoundException e) {
            // create config file
            writeToConfigFile(getDefaultConfig());
        } catch (DatabindException e) {
            LogUtil.logError(EasyLoginConfig.class, "read config file error.Please fix the config file or just regenerate it", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LogUtil.logError(EasyLoginConfig.class, "internal error occurred {}", e);
            throw new RuntimeException(e);
        }
    }

    private static void writeToConfigFile(EasyLoginConfig easyLoginConfig) {
        File configFile = FileUtil.file(CommonStaticRepo.GAME_DIR, CommonStaticRepo.configPath);
        try {
            configFile.createNewFile();
            new ObjectMapper(CommonStaticRepo.ymlFactory).writeValue(configFile, easyLoginConfig);
        } catch (IOException e) {
            LogUtil.logError(EasyLoginConfig.class, "write config file error {}", e);
            throw new RuntimeException(e);
        }
    }

    public static EasyLoginConfig getDefaultConfig() {
        return new EasyLoginConfig(
                CommonStaticRepo.defaultSqlitePath,
                false,
                60 * 20,
                false
        );
    }


}
