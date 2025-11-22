package com.thesilentnights.configs;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.thesilentnights.repo.CommonStaticRepo;
import dev.architectury.platform.Platform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Data
@AllArgsConstructor
public class EasyLoginConfig {
    DataBaseType dataBaseType;
    String pathToDatabase;

    public static EasyLoginConfig readFromConfigFile() {
        File configFile = FileUtil.file(Platform.getGameFolder().toFile(), CommonStaticRepo.configPath);
        if (!configFile.exists()) {
            FileUtil.copyFile(ResourceUtil.getResourceObj(CommonStaticRepo.configResourcePath), configFile);
            try {
                JsonReader jsonReader = new JsonReader(new FileReader(configFile));
                EasyLoginConfig o = new Gson().fromJson(jsonReader, EasyLoginConfig.class);
                jsonReader.close();
                return o;
            } catch (JsonSyntaxException e) {
                log.atError().setCause(e).log("configuration is not valid");
            } catch (IOException e) {
                log.atError().log("error occurred while reading configuration {}", e);
            }
        }

        try {
            JsonReader jsonReader = new JsonReader(new FileReader(configFile));
            EasyLoginConfig o = new Gson().fromJson(jsonReader, EasyLoginConfig.class);
            jsonReader.close();
            return o;
        } catch (IOException e) {
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
                ).getAbsolutePath()
        );
    }

}
