package me.davipccunha.tests.moderation.api.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.moderation.api.model.Mute;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class PlayerMuteEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Mute mute;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
