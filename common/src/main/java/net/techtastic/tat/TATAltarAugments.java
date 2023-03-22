package net.techtastic.tat;

import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.techtastic.tat.dataloader.altar.augment.AltarAugmentBlocksInfoProvider;
import net.techtastic.tat.dataloader.altar.augment.AugmentType;

public class TATAltarAugments implements AltarAugmentBlocksInfoProvider {
    @Override
    public int getPriority() {
        return 30;
    }

    @Override
    public AugmentType getAugmentType(BlockState state) {
        if (state.getBlock() instanceof AbstractCandleBlock) {
            return AugmentType.LIGHT;
        }

        return AltarAugmentBlocksInfoProvider.super.getAugmentType(state);
    }

    @Override
    public int getTypePriority(BlockState state) {
        if (state.getBlock() instanceof AbstractCandleBlock) {
            return (state.is(Blocks.CANDLE_CAKE) || state.getValue(CandleBlock.CANDLES) <= 1) ?
                    8 : 8 - state.getValue(CandleBlock.CANDLES);
        }

        return AltarAugmentBlocksInfoProvider.super.getTypePriority(state);
    }

    @Override
    public double modifyRate(BlockState state, double initRate) {
        if (state.getBlock() instanceof AbstractCandleBlock && state.getValue(BlockStateProperties.LIT)) {
            return (state.is(Blocks.CANDLE_CAKE) || state.getValue(CandleBlock.CANDLES) <= 1) ?
                    initRate + .5 : initRate + state.getValue(CandleBlock.CANDLES) * .5;
        }

        return AltarAugmentBlocksInfoProvider.super.modifyRate(state, initRate);
    }

    @Override
    public double modifyRange(BlockState state, double initRange) {
        return AltarAugmentBlocksInfoProvider.super.modifyRange(state, initRange);
    }

    @Override
    public double modifyPower(BlockState state, double initPower) {
        return AltarAugmentBlocksInfoProvider.super.modifyPower(state, initPower);
    }

    public static void register() {
    }
}
