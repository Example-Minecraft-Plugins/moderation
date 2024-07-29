package me.davipccunha.tests.moderation.command;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.moderation.ModerationPlugin;
import me.davipccunha.tests.moderation.api.model.Ban;
import me.davipccunha.tests.moderation.api.model.Mute;
import me.davipccunha.tests.moderation.api.model.Penalty;
import me.davipccunha.utils.messages.ErrorMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class ChecarPunicaoCommand implements CommandExecutor {
    private final ModerationPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("moderation.checkban")) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUso: /checarpunicao <jogador>");
            return false;
        }

        final String playerName = args[0];

        final Ban ban = this.plugin.getBansCache().get(playerName.toLowerCase());
        if (ban != null) {
            sender.sendMessage("§eO jogador §f" + playerName + " §eestá banido.");
            sender.sendMessage(this.getPenaltyMessage(ban));
            return true;
        }

        final Mute mute = this.plugin.getMutesCache().get(playerName.toLowerCase());
        if (mute != null) {
            sender.sendMessage("§eO jogador §f" + playerName + " §eestá mutado.");
            sender.sendMessage(this.getPenaltyMessage(mute));
            return true;
        }

        sender.sendMessage("§aO jogador §f" + playerName + " §anão tem nenhuma punição ativa.");

        return true;
    }

    private String[] getPenaltyMessage(Penalty penalty) {
        return new String[] {
                " ",
                "§7 Punido por: §f" + penalty.getExecutorName(),
                "§7 Motivo: §f" + penalty.getReason(),
                "§7 Data: §f" + penalty.getFormattedCreatedDate(),
                "§7 Expira em: §f" + penalty.getFormattedRemainingTime(),
                " "
        };
    }
}
