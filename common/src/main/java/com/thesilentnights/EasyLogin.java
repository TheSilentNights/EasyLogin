package com.thesilentnights;

import cn.hutool.core.io.FileUtil;
import com.thesilentnights.configs.Config;
import com.thesilentnights.service.PlayerLoginAuth;
import com.thesilentnights.sql.DatabaseChecker;
import com.thesilentnights.sql.DatabaseProvider;
import com.thesilentnights.sql.SqlLite;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;

public final class EasyLogin {

    public static void init(Config config) {
        //init server side database'
        if (Platform.getEnv() == EnvType.SERVER || Platform.isDevelopmentEnvironment()) {
            //init database
            DatabaseProvider databaseProvider = SqlLite.init(FileUtil.file(Platform.getGameFolder().toFile(),"/easylogin/database.db"));
            new DatabaseChecker(databaseProvider.getConnection()).checkAndRepairTable();
            //init services
            PlayerLoginAuth.init(databaseProvider);
            // test
            if (Platform.isDevelopmentEnvironment()) {
                test();
            }
        }
    }


    private static void test() {
        //test

    }
}

