package dev.envizar.nomorevanish.mixin;

import com.mojang.authlib.GameProfile;
import dev.envizar.nomorevanish.client.PlayerInfoStore;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class PlayerInfoMixin {

    @Shadow @Nullable public abstract PlayerListEntry getPlayerListEntry(UUID uuid);

    @Inject(method = "onPlayerList", at = @At("HEAD"))
    private void onPlayerList(PlayerListS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        for (PlayerListS2CPacket.Entry entry : packet.getEntries()) {
            GameProfile profile = entry.profile();
            if (profile == null) continue;
            PlayerInfoStore.playerInfoUpdate.add(profile.getName());
        }
    }

    @Inject(method = "onPlayerRemove", at = @At("HEAD"))
    private void onPlayerRemove(PlayerRemoveS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        PlayerInfoStore.playerInfoRemove.addAll(packet.profileIds());
    }

    @Inject(method = "onTeam", at = @At("HEAD"))
    private void onTeam(TeamS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        PlayerInfoStore.playerTeams.addAll(packet.getPlayerNames());
    }

}
