package com.thesilentnights;

import cn.hutool.core.io.FileUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.thesilentnights.commands.ICommands;
import com.thesilentnights.configs.Config;
import com.thesilentnights.events.CommonEvents;
import com.thesilentnights.events.ServerSideEvents;
import com.thesilentnights.events.ievents.EasyLoginEvents;
import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.repo.BlockPosRepo;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.service.PlayerLoginAuth;
import com.thesilentnights.sql.DatabaseChecker;
import com.thesilentnights.sql.DatabaseProvider;
import com.thesilentnights.sql.SqlLite;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.EnvType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.sql.SQLException;

@Slf4j
public final class EasyLogin {

    public static void init(Config config){
        //init server side database'
        if (Platform.getEnv() == EnvType.SERVER) {
            //init database
            DatabaseProvider databaseProvider = SqlLite.init(FileUtil.file(Platform.getGameFolder().toFile(), "playerAccounts.db"));
            try {
                new DatabaseChecker(databaseProvider.getConnection()).checkAndRepairTable();
            } catch (SQLException e) {
                log.warn("if you see this message.It's likely that the sql initialization has failed.",e);
            }

            log.info("init services");
            //init services
            PlayerLoginAuth.init(databaseProvider);

            log.info("init events");
            //register events
            CommonEvents.register();
            ServerSideEvents.register();

            // test
            log.info("test");
            if (Platform.isDevelopmentEnvironment()) {
                test(databaseProvider);
            }
        }

        CommandRegistrationEvent.EVENT.register((CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection) -> {
            ICommands.registerCommands(dispatcher);
        });

        if (Platform.isDevelopmentEnvironment()){
            devServerSideLoader();
        }
    }

    /***
     * just for loading server side environment for development
     * ignore it
     */
    public static void devServerSideLoader(){

        //init database
        DatabaseProvider databaseProvider = SqlLite.init(FileUtil.file(Platform.getGameFolder().toFile(), "playerAccounts.db"));
        try {
            new DatabaseChecker(databaseProvider.getConnection()).checkAndRepairTable();
        } catch (SQLException e) {
            log.warn("if you see this message.It's likely that the sql initialization has failed.",e);
        }

        log.info("debug init services");
        //init services
        PlayerLoginAuth.init(databaseProvider);

        log.info("debug init events");
        //register events
        CommonEvents.register();
        ServerSideEvents.register();

        // test
        log.info("debug test");
        if (Platform.isDevelopmentEnvironment()) {
            test(databaseProvider);
        }

        EasyLoginEvents.ON_LOGIN.register(((account, serverPlayer) -> {
            BlockPosRepo.removeBlockPos(account.getUsername());
            PlayerCache.addAccount(account);
        }));
        EasyLoginEvents.ON_LOGOUT.register(((account, serverPlayer)  -> {
            PlayerCache.dropAccount(serverPlayer.getGameProfile().getName());
            BlockPosRepo.removeBlockPos(account.getUsername());
        }));
    }




    private static void test(DatabaseProvider provider) {
        //test
        provider.saveAuth(new PlayerAccount("admin", "admin", "127.0.0.1", 0, 0, 0, "world", "admin", "admin", System.currentTimeMillis()));
        provider.getAuth("admin").ifPresent(playerAccount -> log.info(playerAccount.toString()));
    }
}

