package com.thesilentnights.configs;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.thesilentnights.repo.CommonStaticRepo;
import dev.architectury.platform.Platform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.File;

@Slf4j
@Data
@AllArgsConstructor
public class EasyLoginConfig {
    DataBaseType dataBaseType;
    String pathToDatabase;
    boolean enableKickOther;

    public static EasyLoginConfig readFromConfigFile() {
        File configFile = FileUtil.file(Platform.getGameFolder().toFile(), CommonStaticRepo.configPath);
        File readme = FileUtil.file(Platform.getGameFolder().toFile(),CommonStaticRepo.readmePath);
        //create readme if not exists
        if (!readme.exists()){
            FileUtil.copyFile(ResourceUtil.getResourceObj("./readme.txt"),readme);
        }
        //create config.json
        if (!configFile.exists()) {
            FileUtil.copyFile(ResourceUtil.getResourceObj(CommonStaticRepo.configResourcePath), configFile);
        }

        //read from configs
        try {
            return new ObjectMapper().readValue(configFile, EasyLoginConfig.class);
        } catch (JacksonException e) {
            log.atError().log("file not found");
            return getDefaultConfig();
        } catch (JsonIOException | JsonSyntaxException e) {
            log.atError().setCause(e).log("error loading configuration");
            log.atError().log("using default config instead");
            return getDefaultConfig();
        }
    }

    public static EasyLoginConfig getDefaultConfig() {
        return new EasyLoginConfig(
                DataBaseType.SQLITE,
                FileUtil.file(
                        Platform.getGameFolder().toFile(),
                        "easylogin/playerAccounts.db"
                ).getAbsolutePath(),
                false
        );
    }

}
