package dev.envizar.nomorevanish.mixin;

import dev.envizar.nomorevanish.client.PlayerInfoStore;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.packet.s2c.login.LoginDisconnectS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLoginNetworkHandler.class)
public abstract class PlayerJoinQuitMixin {

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void onDisconnect(LoginDisconnectS2CPacket packet, CallbackInfo ci) {
        PlayerInfoStore.clear();
    }

    @Inject(method = "onSuccess", at = @At("HEAD"))
    private void onDisconnect(LoginSuccessS2CPacket packet, CallbackInfo ci) {
        PlayerInfoStore.clear();
    }

}
