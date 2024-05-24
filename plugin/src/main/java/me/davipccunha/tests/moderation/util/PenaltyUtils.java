package me.davipccunha.tests.moderation.util;

import me.davipccunha.tests.moderation.api.model.TimeUnit;
import me.davipccunha.utils.messages.ErrorMessages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PenaltyUtils {
    public static String extractReason(String[] args) {
        final List<String> argsList = new ArrayList<>(Arrays.asList(args));

        // Remove the player name from the reason
        argsList.remove(0);

        final int timeIndex = argsList.indexOf("--t") + 1;

        if (timeIndex > 0 && timeIndex < argsList.size())
            argsList.remove(timeIndex);

        final List<String> filteredArgs = argsList.stream().filter(arg -> !arg.startsWith("--")).collect(Collectors.toList());

        return !filteredArgs.isEmpty() ? String.join(" ", filteredArgs) : "Motivo não especificado.";
    }

    public static HashMap<String, Boolean> extractFlags(String[] args) {
        final List<String> flags = Arrays.stream(args).filter(arg -> arg.startsWith("--")).collect(Collectors.toList());
        final HashMap<String, Boolean> flagsMap = new HashMap<>();

        flagsMap.put("permanent", !flags.contains("--t"));
        flagsMap.put("silent", flags.contains("--s"));

        return flagsMap;
    }

    // TODO: Is sending error message in this function the correct way to handle it?
    public static long extractTime(CommandSender sender, String[] args) {
        final int timeIndex = Arrays.asList(args).indexOf("--t") + 1;
        if (timeIndex >= args.length) {
            sender.sendMessage("§cVocê precisa especificar um tempo após a flag --t.");
            return 0;
        }

        final String timeInput = args[timeIndex];

        final TimeUnit timeUnit = TimeUnit.fromSuffix(timeInput.charAt(timeInput.length() - 1));
        if (timeUnit == null) {
            sender.sendMessage("§cUnidade de tempo inválida.");
            return 0;
        }

        final long timeValue = NumberUtils.toLong(timeInput.substring(0, timeInput.length() - 1));
        if (timeValue <= 0) {
            sender.sendMessage(ErrorMessages.INVALID_AMOUNT.getMessage());
            return 0;
        }

        return getExpiresAt(timeValue, timeUnit);
    }

    private static long getExpiresAt(long time, TimeUnit timeUnit) {
        return System.currentTimeMillis() + time * timeUnit.getMilliseconds();
    }
}
