package net.techtastic.tat.augments;

import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.api.altar.augment.IAltarAugment;

public class ArthanaAltarAugment implements IAltarAugment {

    @Override
    public String getType() {
        return "arthana";
    }

    @Override
    public int getTypePriority() {
        return 10;
    }

    @Override
    public boolean matches(IAltarAugment augment) {
        return augment instanceof ArthanaAltarAugment;
    }

    @Override
    public double modifyAltarRange(double initRange) {
        return initRange * 2;
    }
}
