package cn.thesilentnights.easylogin.repo;

import cn.thesilentnights.easylogin.EasyLogin;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class CommonStaticRepo {
        public static final String MOD_ID = "easylogin";
        public static final String GAME_DIR = FMLPaths.GAMEDIR.get().toAbsolutePath().toString();
}
