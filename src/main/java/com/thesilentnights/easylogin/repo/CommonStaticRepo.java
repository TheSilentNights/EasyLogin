package com.thesilentnights.easylogin.repo;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.thesilentnights.easylogin.EasyLogin;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonStaticRepo {
    public static final String MOD_ID = "easylogin";
    public static final String GAME_DIR = FMLPaths.GAMEDIR.get().toAbsolutePath().toString();
    public static final String TABLE_NAME = "accounts";
    public static final String configPath = "/easylogin/config.yml";
    public static final String defaultSqlitePath = "/easylogin/playerAccounts.db";
    public static final String readmePath = "/easylogin/readme.txt";
    public static final String readmeResourcePath = "./readme.txt";
    public static final YAMLFactory ymlFactory = YAMLFactory.builder().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER).enable(YAMLGenerator.Feature.ALLOW_LONG_KEYS).build();
    public static final Logger log = LogManager.getLogger(EasyLogin.class);
}