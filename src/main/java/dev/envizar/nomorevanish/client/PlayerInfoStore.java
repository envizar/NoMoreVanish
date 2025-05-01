package dev.envizar.nomorevanish.client;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlayerInfoStore {

    public static Set<String> playerInfoUpdate = new HashSet<>();
    public static Set<UUID> playerInfoRemove = new HashSet<>();
    public static Map<String, Set<String>> teams = new HashMap<>();
    public static Map<String, Set<String>> teamsNpc = new HashMap<>();

    public static void clear() {
        playerInfoUpdate.clear();
        playerInfoRemove.clear();
        teams.clear();
        teamsNpc.clear();
    }

    public static String formatName(String name) {
        if (name.contains("§")) {
            name = name.replaceAll("§[0-9A-z]", "") + "§c*§r";
        }
        return name;
    }

    public static Set<String> formatNames(Iterable<String> names) {
        Set<String> res = new HashSet<>();
        for (String name : names) {
            String formattedName = formatName(name);
            res.add(formattedName);
        }
        return res;
    }

}
