package net.techtastic.tat.forge;

import net.techtastic.tat.ToilAndTroubleExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ToilAndTroubleExpectPlatformImpl {
    /**
     * This is our actual method to {@link ToilAndTroubleExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
