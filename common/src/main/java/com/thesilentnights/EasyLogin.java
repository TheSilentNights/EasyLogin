package com.thesilentnights;

import com.mojang.brigadier.CommandDispatcher;
import com.thesilentnights.commands.EasyLoginCommands;
import com.thesilentnights.configs.SpringConfig;
import com.thesilentnights.events.CommonEvents;
import com.thesilentnights.events.ServerSideEvents;
import com.thesilentnights.events.ievents.EasyLoginEvents;
import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.repo.BlockPosRepo;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.sql.DatabaseChecker;
import com.thesilentnights.sql.DatabaseProvider;
import com.thesilentnights.task.TickTimerManager;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.EnvType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.UUID;

@Slf4j
public final class EasyLogin {
    public static final ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

    public static void init(){
        //init server side database'
        if (Platform.getEnv() == EnvType.SERVER) {
            DatabaseProvider databaseProvider = context.getBean(DatabaseProvider.class);
            //init database
            try {
                context.getBean(DatabaseChecker.class).checkAndRepairTable();
            } catch (SQLException e) {
                log.warn("if you see this message.It's likely that the sql initialization has failed.",e);
            }

            //register events
            context.getBean(CommonEvents.class).register();
            context.getBean(ServerSideEvents.class).register();

            // test
            log.info("test");
            if (Platform.isDevelopmentEnvironment()) {
                test(context);
            }
        }

        CommandRegistrationEvent.EVENT.register((CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection) -> {
            EasyLoginCommands.register(dispatcher);
        });

        if (Platform.isDevelopmentEnvironment()){
            devServerSideLoader(context);
        }
    }

    /***
     * just for loading server side environment for development
     * ignore it
     */
    public static void devServerSideLoader(ApplicationContext context){

        try {
            context.getBean(DatabaseChecker.class).checkAndRepairTable();

            log.info("debug init events");
            //register events
            context.getBean(CommonEvents.class).register();
            context.getBean(ServerSideEvents.class).register();

            // test
            log.info("debug test");
            if (Platform.isDevelopmentEnvironment()) {
                test(context);
            }

            EasyLoginEvents.ON_LOGIN.register(((account, serverPlayer) -> {
                BlockPosRepo.removeBlockPos(account.getUsername());
                TickTimerManager.cancel(serverPlayer.getUUID());
                PlayerCache.addAccount(account);
            }));
            EasyLoginEvents.ON_LOGOUT.register(((account, serverPlayer) -> {
                PlayerCache.dropAccount(serverPlayer.getUUID());
                BlockPosRepo.removeBlockPos(account.getUsername());
            }));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }






    private static void test(ApplicationContext context) {
        DatabaseProvider provider = context.getBean(DatabaseProvider.class);
        //test
        provider.saveAuth(new PlayerAccount("admin", "admin", "127.0.0.1", 0, 0, 0, "world", UUID.randomUUID().toString(), "admin", System.currentTimeMillis()));
        provider.getAuthByName("admin").ifPresent(playerAccount -> log.info(playerAccount.toString()));
    }
}

