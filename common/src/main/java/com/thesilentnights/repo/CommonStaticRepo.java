package com.thesilentnights.repo;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

public class CommonStaticRepo {
    public static final String MOD_ID = "easylogin";
    public static final String configPath = "/easylogin/config.yml";
    public static final String defaultSqlitePath = "/easylogin/playerAccounts.db";
    public static final String configResourcePath = "./config.yml";
    public static final String readmePath = "/easylogin/readme.txt";
    public static final YAMLFactory ymlFactory = YAMLFactory.builder().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER).build();
}
