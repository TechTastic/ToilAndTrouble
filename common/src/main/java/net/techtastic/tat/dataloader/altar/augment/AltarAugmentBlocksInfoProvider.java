package net.techtastic.tat.dataloader.altar.augment;

import net.minecraft.world.level.block.state.BlockState;

public interface AltarAugmentBlocksInfoProvider {
    default int getPriority() {
        return 0;
    }

    default AugmentType getAugmentType(BlockState state) {
        return AugmentType.NONE;
    }

    default int getTypePriority(BlockState state) {
        return 0;
    }

    default double modifyPower(BlockState state, double initPower) {
        return initPower;
    }

    default double modifyRate(BlockState state, double initRate) {
        return initRate;
    }

    default double modifyRange(BlockState state, double initRange) {
        return initRange;
    }

    default AltarAugmentBlocksInfo getInfo(BlockState state) {
        return null;
    }

    default boolean hasInfo(BlockState state) {
        return false;
    }
}
