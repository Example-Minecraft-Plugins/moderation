package me.davipccunha.tests.moderation.command;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.moderation.ModerationPlugin;
import me.davipccunha.tests.moderation.api.event.PlayerUnmuteEvent;
import me.davipccunha.tests.moderation.api.model.Mute;
import me.davipccunha.utils.cache.RedisCache;
import me.davipccunha.utils.messages.ErrorMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class DessilenciarCommand implements CommandExecutor {
    private final ModerationPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("moderation.unmute")) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUso: /dessilenciar <jogador>");
            return false;
        }

        final RedisCache<Mute> cache = this.plugin.getMutesCache();
        final String playerName = args[0];

        if (!cache.has(playerName)) {
            sender.sendMessage("§cEste jogador não está silenciado.");
            return true;
        }

        final Mute mute = cache.get(playerName);

        cache.remove(playerName);

        if (mute != null) {
            final PlayerUnmuteEvent unmuteEvent = new PlayerUnmuteEvent(mute);
            Bukkit.getPluginManager().callEvent(unmuteEvent);
        }

        sender.sendMessage(String.format("§aVocê dessilenciou §f%s §acom sucesso.", playerName));

        return true;
    }
}
