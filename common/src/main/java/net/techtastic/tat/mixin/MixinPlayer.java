package net.techtastic.tat.mixin;

import net.minecraft.world.entity.player.Player;
import net.techtastic.tat.util.IWitchcraftPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class MixinPlayer implements IWitchcraftPlayer {
    @Override
    public double getExtraBrewChance() {
        return 0;
    }
}