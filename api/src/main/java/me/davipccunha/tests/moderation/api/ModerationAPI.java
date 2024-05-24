package me.davipccunha.tests.moderation.api;

import me.davipccunha.tests.moderation.api.model.Ban;
import me.davipccunha.tests.moderation.api.model.BanIP;
import me.davipccunha.tests.moderation.api.model.Mute;

public interface ModerationAPI {
    Mute getMute(String playerName);

    Ban getBan(String playerName);

    BanIP getIPBan(String ip);

    boolean kick(String playerName, String reason);

    Mute mute(String playerName, String executorName, String reason, long createdAt, long expiresAt);

    Ban ban(String playerName, String executorName, String reason, long createdAt, long expiresAt);

    BanIP banIP(String ip, String executorName, String reason, long createdAt);

    void unmute(String playerName, String executorName);

    void unban(String playerName, String executorName);

    void unbanIP(String ip, String executorName);
}
