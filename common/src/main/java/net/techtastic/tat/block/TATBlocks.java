package net.techtastic.tat.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.ToilAndTrouble;
import net.techtastic.tat.block.custom.*;
import net.techtastic.tat.world.feature.tree.AlderTreeGrower;
import net.techtastic.tat.world.feature.tree.HawthornTreeGrower;
import net.techtastic.tat.world.feature.tree.RowanTreeGrower;

public class TATBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.BLOCK_REGISTRY);

    public static final RegistrySupplier<Block> CAST_IRON_OVEN = registerBlock("cast_iron_oven",
            new CastIronOvenBlock(BlockBehaviour.Properties.of(Material.METAL).strength(4.0f).requiresCorrectToolForDrops()
                    .noOcclusion().lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0)));

    public static final RegistrySupplier<Block> FUME_FUNNEL = registerBlock("fume_funnel",
            new FumeFunnelBlock(BlockBehaviour.Properties.of(Material.METAL).strength(4.0f).requiresCorrectToolForDrops().noOcclusion()
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0)));

    public static final RegistrySupplier<Block> FILTERED_FUME_FUNNEL = registerBlock("filtered_fume_funnel",
            new FilteredFumeFunnelBlock(BlockBehaviour.Properties.of(Material.METAL).strength(4.0f).requiresCorrectToolForDrops().noOcclusion()
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0)));

    public static final RegistrySupplier<Block> ALTAR = registerBlock("altar",
            new AltarBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));

    public static final RegistrySupplier<Block> BLODDY_ROSE = registerBlock("bloody_rose",
            new BloodyRoseBlock(BlockBehaviour.Properties.copy(Blocks.DANDELION)));

    //////////////////////////
    //                      //
    //  ROWAN WOOD STUFFS   //
    //                      //
    //////////////////////////

    public static RegistrySupplier<Block> ROWAN_LOG = registerBlock("rowan_log",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static RegistrySupplier<Block> ROWAN_WOOD = registerBlock("rowan_wood",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> STRIPPED_ROWAN_LOG = registerBlock("stripped_rowan_log",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> STRIPPED_ROWAN_WOOD = registerBlock("stripped_rowan_wood",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> ROWAN_PLANKS = registerBlock("rowan_planks",
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 20;
                }
            });

    public static final RegistrySupplier<Block> ROWAN_LEAVES = registerBlock("rowan_leaves",
            new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).noOcclusion()) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 30;
                }
            });

    public static final RegistrySupplier<Block> ROWAN_SAPLING = registerBlock("rowan_sapling",
            new SaplingBlock(new RowanTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noOcclusion()));

    public static final RegistrySupplier<Block> ROWAN_PRESSURE_PLATE = registerBlock("rowan_pressure_plate",
            new LockedPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));

    public static final RegistrySupplier<Block> ROWAN_BUTTON = registerBlock("rowan_button",
            new LockedStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).noCollission()));

    public static final RegistrySupplier<Block> ROWAN_FENCE = registerBlock("rowan_fence",
            new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)) {
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

    public static final RegistrySupplier<Block> ROWAN_FENCE_GATE = registerBlock("rowan_fence_gate",
            new LockedFenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE)) {
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

    public static final RegistrySupplier<Block> ROWAN_SLAB = TATBlocks.registerBlock("rowan_slab",
            new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)) {
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

    /////////////////////////////
    //                         //
    //  HAWTHORN WOOD STUFFS   //
    //                         //
    /////////////////////////////

    public static final RegistrySupplier<Block> HAWTHORN_PRESSURE_PLATE = registerBlock("hawthorn_pressure_plate",
            new LockedPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));

    public static final RegistrySupplier<Block> HAWTHORN_BUTTON = registerBlock("hawthorn_button",
            new LockedStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).noCollission()));

    public static final RegistrySupplier<Block> HAWTHORN_FENCE = registerBlock("hawthorn_fence",
            new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)) {
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

    public static final RegistrySupplier<Block> HAWTHORN_FENCE_GATE = registerBlock("hawthorn_fence_gate",
            new LockedFenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE)) {
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

    public static final RegistrySupplier<Block> HAWTHORN_SLAB = TATBlocks.registerBlock("hawthorn_slab",
            new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)) {
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

    public static RegistrySupplier<Block> HAWTHORN_LOG = registerBlock("hawthorn_log",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static RegistrySupplier<Block> HAWTHORN_WOOD = registerBlock("hawthorn_wood",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> STRIPPED_HAWTHORN_LOG = registerBlock("stripped_hawthorn_log",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> STRIPPED_HAWTHORN_WOOD = registerBlock("stripped_hawthorn_wood",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> HAWTHORN_PLANKS = registerBlock("hawthorn_planks",
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
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

    public static final RegistrySupplier<Block> HAWTHORN_LEAVES = registerBlock("hawthorn_leaves",
            new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).noOcclusion()) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 30;
                }
            });

    public static final RegistrySupplier<Block> HAWTHORN_SAPLING = registerBlock("hawthorn_sapling",
            new SaplingBlock(new HawthornTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noOcclusion()));

    //////////////////////////
    //                      //
    //  ALDER WOOD STUFFS   //
    //                      //
    //////////////////////////

    public static final RegistrySupplier<Block> ALDER_PRESSURE_PLATE = registerBlock("alder_pressure_plate",
            new LockedPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));

    public static final RegistrySupplier<Block> ALDER_BUTTON = registerBlock("alder_button",
            new LockedStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).noCollission()));

    public static final RegistrySupplier<Block> ALDER_FENCE = registerBlock("alder_fence",
            new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)) {
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

    public static final RegistrySupplier<Block> ALDER_FENCE_GATE = registerBlock("alder_fence_gate",
            new LockedFenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE)) {
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

    public static final RegistrySupplier<Block> ALDER_SLAB = TATBlocks.registerBlock("alder_slab",
            new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)) {
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

    public static RegistrySupplier<Block> ALDER_LOG = registerBlock("alder_log",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static RegistrySupplier<Block> ALDER_WOOD = registerBlock("alder_wood",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> STRIPPED_ALDER_LOG = registerBlock("stripped_alder_log",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> STRIPPED_ALDER_WOOD = registerBlock("stripped_alder_wood",
            new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            });

    public static final RegistrySupplier<Block> ALDER_PLANKS = registerBlock("alder_planks",
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
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

    public static final RegistrySupplier<Block> ALDER_LEAVES = registerBlock("alder_leaves",
            new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)) {
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 30;
                }
            });

    public static final RegistrySupplier<Block> ALDER_SAPLING = registerBlock("alder_sapling",
            new SaplingBlock(new AlderTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    private static RegistrySupplier<Block> registerBlock(String name, Block block) {
        TATItems.registerItem(name, new BlockItem(block, new Item.Properties().tab(TATItems.TAB)));
        return BLOCKS.register(name, () -> block);
    }

    private static RegistrySupplier<Block> registerBlockWithoutItem(String name, Block block) {
        return BLOCKS.register(name, () -> block);
    }

    public static void register() {
        BLOCKS.register();
        TATStairBlocks.register();
    }
}
