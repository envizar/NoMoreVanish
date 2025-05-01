package dev.envizar.nomorevanish.client;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class NoMoreVanishCommand {

    public void registerCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("nomorevanish")
                        .then(ClientCommandManager.literal("updates").executes(this::executePlayerInfoUpdate))
                        .then(ClientCommandManager.literal("removals").executes(this::executePlayerInfoRemove))
                        .then(ClientCommandManager.literal("teams").executes(this::executePlayerTeams))
                        .then(ClientCommandManager.literal("teams_npc").executes(this::executeNpcTeams))
                        .then(ClientCommandManager.literal("clear_data").executes(this::executeClearData))
                        .executes(context -> {
                            context.getSource().sendFeedback(Text.literal("NoMoreVanish by envizar"));
                            return 1;
                        })
                ));
    }

    private Text generatePlayerListText(Set<String> players, CommandContext<FabricClientCommandSource> context) {

        if (players.isEmpty()) return Text.literal("No players found.").formatted(Formatting.RED);

        Text res = Text.literal(players.size() + " players: ").formatted(Formatting.YELLOW);
        Text delim = Text.literal(", ").formatted(Formatting.GRAY);

        // get online players
        List<String> onlinePlayers = new ArrayList<>();
        try {
            for (PlayerListEntry player : context.getSource().getClient().getNetworkHandler().getPlayerList()) {
                onlinePlayers.add(player.getProfile().getName());
                onlinePlayers.add(player.getProfile().getId().toString());
            }
        } catch (NullPointerException ignored) {
            context.getSource().sendError(Text.literal("exception"));
        }

        // append all players from list to final component
        boolean noDelim = true;
        for (String playerName : players) {
            Formatting formatting = onlinePlayers.contains(playerName) ? Formatting.GREEN : Formatting.WHITE;
            if (noDelim) {
                noDelim = false;
            } else {
                res = res.copy().append(delim);
            }
            res = res.copy().append(Text.literal(playerName).formatted(formatting));
        }

        return res;
    }

    private int executePlayerInfoUpdate(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(generatePlayerListText(PlayerInfoStore.playerInfoUpdate, context));
        return 1;
    }

    private int executePlayerInfoRemove(CommandContext<FabricClientCommandSource> context) {
        Set<String> strings = new HashSet<>();
        for (UUID uuid : PlayerInfoStore.playerInfoRemove) {
            strings.add(uuid.toString());
        }
        context.getSource().sendFeedback(generatePlayerListText(strings, context));
        return 1;
    }

    private int executePlayerTeams(CommandContext<FabricClientCommandSource> context) {
        return executeTeams(context, PlayerInfoStore.teams);
    }

    private int executeNpcTeams(CommandContext<FabricClientCommandSource> context) {
        return executeTeams(context, PlayerInfoStore.teamsNpc);
    }

    private int executeTeams(CommandContext<FabricClientCommandSource> context, Map<String, Set<String>> teams) {
        if (teams.isEmpty()) {
            context.getSource().sendError(Text.literal("No teams found."));
        } else {
            for (String teamName : teams.keySet()) {
                context.getSource().sendFeedback(
                        Text.literal("Team " + teamName + ": ").formatted(Formatting.GOLD).append(
                                generatePlayerListText(teams.get(teamName), context)
                        )
                );
            }
        }
        return 1;
    }

    private int executeClearData(CommandContext<FabricClientCommandSource> context) {
        PlayerInfoStore.clear();
        context.getSource().sendFeedback(Text.literal("NoMoreVanish data cleared.").formatted(Formatting.GREEN));
        return 1;
    }

}
