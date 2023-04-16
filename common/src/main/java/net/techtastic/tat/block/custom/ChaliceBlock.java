package net.techtastic.tat.block.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChaliceBlock extends Block {
    public static final BooleanProperty SOUP = BooleanProperty.create("soup");

    public ChaliceBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        CompoundTag tag = blockPlaceContext.getItemInHand().getOrCreateTag();
        if (tag.contains("ToilAndTrouble$soup") && tag.getBoolean("ToilAndTrouble$soup"))
            return this.defaultBlockState().setValue(SOUP, true);
        return this.defaultBlockState().setValue(SOUP, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SOUP);
    }
}
