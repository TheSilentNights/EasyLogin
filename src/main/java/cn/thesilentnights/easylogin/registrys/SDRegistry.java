package cn.thesilentnights.easylogin.registrys;

import cn.thesilentnights.easylogin.data.PasswordData;
import net.minecraft.client.gui.font.providers.UnihexProvider.Dimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SDRegistry {

    public SDRegistry(IEventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void registerPasswordData(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level && level.dimension() == Level.OVERWORLD) {
            PasswordData.refreshLevel(level);
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        LevelAccessor level = event.getLevel();
        if (level instanceof ServerLevel serverLevel && serverLevel.dimension() == Level.OVERWORLD) {
            PasswordData.invalidate();
        }
    }

}
