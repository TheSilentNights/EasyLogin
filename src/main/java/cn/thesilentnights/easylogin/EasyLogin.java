package cn.thesilentnights.easylogin;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.events.listener.ActionListener;
import cn.thesilentnights.easylogin.events.listener.Listener;
import cn.thesilentnights.easylogin.registrys.CommandRegistrar;
import cn.thesilentnights.easylogin.registrys.SDRegistry;
import cn.thesilentnights.easylogin.repo.CommonStaticRepo;
import cn.thesilentnights.easylogin.service.AccountService;
import cn.thesilentnights.easylogin.utils.PathAppender;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(value = CommonStaticRepo.MOD_ID)
public class EasyLogin {

    public EasyLogin() {
        new SDRegistry(MinecraftForge.EVENT_BUS);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EasyLoginConfig.SPEC);

        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER || !FMLLoader.isProduction()) {
            initServer();
        } else {
            initClient();
        }
    }

    private static void initServer() {
        new Listener();
        new ActionListener(MinecraftForge.EVENT_BUS);
        new CommandRegistrar(MinecraftForge.EVENT_BUS);
    }

    private static void initClient() {
        new CommandRegistrar(MinecraftForge.EVENT_BUS);
    }

}