package com.thesilentnights.configs;

import cn.hutool.core.io.FileUtil;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.thesilentnights.repo.CommonStaticRepo;
import dev.architectury.platform.Platform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
@Data
@AllArgsConstructor
public class EasyLoginConfig {
    DataBaseType dataBaseType;
    String pathToDatabase;

    public static EasyLoginConfig readFromConfigFile() {
        File configFile = FileUtil.file(Platform.getGameFolder().toFile(), CommonStaticRepo.configPath);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                Gson gson = new Gson();
                JsonWriter jsonWriter = new JsonWriter(new FileWriter(configFile));
                gson.toJson(getDefaultConfig(), EasyLoginConfig.class, jsonWriter);
                jsonWriter.close();
            } catch (IOException e) {
                log.atError().log("failed to create config file in {} {}", configFile.getAbsoluteFile(), e);
                log.atError().log("using default config instead");
            }
            return getDefaultConfig();
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
            log.atError().log("error loading configuration {}",e);
            log.atError().log("using default config instead {}",e);
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
