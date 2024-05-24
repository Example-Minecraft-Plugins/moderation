package me.davipccunha.tests.moderation.command;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.moderation.ModerationPlugin;
import me.davipccunha.tests.moderation.api.event.PlayerMuteEvent;
import me.davipccunha.tests.moderation.api.model.Mute;
import me.davipccunha.tests.moderation.api.model.TimeUnit;
import me.davipccunha.tests.moderation.util.PenaltyUtils;
import me.davipccunha.utils.cache.RedisCache;
import me.davipccunha.utils.messages.ErrorMessages;
import me.davipccunha.utils.player.PlayerUtils;
import me.davipccunha.utils.server.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class SilenciarCommand implements CommandExecutor {
    private final ModerationPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("moderation.mute")) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUso: /silenciar <jogador> <--t <tempo>> [motivo] [--s]");
            return false;
        }

        final RedisCache<Mute> cache = this.plugin.getMutesCache();

        final String playerName = PlayerUtils.correctNameCasing(args[0]);

        final HashMap<String, Boolean> flags = PenaltyUtils.extractFlags(args);
        final boolean permanent = flags.getOrDefault("permanent", false);
        final boolean silent = flags.getOrDefault("silent", false);

        if (permanent) {
            sender.sendMessage("§cSilenciamentos não podem ser permanentes.");
            return true;
        }

        final long expiresAt = PenaltyUtils.extractTime(sender, args);

        if (expiresAt <= 0) return true;

        final String reason = PenaltyUtils.extractReason(args);

        final Mute mute = new Mute(playerName, sender.getName(), reason, System.currentTimeMillis(), expiresAt);

        final boolean isAlreadyMuted = cache.has(playerName);

        cache.add(playerName.toLowerCase(), mute);

        final PlayerMuteEvent muteEvent = new PlayerMuteEvent(mute);
        Bukkit.getPluginManager().callEvent(muteEvent);

        if (!silent) {
            final long remainingTime = expiresAt - System.currentTimeMillis();
            final String remainingTimeFormatted = TimeUnit.formatTime(remainingTime);
            final List<String> message = List.of(
                    "§8 " + playerName + " §ffoi silenciado por " + sender.getName() + "§f.",
                    "§7 Motivo: §f" + reason,
                    "§7 Expira em: §f" + remainingTimeFormatted
            );

            ServerUtils.messageEveryone(message, true);
        }

        final Player player = this.plugin.getServer().getPlayer(playerName);
        if (player != null)
            player.sendMessage("§cVocê foi silenciado e não pode falar nos chats públicos.");

        final String executorMessage = isAlreadyMuted ?
                String.format("§f%s §ejá estava silenciado mas o silenciamento foi atualizado.", playerName) :
                "§aVocê silenciou §f" + playerName + " §acom sucesso.";

        sender.sendMessage(executorMessage);

        return true;
    }
}
