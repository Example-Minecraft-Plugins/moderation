package me.davipccunha.tests.moderation.command;

import me.davipccunha.tests.moderation.util.PenaltyUtils;
import me.davipccunha.utils.messages.ErrorMessages;
import me.davipccunha.utils.server.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class ExpulsarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("moderation.kick")) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUso: /expulsar <jogador> [motivo]");
            return false;
        }

        final String playerName = args[0];
        final Player player = Bukkit.getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(ErrorMessages.PLAYER_NOT_ONLINE.getMessage());
            return true;
        }

        final String reason = PenaltyUtils.extractReason(args);
        final HashMap<String, Boolean> flags = PenaltyUtils.extractFlags(args);
        final boolean silent = flags.getOrDefault("silent", false);

        player.kickPlayer(this.kickScreenMessage(sender.getName(), reason));

        if (!silent) {
            final List<String> message = this.kickPublicMessage(sender.getName(), player.getName(), reason);
            ServerUtils.messageEveryone(message, true);
        }

        sender.sendMessage("§aVocê expulsou §f " + player.getName() + " §acom sucesso.");

        return true;
    }

    private String kickScreenMessage(String executor, String reason) {
        return String.format("§cVocê foi expulso do servidor" +
                "\n\n§7Expulso por: §f%s" +
                "\n§7Motivo: §f%s", executor, reason);
    }

    private List<String> kickPublicMessage(String executor, String playerName, String reason) {
        return List.of(
                "§8 " + playerName + " §ffoi expulso por " + executor + "§f.",
                "§7 Motivo: §f" + reason
        );
    }
}
