package me.davipccunha.tests.moderation.api.model;

public class BanIP extends Penalty {
    public BanIP(String ip, String executorName, String reason, long createdAt) {
        super(ip, executorName, reason, createdAt, -1L);
    }

    @Override
    public String getRestrictionMessage() {
        return String.format(
                "§cVocê foi banido do servidor." +
                        "\n\n§7Banido por: §f%s." +
                        "\n§7Motivo: §f%s",
                this.getExecutorName(), this.getReason());
    }
}
