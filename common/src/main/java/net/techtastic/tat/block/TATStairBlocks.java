package net.techtastic.tat.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.ToilAndTrouble;

public class TATStairBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.BLOCK_REGISTRY);

    public static final RegistrySupplier<Block> ALDER_STAIRS = registerBlock("alder_stairs",
            new StairBlock(TATBlocks.ALDER_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> HAWTHORN_STAIRS = registerBlock("hawthorn_stairs",
            new StairBlock(TATBlocks.HAWTHORN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> ROWAN_STAIRS = registerBlock("rowan_stairs",
            new StairBlock(TATBlocks.ROWAN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    private static RegistrySupplier<Block> registerBlock(String name, Block block) {
        TATItems.registerItem(name, new BlockItem(block, new Item.Properties().tab(TATItems.TAB)));
        return BLOCKS.register(name, () -> block);
    }

    public static void register() {
        BLOCKS.register();
    }
}
