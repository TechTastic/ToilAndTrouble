package net.techtastic.tat.augments;

import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.techtastic.tat.api.altar.augment.IAltarAugment;
import net.techtastic.tat.block.custom.ChaliceBlock;

public class ChaliceAltarAugment implements IAltarAugment {
    private final BlockState state;

    public ChaliceAltarAugment(BlockState state) {
        this.state = state;
    }

    @Override
    public String getType() {
        return "chalice";
    }

    @Override
    public int getTypePriority() {
        return state.getValue(ChaliceBlock.SOUP) ? 5 : 10;
    }

    @Override
    public boolean matches(IAltarAugment augment) {
        return augment instanceof ChaliceAltarAugment;
    }

    @Override
    public double modifyMaxAltarPower(double initPower) {
        return state.getValue(ChaliceBlock.SOUP) ? initPower * 2 : initPower * 3;
    }
}
