package com.thesilentnights;

import com.thesilentnights.configs.DataBaseType;
import com.thesilentnights.configs.EasyLoginConfig;
import com.thesilentnights.repo.CommonStaticRepo;
import org.junit.Test;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class generator {
    @Test
    public void generateJsonConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.writeValue(new File("./config.json"),new EasyLoginConfig(
                DataBaseType.SQLITE,
                CommonStaticRepo.defaultSqlitePath,
                true
        ));
    }
}
