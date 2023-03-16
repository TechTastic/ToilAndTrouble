package net.techtastic.tat.fabric;

import net.techtastic.tat.ToilAndTroubleExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ToilAndTroubleExpectPlatformImpl {
    /**
     * This is our actual method to {@link ToilAndTroubleExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
