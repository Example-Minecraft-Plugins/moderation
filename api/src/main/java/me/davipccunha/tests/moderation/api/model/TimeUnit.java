package me.davipccunha.tests.moderation.api.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Getter
public enum TimeUnit {
    MINUTES("m", 60 * 1000),
    HOURS("h", 60 * 60 * 1000),
    DAYS("d", 60 * 60 * 24 * 1000);

    private final String suffix;
    private final int milliseconds;

    public static TimeUnit fromSuffix(String suffix) {
        for (TimeUnit unit : values()) {
            if (unit.getSuffix().equalsIgnoreCase(suffix)) return unit;
        }

        return null;
    }

    public static TimeUnit fromSuffix(char suffix) {
        return fromSuffix(String.valueOf(suffix));
    }

    public static String formatTime(long time) {
        long days = time / DAYS.getMilliseconds();
        time -= days * DAYS.getMilliseconds();

        long hours = time / HOURS.getMilliseconds();
        time -= hours * HOURS.getMilliseconds();

        long minutes = time / MINUTES.getMilliseconds();
        time -= minutes * MINUTES.getMilliseconds();

        long seconds = time / 1000;

        StringBuilder builder = new StringBuilder();

        if (days > 0) builder.append(days).append(" dias, ");
        if (hours > 0) builder.append(hours).append(" horas, ");
        if (minutes > 0) builder.append(minutes).append(" minutos, ");

        builder.append(seconds).append(" segundos ");

        return builder.toString().trim();
    }

    public static String formatDate(long time) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final Date date = new Date(time);
        return dateFormat.format(date);
    }
}
