package com.thesilentnights.events.ievents;

import com.thesilentnights.pojo.PlayerAccount;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

public class EasyLoginEvents {
    //use loop!
    public static final Event<PlayerLoginEvent> ON_LOGIN = EventFactory.createLoop();
    public static final Event<PlayerLogoutEvent> ON_LOGOUT = EventFactory.createLoop();

    public interface PlayerLoginEvent {
        void onLogin(PlayerAccount account, ServerPlayer serverPlayer);
    }

    public interface PlayerLogoutEvent {
        void onLogout(PlayerAccount account, ServerPlayer serverPlayer);
    }
}
