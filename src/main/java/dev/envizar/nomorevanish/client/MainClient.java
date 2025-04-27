package dev.envizar.nomorevanish.client;

import net.fabricmc.api.ClientModInitializer;

public class MainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        NoMoreVanishCommand noMoreVanishCommand = new NoMoreVanishCommand();
        noMoreVanishCommand.registerCommand();

    }
}
