package me.davipccunha.tests.moderation.api.model;

public class Mute extends Penalty {
    public Mute(String playerName, String executorName, String reason, long createdAt, long expiresAt) {
        super(playerName, executorName, reason, createdAt, expiresAt);
    }

    @Override
    public String getRestrictionMessage() {
        return String.format(
                "\n§c Você está mutado e não pode falar publicamente." +
                        "\n\n§7 Mutado por: §f%s." +
                        "\n§7 Motivo: §f%s" +
                        "\n§7 Expira em: §f%s" +
                        "\n ",
                this.getExecutorName(), this.getReason(), this.getFormattedRemainingTime());
    }
}