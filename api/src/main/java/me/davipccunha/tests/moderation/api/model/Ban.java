package me.davipccunha.tests.moderation.api.model;

import lombok.Getter;

@Getter
public class Ban extends Penalty {
    private boolean permanent;

    public Ban(String playerName, String executorName, String reason, long createdAt, long expiresAt) {
        super(playerName, executorName, reason, createdAt, expiresAt);

        if (expiresAt <= 0) this.permanent = true;
    }

    @Override
    public String getRestrictionMessage() {
        return String.format(
                "§cVocê está banido do servidor" +
                        "\n\n§7Banido por: §f%s" +
                        "\n§7Motivo: §f%s" +
                        "\n§7Data: §f%s" +
                        "\n§7Expira em: §f%s",

                this.getExecutorName(), this.getReason(), this.getFormattedCreatedDate(), permanent ? "Nunca" : this.getFormattedRemainingTime());
    }
}
