package com.thesilentnights.easylogin;

import com.thesilentnights.easylogin.configs.EasyLoginConfig;
import com.thesilentnights.easylogin.configs.SpringConfig;
import com.thesilentnights.easylogin.repo.CommonStaticRepo;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Mod(value = CommonStaticRepo.MOD_ID)
public class EasyLogin {

    public EasyLogin() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        if (!FMLLoader.isProduction()) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EasyLoginConfig.config);
        } else {
            ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, EasyLoginConfig.config);
        }
    }

}