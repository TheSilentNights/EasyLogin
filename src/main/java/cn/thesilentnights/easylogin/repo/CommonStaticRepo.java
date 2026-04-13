package cn.thesilentnights.easylogin.repo;

import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.thesilentnights.easylogin.EasyLogin;

public class CommonStaticRepo {
    public static final String MOD_ID = "easylogin";
    public static final String GAME_DIR = FMLPaths.GAMEDIR.get().toAbsolutePath().toString();
    public static final String TABLE_NAME = "accounts";
    public static final String configPath = "/easylogin/config.yml";
    public static final String defaultSqlitePath = "/easylogin/playerAccounts.db";
    public static final String readmePath = "/easylogin/readme.txt";
    public static final String readmeResourcePath = "./readme.txt";
    public static final Logger log = LogManager.getLogger(EasyLogin.class);
}