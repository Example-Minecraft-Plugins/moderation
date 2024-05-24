package me.davipccunha.tests.moderation.command;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.moderation.ModerationPlugin;
import me.davipccunha.tests.moderation.api.event.PlayerUnbanEvent;
import me.davipccunha.tests.moderation.api.model.Ban;
import me.davipccunha.utils.cache.RedisCache;
import me.davipccunha.utils.messages.ErrorMessages;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class DesbanirCommand implements CommandExecutor {
    private final ModerationPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("moderation.unban")) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUso: /desbanir <jogador>");
            return false;
        }

        final RedisCache<Ban> cache = this.plugin.getBansCache();
        final BanList banList = this.plugin.getServer().getBanList(BanList.Type.NAME);
        final String playerName = args[0];

        if (!cache.has(playerName) && !banList.isBanned(playerName)) {
            sender.sendMessage("§cEste jogador não está banido.");
            return true;
        }

        final Ban ban = cache.get(playerName);

        cache.remove(playerName);
        banList.pardon(playerName);

        if (ban != null) {
            final PlayerUnbanEvent unbanEvent = new PlayerUnbanEvent(ban);
            Bukkit.getPluginManager().callEvent(unbanEvent);
        }

        sender.sendMessage(String.format("§aVocê desbaniu §f%s §acom sucesso.", playerName));

        return false;
    }
}
