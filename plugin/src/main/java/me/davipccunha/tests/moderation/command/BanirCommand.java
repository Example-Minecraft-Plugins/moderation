package me.davipccunha.tests.moderation.command;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.moderation.ModerationPlugin;
import me.davipccunha.tests.moderation.api.event.PlayerBanEvent;
import me.davipccunha.tests.moderation.api.model.Ban;
import me.davipccunha.tests.moderation.api.model.TimeUnit;
import me.davipccunha.tests.moderation.util.PenaltyUtils;
import me.davipccunha.utils.cache.RedisCache;
import me.davipccunha.utils.messages.ErrorMessages;
import me.davipccunha.utils.player.PlayerUtils;
import me.davipccunha.utils.server.ServerUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class BanirCommand implements CommandExecutor {
    private final ModerationPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("moderation.ban")) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUso: /banir <jogador> [motivo] [--t <tempo>] [--s]");
            return false;
        }

        final RedisCache<Ban> cache = this.plugin.getBansCache();
        final BanList banList = this.plugin.getServer().getBanList(BanList.Type.NAME);

        final String playerName = PlayerUtils.correctNameCasing(args[0]);

        final HashMap<String, Boolean> flags = PenaltyUtils.extractFlags(args);
        final boolean permanent = flags.getOrDefault("permanent", false);
        final boolean silent = flags.getOrDefault("silent", false);

        long expiresAt = 0;
        if (!permanent)
            expiresAt = PenaltyUtils.extractTime(sender, args);

        if (expiresAt < 0) return true;

        final String reason = PenaltyUtils.extractReason(args);

        final Ban ban = new Ban(playerName, sender.getName(), reason, System.currentTimeMillis(), expiresAt);

        final boolean isAlreadyBanned = cache.has(playerName) || banList.isBanned(playerName);

        cache.add(playerName.toLowerCase(), ban);

        final PlayerBanEvent banEvent = new PlayerBanEvent(ban);
        Bukkit.getPluginManager().callEvent(banEvent);

        if (!silent) {
            final long remainingTime = expiresAt - System.currentTimeMillis();
            final String remainingTimeFormatted = TimeUnit.formatTime(remainingTime);
            final List<String> message = List.of(
                    "§8 " + playerName + " §ffoi banido por " + sender.getName() + "§f.",
                    "§7 Motivo: §f" + reason,
                    "§7 Expira em: §f" + (permanent ? "Nunca" : remainingTimeFormatted)
            );

            ServerUtils.messageEveryone(message, true);
        }

        final Player player = this.plugin.getServer().getPlayer(playerName);
        if (player != null)
            player.kickPlayer("§cVocê foi banido do servidor.");

        final String executorMessage = isAlreadyBanned ?
                String.format("§f%s §ejá estava banido mas o banimento foi atualizado.", playerName) :
                "§aVocê baniu §f" + playerName + " §acom sucesso.";

        sender.sendMessage(executorMessage);

        return true;
    }

    private long getExpiresAt(long time, TimeUnit timeUnit) {
        return System.currentTimeMillis() + time * timeUnit.getMilliseconds();
    }
}
