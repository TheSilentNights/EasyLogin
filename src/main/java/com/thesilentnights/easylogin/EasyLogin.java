package com.thesilentnights.easylogin;

import cn.hutool.core.io.FileUtil;
import com.thesilentnights.easylogin.commands.EasyLoginCommands;
import com.thesilentnights.easylogin.commands.admin.ByPass;
import com.thesilentnights.easylogin.commands.admin.EmailTest;
import com.thesilentnights.easylogin.commands.admin.PlayerInfoCommands;
import com.thesilentnights.easylogin.commands.admin.TeleportToOfflinePlayer;
import com.thesilentnights.easylogin.commands.common.*;
import com.thesilentnights.easylogin.configs.EasyLoginConfig;
import com.thesilentnights.easylogin.dsl.Dependencies;
import com.thesilentnights.easylogin.events.listener.ActionListener;
import com.thesilentnights.easylogin.events.listener.Listener;
import com.thesilentnights.easylogin.registrys.CommandRegistrar;
import com.thesilentnights.easylogin.repo.CommonStaticRepo;
import com.thesilentnights.easylogin.service.*;
import com.thesilentnights.easylogin.sql.DataSource;
import com.thesilentnights.easylogin.sql.DatabaseChecker;
import com.thesilentnights.easylogin.sql.DatasourceConfigs;
import com.thesilentnights.easylogin.utils.PathAppender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(value = CommonStaticRepo.MOD_ID)
public class EasyLogin {

    public EasyLogin() {
        if (!FMLLoader.isProduction()) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EasyLoginConfig.config);
        } else {
            ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, EasyLoginConfig.config);
        }

        Dependencies.register(IEventBus.class, MinecraftForge.EVENT_BUS);
        Dependencies.register(
                DataSource.class, new DataSource(() -> {
                    switch (EasyLoginConfig.dataBaseType.get()) {
                        case SQLITE -> {
                            return DatasourceConfigs.generateSqliteDataSource(
                                    FileUtil.file(
                                            PathAppender.append(
                                                    EasyLoginConfig.pathToDatabase.get()
                                            )));
                        }

                        case MYSQL -> {
                            return DatasourceConfigs.generateMySqlDataSource(EasyLoginConfig.pathToDatabase.get());
                        }

                        default -> throw new IllegalArgumentException("Invalid database type");
                    }

                })
        );
        new DatabaseChecker(Dependencies.getBean(DataSource.class)).checkDatabase();

        //init services
        Dependencies.register(CommandRejectionService.class, new CommandRejectionService());
        Dependencies.register(AccountService.class, new AccountService(Dependencies.getBean(DataSource.class)));
        Dependencies.register(LoginService.class, new LoginService(Dependencies.getBean(AccountService.class)));
        Dependencies.register(
                PreLoginService.class,
                new PreLoginService(
                        Dependencies.getBean(AccountService.class),
                        Dependencies.getBean(LoginService.class)
                )
        );
        Dependencies.register(
                EmailService.class,
                new EmailService(
                        Dependencies.getBean(AccountService.class),
                        Dependencies.getBean(LoginService.class)
                )
        );
        Dependencies.register(
                PasswordRecoveryService.class,
                new PasswordRecoveryService(
                        Dependencies.getBean(LoginService.class),
                        Dependencies.getBean(EmailService.class),
                        Dependencies.getBean(AccountService.class)
                )
        );
        Dependencies.register(
                ChangePasswordService.class,
                new ChangePasswordService(
                        Dependencies.getBean(LoginService.class),
                        Dependencies.getBean(AccountService.class)
                )
        );

        //init listener
        new ActionListener(Dependencies.getBean(IEventBus.class), Dependencies.getBean(CommandRejectionService.class));
        new Listener(Dependencies.getBean(AccountService.class), Dependencies.getBean(LoginService.class), Dependencies.getBean(PreLoginService.class));

        //init  commands
        new CommandRegistrar(
                new EasyLoginCommands(
                        new Login(Dependencies.getBean(LoginService.class)),
                        new Registrar(Dependencies.getBean(LoginService.class)),
                        new Logout(Dependencies.getBean(LoginService.class)),
                        new ChangePassword(Dependencies.getBean(ChangePasswordService.class)),
                        new Recover(Dependencies.getBean(PasswordRecoveryService.class)),
                        new ByPass(Dependencies.getBean(LoginService.class)),
                        new TeleportToOfflinePlayer(Dependencies.getBean(AccountService.class), Dependencies.getBean(LoginService.class)),
                        new EmailTest(Dependencies.getBean(LoginService.class)),
                        new PlayerInfoCommands(Dependencies.getBean(AccountService.class), Dependencies.getBean(LoginService.class))
                ), Dependencies.getBean(IEventBus.class)
        );

    }

}