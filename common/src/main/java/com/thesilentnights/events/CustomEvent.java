package com.thesilentnights.events;

import com.thesilentnights.pojo.PlayerAccount;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public class CustomEvent {
    public static Event<LoginEvent> LOGIN_EVENT = EventFactory.createEventResult();

    public interface LoginEvent {
        void onLogin(PlayerAccount account, ServerPlayerEntity entity);
    }
}
