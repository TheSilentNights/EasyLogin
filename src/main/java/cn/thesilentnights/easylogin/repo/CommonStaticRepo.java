package cn.thesilentnights.easylogin.repo;

import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.thesilentnights.easylogin.EasyLogin;

public class CommonStaticRepo {
    public static final String MOD_ID = "easylogin";
    public static final String GAME_DIR = FMLPaths.GAMEDIR.get().toAbsolutePath().toString();
    public static final String TABLE_NAME = "accounts";
    public static final Logger logger = LogManager.getLogger(EasyLogin.class);
}