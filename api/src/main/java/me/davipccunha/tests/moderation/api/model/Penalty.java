package me.davipccunha.tests.moderation.api.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;

@RequiredArgsConstructor
@Getter
public abstract class Penalty {
    private final String target, executorName;
    private final String reason;
    private final long createdAt, expiresAt;

    public abstract String getRestrictionMessage();

    public String getFormattedRemainingTime() {
        final long remainingTime = this.expiresAt - System.currentTimeMillis();

        return remainingTime <= 0 ? "Nunca" : TimeUnit.formatTime(this.expiresAt - System.currentTimeMillis());
    }

    public String getFormattedCreatedDate() {
        return TimeUnit.formatDate(this.createdAt);
    }
}
