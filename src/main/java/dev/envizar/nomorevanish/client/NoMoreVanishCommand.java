package dev.envizar.nomorevanish.client;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
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
                        .then(ClientCommandManager.literal("clear_data").executes(this::executeClearData))
                        .executes(context -> {
                            context.getSource().sendFeedback(Text.literal("NoMoreVanish by envizar"));
                            return 1;
                        })
                ));
    }

    Text noDataMessage = Text.literal("No data found.").formatted(Formatting.RED);

    private Text generatePlayerListText(Set<String> players) {
        return Text.literal(players.size() + " players: ").formatted(Formatting.YELLOW)
                .append("§f" + String.join("§r§7, §f", players));
    }

    private int executePlayerInfoUpdate(CommandContext<FabricClientCommandSource> context) {
        if (PlayerInfoStore.playerInfoUpdate.isEmpty()) {
            context.getSource().sendFeedback(noDataMessage);
        } else {
            context.getSource().sendFeedback(generatePlayerListText(PlayerInfoStore.playerInfoUpdate));
        }
        return 1;
    }

    private int executePlayerInfoRemove(CommandContext<FabricClientCommandSource> context) {
        if (PlayerInfoStore.playerInfoRemove.isEmpty()) {
            context.getSource().sendFeedback(noDataMessage);
        } else {
            Set<String> strings = new HashSet<>();
            for (UUID uuid : PlayerInfoStore.playerInfoRemove) {
                strings.add(uuid.toString());
            }
            context.getSource().sendFeedback(generatePlayerListText(strings));
        }
        return 1;
    }

    private int executePlayerTeams(CommandContext<FabricClientCommandSource> context) {
        if (PlayerInfoStore.playerTeams.isEmpty()) {
            context.getSource().sendFeedback(noDataMessage);
        } else {
            context.getSource().sendFeedback(generatePlayerListText(PlayerInfoStore.playerTeams));
        }
        return 1;
    }

    private int executeClearData(CommandContext<FabricClientCommandSource> context) {
        PlayerInfoStore.clear();
        context.getSource().sendFeedback(Text.literal("NoMoreVanish data cleared.").formatted(Formatting.GREEN));
        return 1;
    }

}
