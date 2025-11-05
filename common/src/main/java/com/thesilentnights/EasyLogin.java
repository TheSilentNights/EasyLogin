package com.thesilentnights;

import cn.hutool.core.io.FileUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.thesilentnights.commands.ICommands;
import com.thesilentnights.configs.Config;
import com.thesilentnights.events.CommonEvents;
import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.service.PlayerLoginAuth;
import com.thesilentnights.sql.DatabaseChecker;
import com.thesilentnights.sql.DatabaseProvider;
import com.thesilentnights.sql.SqlLite;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.EnvType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

@Slf4j
public final class EasyLogin {

    public static void init(Config config) {
        //init server side database'
        if (Platform.getEnv() == EnvType.SERVER || Platform.isDevelopmentEnvironment()) {
            //init database
            DatabaseProvider databaseProvider = SqlLite.init(FileUtil.file(Platform.getGameFolder().toFile(),"/easylogin/playerAccounts.db"));
            new DatabaseChecker(databaseProvider.getConnection()).checkAndRepairTable();
            //init services
            PlayerLoginAuth.init(databaseProvider);

            //register commands
            CommandRegistrationEvent.EVENT.register((CommandDispatcher<ServerCommandSource> dispatcher, CommandManager.RegistrationEnvironment selection)->{
                ICommands.registerCommands(dispatcher);
            });

            //register events
            CommonEvents.register();

            // test
            if (Platform.isDevelopmentEnvironment()) {
                test(databaseProvider);
            }
        }
    }


    private static void test(DatabaseProvider provider) {
        //test
        provider.saveAuth(new PlayerAccount("admin", "admin", "127.0.0.1", 0, 0, 0, "world", "admin", "admin", System.currentTimeMillis()));
        provider.getAuth("admin").ifPresent(playerAccount -> log.info(playerAccount.toString()));
    }
}

