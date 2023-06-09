package net.techtastic.tat.sources;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.api.altar.source.IAltarSource;
import net.techtastic.tat.api.altar.source.IAltarSourceProvider;
import net.techtastic.tat.block.custom.AltarBlock;
import net.techtastic.tat.block.entity.AltarBlockEntity;

import java.util.Optional;

public class TATAltarSourceProvider implements IAltarSourceProvider {
    @Override
    public Optional<IAltarSource> getAltarSource(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        BlockState state = level.getBlockState(pos);
        if (be instanceof AltarBlockEntity altar && altar.isMaster()) {
            return Optional.of(new AltarBlockEntitySource(altar));
        }
        else
            return Optional.empty();
    }
}
