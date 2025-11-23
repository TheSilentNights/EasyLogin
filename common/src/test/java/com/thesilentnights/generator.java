package com.thesilentnights;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesilentnights.configs.DataBaseType;
import com.thesilentnights.configs.EasyLoginConfig;
import com.thesilentnights.repo.CommonStaticRepo;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class generator {
    @Test
    public void generateJsonConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper(CommonStaticRepo.ymlFactory);
        mapper.writeValue(new File("./config.yml"),new EasyLoginConfig(
                DataBaseType.SQLITE,
                CommonStaticRepo.defaultSqlitePath,
                true
        ));

    }
}
