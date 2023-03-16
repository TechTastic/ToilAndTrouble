package net.techtastic.tat.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.techtastic.tat.TATTags;

import java.util.Optional;
import java.util.Random;

public class MutandisItem extends Item {
    public MutandisItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();

        if (!level.isClientSide()) return super.useOn(useOnContext);

        BlockPos targetPos = useOnContext.getClickedPos();
        BlockState targetState = level.getBlockState(targetPos);
        Player player = useOnContext.getPlayer();
        TagKey<Block> MUTANDIS = TATTags.Blocks.MUTANDIS;

        System.err.println("Hit Block: " + targetState.getBlock());
        System.err.println("Hit Pos: " + targetPos);
        System.err.println("Hit Face: " + useOnContext.getClickedFace());

        // Is this block in mutandis tag?
        if (!targetState.is(MUTANDIS)) return super.useOn(useOnContext);
        Optional<HolderSet.Named<Block>> tagList = Registry.BLOCK.getTag(MUTANDIS);

        // Is there a tagList?
        if (!tagList.isPresent()) return super.useOn(useOnContext);
        Optional<Holder<Block>> newBlockOpt = tagList.flatMap(list -> list.getRandomElement(new Random()));

        // Is there a newBlockOpt?
        if (!newBlockOpt.isPresent()) return super.useOn(useOnContext);

        Block newBlock = newBlockOpt.get().value();
        if (newBlock.defaultBlockState().is(Blocks.WITHER_ROSE)) return super.useOn(useOnContext);

        targetState.getBlock().destroy(level, targetPos, targetState);

        BlockState newState = newBlock.defaultBlockState();

        if (isWaterlogged(newState.getBlock())) newState.setValue(BlockStateProperties.WATERLOGGED, false);

        if (newState.getBlock() instanceof VineBlock) switch (useOnContext.getHorizontalDirection()) {
            case SOUTH -> newState.setValue(VineBlock.SOUTH, true);
            case EAST -> newState.setValue(VineBlock.EAST, true);
            case WEST -> newState.setValue(VineBlock.WEST, true);
            default -> newState.setValue(VineBlock.NORTH, true);
        }

        InteractionResult result = ((BlockItem) newState.getBlock().asItem()).place(new BlockPlaceContext(useOnContext));
        if (!result.equals(InteractionResult.SUCCESS)) level.setBlockAndUpdate(targetPos, newState);

        if (player != null && !player.isCreative()) useOnContext.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }

    private static boolean isWaterlogged(Block block) {
        return block instanceof SimpleWaterloggedBlock ||
                block instanceof CoralBlock ||
                block instanceof SeaPickleBlock;
    }
}
