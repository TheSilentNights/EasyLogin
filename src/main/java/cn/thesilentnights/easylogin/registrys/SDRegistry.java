package cn.thesilentnights.easylogin.registrys;

import cn.thesilentnights.easylogin.data.PasswordData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;
import net.minecraftforge.fmlserverevents.ServerLifecycleEvent;

public class SDRegistry {

    public SDRegistry(IEventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void registerPasswordData(FMLServerStartedEvent event) {
        PasswordData.refreshLevel(event.getServer().overworld());
    }

}
