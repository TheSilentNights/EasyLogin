package cn.thesilentnights.easylogin.registrys;

import cn.thesilentnights.easylogin.data.PasswordData;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SDRegistry {

    public SDRegistry(IEventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void registerPasswordData(ServerStartedEvent event) {
        PasswordData.refreshLevel(event.getServer().overworld());
    }

}
