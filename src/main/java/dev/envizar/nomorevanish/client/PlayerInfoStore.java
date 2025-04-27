package dev.envizar.nomorevanish.client;

import java.util.*;

public class PlayerInfoStore {

    public static Set<String> playerInfoUpdate = new HashSet<>();
    public static Set<UUID> playerInfoRemove = new HashSet<>();
    public static Set<String> playerTeams = new HashSet<>();

    public static void clear() {
        playerInfoUpdate.clear();
        playerInfoRemove.clear();
        playerTeams.clear();
    }

}
