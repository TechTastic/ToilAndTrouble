package net.techtastic.tat.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.techtastic.tat.ToilAndTrouble;

public class ToilAndTroubleFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ToilAndTrouble.initClient();
    }
}
