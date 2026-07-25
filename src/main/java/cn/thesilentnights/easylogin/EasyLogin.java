package cn.thesilentnights.easylogin;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.events.listener.ActionListener;
import cn.thesilentnights.easylogin.events.listener.Listener;
import cn.thesilentnights.easylogin.registrys.CommandRegistrar;
import cn.thesilentnights.easylogin.registrys.SDRegistry;
import cn.thesilentnights.easylogin.repo.CommonStaticRepo;
import cn.thesilentnights.easylogin.utils.LogUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = CommonStaticRepo.MOD_ID,dist = Dist.DEDICATED_SERVER)
public class EasyLogin {

    @SuppressWarnings("removal")
    public EasyLogin(ModContainer modContainer) {
        new SDRegistry(NeoForge.EVENT_BUS);

        modContainer.registerConfig(Type.SERVER, EasyLoginConfig.SPEC);

        if (FMLEnvironment.getDist() == Dist.DEDICATED_SERVER || !FMLEnvironment.isProduction()) {
            initServer();
        } else {
            initClient();
        }
    }

    private static void initServer() {
        new Listener();
        new ActionListener(NeoForge.EVENT_BUS);
        new CommandRegistrar(NeoForge.EVENT_BUS);
    }

    private static void initClient() {
        new CommandRegistrar(NeoForge.EVENT_BUS);
    }

}