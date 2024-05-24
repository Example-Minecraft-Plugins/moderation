package me.davipccunha.tests.moderation.listener;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.moderation.ModerationPlugin;
import me.davipccunha.tests.moderation.api.model.Ban;
import me.davipccunha.tests.moderation.api.model.BanIP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

@RequiredArgsConstructor
public class AsyncPlayerPreLoginListener implements Listener {
    private final ModerationPlugin plugin;

    @EventHandler
    private void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        final String name = event.getName().toLowerCase();
        final Ban ban = plugin.getBansCache().get(name);

        if (ban != null) {
            final boolean isExpired = System.currentTimeMillis() >= ban.getExpiresAt() && !ban.isPermanent();

            if (isExpired) {
                plugin.getBansCache().remove(name);
            } else {
                final String message = ban.getRestrictionMessage();
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);
                return;
            }
        }

        final String ip = event.getAddress().getHostAddress();
        final BanIP banIP = plugin.getIpBansCache().get(ip);

        if (banIP != null) {
            final String message = banIP.getRestrictionMessage();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, message);
        }
    }
}
