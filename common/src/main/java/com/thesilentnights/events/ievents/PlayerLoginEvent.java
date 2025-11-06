package com.thesilentnights.events.ievents;

import com.thesilentnights.pojo.PlayerAccount;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;


public interface PlayerLoginEvent{
    Event <PlayerLoginEvent> ON_LOGIN = EventFactory.createEventResult();

    void onLogin(PlayerAccount account, ServerPlayer serverPlayer);
}
