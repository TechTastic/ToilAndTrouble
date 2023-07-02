package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.dimension.DimensionDefaults;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.techtastic.tat.block.entity.CandelabraBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class CandelabraBlock extends AbstractCandleBlock implements EntityBlock {
    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;

    public CandelabraBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(@NotNull BlockState blockState) {
        return List.of(
                new Vec3(0.5, 1.0, 0.5),
                new Vec3(0.5, 0.95, 0.2),
                new Vec3(0.5, 0.95, 0.8),
                new Vec3(0.2, 0.95, 0.5),
                new Vec3(0.8, 0.95, 0.5)
        );
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape = Shapes.join(Block.box(1.5, 0, 1.5, 14.5, 7, 14.5), Block.box(6.5, 7, 6.5, 9.5, 8, 9.5), BooleanOp.OR);

        return shape.optimize();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(LIT, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public void onProjectileHit(Level level, @NotNull BlockState blockState, @NotNull BlockHitResult blockHitResult, @NotNull Projectile projectile) {
        if (!level.isClientSide && projectile.isOnFire() && this.canBeLit(blockState))
            setLit(level, blockState, blockHitResult.getBlockPos(), true);

    }

    @Override
    public boolean canSurvive(@NotNull BlockState blockState, @NotNull LevelReader levelReader, BlockPos blockPos) {
        return Block.canSupportCenter(levelReader, blockPos.below(), Direction.UP);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!player.getAbilities().mayBuild)
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(interactionHand);

        if (stack.isEmpty() && blockState.getValue(LIT)) {
            extinguish(player, blockState, level, blockPos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (stack.is(Items.FLINT_AND_STEEL) && !blockState.getValue(LIT)) {
            stack.setDamageValue(stack.getDamageValue() + 1);
            setLit(level, blockState, blockPos, true);
            return InteractionResult.SUCCESS;
        } else if (stack.is(Items.FIRE_CHARGE) && !blockState.getValue(LIT)) {
            stack.shrink(1);
            setLit(level, blockState, blockPos, true);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    protected boolean canBeLit(BlockState blockState) {
        return !blockState.getValue(LIT);
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
        if (blockState.getValue(LIT)) {
            this.getParticleOffsets(blockState).forEach((vec3) -> {
                addParticlesAndSound(
                        level,
                        vec3.add(blockPos.getX(), blockPos.getY(), blockPos.getZ()),
                        random,
                        level.dimensionTypeRegistration().is(DimensionType.NETHER_LOCATION));
            });
        }
    }

    private static void addParticlesAndSound(Level level, Vec3 vec3, Random random, boolean isNether) {
        float f = random.nextFloat();
        if (f < 0.3F) {
            level.addParticle(ParticleTypes.SMOKE, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
            if (f < 0.17F)
                level.playLocalSound(vec3.x + 0.5, vec3.y + 0.5, vec3.z + 0.5, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
        }

        level.addParticle(
                isNether ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMALL_FLAME,
                vec3.x,
                vec3.y + (isNether ? 0.05 : 0.0),
                vec3.z,
                0.0,
                0.0,
                0.0
        );
    }

    public static void extinguish(@Nullable Player player, BlockState blockState, LevelAccessor levelAccessor, @NotNull BlockPos blockPos) {
        setLit(levelAccessor, blockState, blockPos, false);
        if (blockState.getBlock() instanceof CandelabraBlock block) {
            block.getParticleOffsets(blockState).forEach((vec3 ->
                    levelAccessor.addParticle(ParticleTypes.SMOKE, blockPos.getX() + vec3.x(), blockPos.getY() + vec3.y(), blockPos.getZ() + vec3.z(), 0.0, 0.10000000149011612, 0.0)));
        }

        levelAccessor.playSound(null, blockPos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        levelAccessor.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
    }

    private static void setLit(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos, boolean bl) {
        levelAccessor.setBlock(blockPos, blockState.setValue(LIT, bl), 11);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new CandelabraBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }
}
