package com.thesilentnights.easylogin.configs;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesilentnights.pojo.MailAccountEntry;
import com.thesilentnights.repo.CommonStaticRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EasyLoginConfig {

    DataBaseType dataBaseType;
    String pathToDatabase;
    boolean enableKickOther;
    MailAccountEntry mailAccountEntry;
    Long loginTimeoutTick;

    public static EasyLoginConfig readFromConfigFile() {
        File configFile = FileUtil.file(CommonStaticRepo.GAME_DIR, CommonStaticRepo.configPath);
        File readme = FileUtil.file(CommonStaticRepo.GAME_DIR, CommonStaticRepo.readmePath);
        // create readme if not exists
        if (!readme.exists()) {
            FileUtil.copyFile(ResourceUtil.getResourceObj(CommonStaticRepo.readmeResourcePath), readme);
        }

        // read from configs
        try {
            return new ObjectMapper(CommonStaticRepo.ymlFactory).readValue(configFile, EasyLoginConfig.class);
        } catch (FileNotFoundException e) {
            // create config file
            writeToConfigFile(getDefaultConfig());
            return getDefaultConfig();
        } catch (DatabindException e) {
            log.atError().log("read config file error.Please fix the config file or just regenerate it {}", e);
            return getDefaultConfig();
        } catch (IOException e) {
            log.atError().log("internal error occurred {}", e);
            return getDefaultConfig();
        }
    }

    private static void writeToConfigFile(EasyLoginConfig easyLoginConfig) {
        File configFile = FileUtil.file(CommonStaticRepo.GAME_DIR, CommonStaticRepo.configPath);
        try {
            configFile.createNewFile();
            new ObjectMapper(CommonStaticRepo.ymlFactory).writeValue(configFile, easyLoginConfig);
        } catch (IOException e) {
            log.atError().log("write config file error {}", e);
        }
    }

    public static EasyLoginConfig getDefaultConfig() {
        return new EasyLoginConfig(
                DataBaseType.SQLITE,
                CommonStaticRepo.defaultSqlitePath,
                false,
                new MailAccountEntry(),
                60 * 20L
        );
    }

}
