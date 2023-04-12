package net.techtastic.tat.augments;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.api.altar.augment.IAltarAugment;
import net.techtastic.tat.api.altar.augment.IAltarAugmentProvider;
import net.techtastic.tat.block.custom.ArthanaBlock;

import java.util.Optional;

public class TATAugmentProvider implements IAltarAugmentProvider {
    @Override
    public Optional<IAltarAugment> getAugment(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof CandleCakeBlock)
            return Optional.of(new CandleCakeAltarAugment(state));
        else if (state.getBlock() instanceof CandleBlock)
            return Optional.of(new CandleAltarAugment(state));
        else if (state.getBlock() instanceof TorchBlock)
            return Optional.of(new TorchAltarAugment(state));
        else if (state.getBlock() instanceof AbstractSkullBlock)
            return Optional.of(new SkullAltarAugment(state));
        else if (state.getBlock() instanceof ArthanaBlock)
            return Optional.of(new ArthanaAltarAugment());
        else
            return Optional.empty();
    }
}
