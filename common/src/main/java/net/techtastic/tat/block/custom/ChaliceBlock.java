package net.techtastic.tat.block.custom;

import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.techtastic.tat.item.TATItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ChaliceBlock extends Block {
    public static final BooleanProperty SOUP = BooleanProperty.create("soup");

    public ChaliceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(6, 0, 6, 10, 9, 10);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        ItemStack chalice = new ItemStack(TATItems.CHALICE.get());

        if (blockState.getValue(SOUP))
            chalice.getOrCreateTag().putBoolean("ToilAndTrouble$soup", true);

        return chalice;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (!level.isClientSide) {
            if (blockState.getValue(SOUP) && stack.is(Items.GLASS_BOTTLE)) {
                stack.shrink(1);

                Inventory inv = player.getInventory();
                ItemStack drop = new ItemStack(TATItems.REDSTONE_SOUP.get(), 1);
                if (inv.getFreeSlot() == -1 && inv.getSlotWithRemainingSpace(drop) == -1)
                    level.addFreshEntity(new ItemEntity(
                            level,
                            blockPos.getX(),
                            blockPos.above().getY(),
                            blockPos.getZ(),
                            drop
                    ));
                else
                    inv.add(drop);
                level.setBlockAndUpdate(blockPos, blockState.setValue(SOUP, false));
            } else if (!blockState.getValue(SOUP) && stack.is(TATItems.REDSTONE_SOUP.get())) {
                stack.shrink(1);
                level.setBlockAndUpdate(blockPos, blockState.setValue(SOUP, true));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
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

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
        if (blockState.getValue(SOUP))
            level.addParticle(
                    ParticleTypes.ELECTRIC_SPARK,
                    0.5,
                    0.8,
                    0.5,
                    0.0,
                    0.0,
                    0.0
            );
    }
}
