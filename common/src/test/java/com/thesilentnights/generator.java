package com.thesilentnights;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.thesilentnights.configs.DataBaseType;
import com.thesilentnights.configs.EasyLoginConfig;
import com.thesilentnights.repo.CommonStaticRepo;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class generator {
    @Test
    public void generateJsonConfig() throws IOException {
        Gson gson = new Gson();
        File tempFile = new File("./config.json");
        tempFile.createNewFile();
        JsonWriter jsonWriter = gson.newJsonWriter(new FileWriter(tempFile));
        gson.toJson(new EasyLoginConfig(DataBaseType.SQLITE, CommonStaticRepo.configPath),EasyLoginConfig.class,jsonWriter);
        jsonWriter.close();
    }
}
