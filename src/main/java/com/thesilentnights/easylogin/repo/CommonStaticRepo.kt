package com.thesilentnights.easylogin.repo;

import com.thesilentnights.easylogin.EasyLogin;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonStaticRepo {
    public static final String MOD_ID = "easylogin";
    public static final String GAME_DIR = FMLPaths.GAMEDIR.get().toAbsolutePath().toString();
    public static final String TABLE_NAME = "accounts";
    public static final Logger log = LogManager.getLogger(EasyLogin.class);
}