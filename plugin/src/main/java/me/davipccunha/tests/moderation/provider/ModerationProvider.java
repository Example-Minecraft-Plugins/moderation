package me.davipccunha.tests.moderation.provider;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.moderation.ModerationPlugin;
import me.davipccunha.tests.moderation.api.ModerationAPI;
import me.davipccunha.tests.moderation.api.model.Ban;
import me.davipccunha.tests.moderation.api.model.BanIP;
import me.davipccunha.tests.moderation.api.model.Mute;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class ModerationProvider implements ModerationAPI {
    private final ModerationPlugin plugin;

    @Override
    public @Nullable Mute getMute(String playerName) {
        return this.plugin.getMutesCache().get(playerName.toLowerCase());
    }

    @Override
    public @Nullable Ban getBan(String playerName) {
        return this.plugin.getBansCache().get(playerName.toLowerCase());
    }

    @Override
    public @Nullable BanIP getIPBan(String ip) {
        return this.plugin.getIpBansCache().get(ip);
    }

    @Override
    public boolean kick(String playerName, String reason) {
        final Player player = this.plugin.getServer().getPlayer(playerName);
        if (player == null) return false;

        player.kickPlayer(reason);
        return true;
    }

    @Override
    public Mute mute(String playerName, String executorName, String reason, long createdAt, long expiresAt) {
        final Mute mute = new Mute(playerName, executorName, reason, createdAt, expiresAt);
        this.plugin.getMutesCache().add(playerName.toLowerCase(), mute);
        return mute;
    }

    @Override
    public Ban ban(String playerName, String executorName, String reason, long createdAt, long expiresAt) {
        final Ban ban = new Ban(playerName, executorName, reason, createdAt, expiresAt);
        this.plugin.getBansCache().add(playerName.toLowerCase(), ban);
        return ban;
    }

    @Override
    public BanIP banIP(String ip, String executorName, String reason, long createdAt) {
        final BanIP banIP = new BanIP(ip, executorName, reason, createdAt);
        this.plugin.getIpBansCache().add(ip, banIP);
        return banIP;
    }

    @Override
    public void unmute(String playerName, String executorName) {
        this.plugin.getMutesCache().remove(playerName.toLowerCase());
    }

    @Override
    public void unban(String playerName, String executorName) {
        this.plugin.getBansCache().remove(playerName.toLowerCase());
    }

    @Override
    public void unbanIP(String ip, String executorName) {
        this.plugin.getIpBansCache().remove(ip);
    }

}
