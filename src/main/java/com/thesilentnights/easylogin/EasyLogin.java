package com.thesilentnights.easylogin;

import cn.hutool.core.io.FileUtil;
import com.thesilentnights.easylogin.configs.EasyLoginConfig;
import com.thesilentnights.easylogin.dsl.Dependencies;
import com.thesilentnights.easylogin.repo.CommonStaticRepo;
import com.thesilentnights.easylogin.service.*;
import com.thesilentnights.easylogin.sql.DataSource;
import com.thesilentnights.easylogin.sql.DatabaseChecker;
import com.thesilentnights.easylogin.sql.DatasourceConfigs;
import com.thesilentnights.easylogin.utils.PathAppender;
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

    }

}