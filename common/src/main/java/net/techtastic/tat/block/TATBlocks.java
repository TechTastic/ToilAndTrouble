package net.techtastic.tat.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.techtastic.tat.api.RitualType;
import net.techtastic.tat.block.custom.CauldronBlock;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.ToilAndTrouble;
import net.techtastic.tat.block.custom.*;
import net.techtastic.tat.item.custom.ChalkBlockItem;
import net.techtastic.tat.world.feature.tree.AlderTreeGrower;
import net.techtastic.tat.world.feature.tree.HawthornTreeGrower;
import net.techtastic.tat.world.feature.tree.RowanTreeGrower;

import java.util.function.Supplier;

public class TATBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.BLOCK_REGISTRY);
    private static final DeferredRegister<Block> BLOCKS_WITHOUT_ITEMS = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.BLOCK_REGISTRY);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Block> CAST_IRON_OVEN = BLOCKS.register("cast_iron_oven",
            () -> new CastIronOvenBlock(BlockBehaviour.Properties.of(Material.METAL).strength(4.0f).requiresCorrectToolForDrops()
                    .noOcclusion().lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0)));

    public static final RegistrySupplier<Block> FUME_FUNNEL = BLOCKS.register("fume_funnel",
            () -> new FumeFunnelBlock(BlockBehaviour.Properties.of(Material.METAL).strength(4.0f).requiresCorrectToolForDrops().noOcclusion()
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0)));

    public static final RegistrySupplier<Block> FILTERED_FUME_FUNNEL = BLOCKS.register("filtered_fume_funnel",
            () -> new FilteredFumeFunnelBlock(BlockBehaviour.Properties.of(Material.METAL).strength(4.0f).requiresCorrectToolForDrops().noOcclusion()
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0)));

    public static final RegistrySupplier<Block> ALTAR = BLOCKS.register("altar",
            () -> new AltarBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));

    public static final RegistrySupplier<Block> BLOODY_ROSE = BLOCKS.register("bloody_rose",
            () -> new BloodyRoseBlock(BlockBehaviour.Properties.copy(Blocks.DANDELION)));

    public static final RegistrySupplier<Block> DISTILLERY = BLOCKS.register("distillery",
            () -> new DistilleryBlock(BlockBehaviour.Properties.of(Material.METAL).strength(4.0f).requiresCorrectToolForDrops()
                    .noOcclusion().lightLevel(state -> state.getValue(DistilleryBlock.POWERED) ? 15 : 0)));

    public static final RegistrySupplier<Block> KETTLE = BLOCKS.register("kettle",
            () -> new KettleBlock(BlockBehaviour.Properties.of(Material.METAL).strength(4.0f).requiresCorrectToolForDrops()
                    .noOcclusion().lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 15 : 0)));

    public static final RegistrySupplier<Block> CAULDRON = BLOCKS.register("cauldron",
            () -> new CauldronBlock(BlockBehaviour.Properties.of(Material.METAL).strength(4.0f).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistrySupplier<Block> ARTHANA = BLOCKS_WITHOUT_ITEMS.register("arthana",
            () -> new ArthanaBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).noOcclusion().instabreak().noDrops()));

    ///////////
    //       //
    // CROPS //
    //       //
    ///////////

    public static final RegistrySupplier<Block> BELLADONNA_PLANT = BLOCKS_WITHOUT_ITEMS.register("belladonna_plant",
            () -> new BelladonnaCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion()));

    public static final RegistrySupplier<Block> MANDRAKE_PLANT = BLOCKS_WITHOUT_ITEMS.register("mandrake_plant",
            () -> new MandrakeCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion()));

    public static final RegistrySupplier<Block> WATER_ARTICHOKE_PLANT = BLOCKS_WITHOUT_ITEMS.register("water_artichoke_plant",
            () -> new WaterArtichokeCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion()));

    public static final RegistrySupplier<Block> SNOWBELL_PLANT = BLOCKS_WITHOUT_ITEMS.register("snowbell_plant",
            () -> new SnowbellCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion()));

    public static final RegistrySupplier<Block> WOLFSBANE_PLANT = BLOCKS_WITHOUT_ITEMS.register("wolfsbane_plant",
            () -> new WolfsbaneCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion()));

    public static final RegistrySupplier<Block> GARLIC_PLANT = BLOCKS_WITHOUT_ITEMS.register("garlic_plant",
            () -> new GarlicCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion()));

    public static final RegistrySupplier<Block> WORMWOOD_PLANT = BLOCKS_WITHOUT_ITEMS.register("wormwood_plant",
            () -> new WormwoodCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion()));

    /////////////////
    //             //
    // CANDELABRAS //
    //             //
    /////////////////

    public static final RegistrySupplier<Block> CANDELABRA = BLOCKS.register("candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.SAND).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> WHITE_CANDELABRA = BLOCKS.register("white_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.WOOL).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> ORANGE_CANDELABRA = BLOCKS.register("orange_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> MAGENTA_CANDELABRA = BLOCKS.register("magenta_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> LIGHT_BLUE_CANDELABRA = BLOCKS.register("light_blue_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> YELLOW_CANDELABRA = BLOCKS.register("yellow_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_YELLOW).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> LIME_CANDELABRA = BLOCKS.register("lime_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GREEN).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> PINK_CANDELABRA = BLOCKS.register("pink_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> GRAY_CANDELABRA = BLOCKS.register("gray_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> LIGHT_GRAY_CANDELABRA = BLOCKS.register("light_gray_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> CYAN_CANDELABRA = BLOCKS.register("cyan_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_CYAN).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> PURPLE_CANDELABRA = BLOCKS.register("purple_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> BLUE_CANDELABRA = BLOCKS.register("blue_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> BROWN_CANDELABRA = BLOCKS.register("brown_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> GREEN_CANDELABRA = BLOCKS.register("green_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> RED_CANDELABRA = BLOCKS.register("red_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));
    public static final RegistrySupplier<Block> BLACK_CANDELABRA = BLOCKS.register("black_candelabra",
            () -> new CandelabraBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(AbstractCandleBlock.LIT) ? 15 : 0)));

    //////////////////////////
    //                      //
    //  ROWAN WOOD STUFFS   //
    //                      //
    //////////////////////////

    public static RegistrySupplier<Block> ROWAN_LOG = BLOCKS.register("rowan_log",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static RegistrySupplier<Block> ROWAN_WOOD = BLOCKS.register("rowan_wood",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistrySupplier<Block> STRIPPED_ROWAN_LOG = BLOCKS.register("stripped_rowan_log",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistrySupplier<Block> STRIPPED_ROWAN_WOOD = BLOCKS.register("stripped_rowan_wood",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistrySupplier<Block> ROWAN_PLANKS = BLOCKS.register("rowan_planks",
            () -> new FlammableBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> ROWAN_STAIRS = BLOCKS.register("rowan_stairs",
            () -> new FlammableStairBlock(ROWAN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistrySupplier<Block> ROWAN_LEAVES = BLOCKS.register("rowan_leaves",
            () -> new FlammableLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).noOcclusion()));
    public static final RegistrySupplier<Block> ROWAN_SAPLING = BLOCKS.register("rowan_sapling",
            () -> new SaplingBlock(new RowanTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noOcclusion()));
    public static final RegistrySupplier<Block> ROWAN_PRESSURE_PLATE = BLOCKS.register("rowan_pressure_plate",
            () -> new LockedPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));
    public static final RegistrySupplier<Block> ROWAN_BUTTON = BLOCKS.register("rowan_button",
            () -> new LockedStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).noCollission()));
    public static final RegistrySupplier<Block> ROWAN_FENCE = BLOCKS.register("rowan_fence",
            () -> new FlammableFenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistrySupplier<Block> ROWAN_FENCE_GATE = BLOCKS.register("rowan_fence_gate",
            () -> new LockedFenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE)));
    public static final RegistrySupplier<Block> ROWAN_SLAB = TATBlocks.BLOCKS.register("rowan_slab",
            () -> new FlammableSlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));

    /////////////////////////////
    //                         //
    //  HAWTHORN WOOD STUFFS   //
    //                         //
    /////////////////////////////

    public static final RegistrySupplier<Block> HAWTHORN_PRESSURE_PLATE = BLOCKS.register("hawthorn_pressure_plate",
            () -> new LockedPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));
    public static final RegistrySupplier<Block> HAWTHORN_BUTTON = BLOCKS.register("hawthorn_button",
            () -> new LockedStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).noCollission()));
    public static final RegistrySupplier<Block> HAWTHORN_FENCE = BLOCKS.register("hawthorn_fence",
            () -> new FlammableFenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE), 20, 5));
    public static final RegistrySupplier<Block> HAWTHORN_FENCE_GATE = BLOCKS.register("hawthorn_fence_gate",
            () -> new LockedFenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE)));
    public static final RegistrySupplier<Block> HAWTHORN_SLAB = TATBlocks.BLOCKS.register("hawthorn_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static RegistrySupplier<Block> HAWTHORN_LOG = BLOCKS.register("hawthorn_log",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static RegistrySupplier<Block> HAWTHORN_WOOD = BLOCKS.register("hawthorn_wood",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistrySupplier<Block> STRIPPED_HAWTHORN_LOG = BLOCKS.register("stripped_hawthorn_log",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistrySupplier<Block> STRIPPED_HAWTHORN_WOOD = BLOCKS.register("stripped_hawthorn_wood",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistrySupplier<Block> HAWTHORN_PLANKS = BLOCKS.register("hawthorn_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> HAWTHORN_STAIRS = BLOCKS.register("hawthorn_stairs",
            () -> new StairBlock(HAWTHORN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistrySupplier<Block> HAWTHORN_LEAVES = BLOCKS.register("hawthorn_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).noOcclusion()));
    public static final RegistrySupplier<Block> HAWTHORN_SAPLING = BLOCKS.register("hawthorn_sapling",
            () -> new SaplingBlock(new HawthornTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).noOcclusion()));

    //////////////////////////
    //                      //
    //  ALDER WOOD STUFFS   //
    //                      //
    //////////////////////////

    public static final RegistrySupplier<Block> ALDER_PRESSURE_PLATE = BLOCKS.register("alder_pressure_plate",
            () -> new LockedPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));
    public static final RegistrySupplier<Block> ALDER_BUTTON = BLOCKS.register("alder_button",
            () -> new LockedStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON).noCollission()));
    public static final RegistrySupplier<Block> ALDER_FENCE = BLOCKS.register("alder_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistrySupplier<Block> ALDER_FENCE_GATE = BLOCKS.register("alder_fence_gate",
            () -> new LockedFenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE)));
    public static final RegistrySupplier<Block> ALDER_SLAB = TATBlocks.BLOCKS.register("alder_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static RegistrySupplier<Block> ALDER_LOG = BLOCKS.register("alder_log",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static RegistrySupplier<Block> ALDER_WOOD = BLOCKS.register("alder_wood",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistrySupplier<Block> STRIPPED_ALDER_LOG = BLOCKS.register("stripped_alder_log",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistrySupplier<Block> STRIPPED_ALDER_WOOD = BLOCKS.register("stripped_alder_wood",
            () -> new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistrySupplier<Block> ALDER_PLANKS = BLOCKS.register("alder_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> ALDER_STAIRS = BLOCKS.register("alder_stairs",
            () -> new StairBlock(ALDER_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistrySupplier<Block> ALDER_LEAVES = BLOCKS.register("alder_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistrySupplier<Block> ALDER_SAPLING = BLOCKS.register("alder_sapling",
            () -> new SaplingBlock(new AlderTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    ////////////
    //        //
    //  MISC  //
    //        //
    ////////////

    public static final RegistrySupplier<Block> DEMON_HEART = BLOCKS.register("demon_heart",
            () -> new DemonHeartBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_FUNGUS)));

    public static final RegistrySupplier<Block> RITUAL_CHALK = BLOCKS_WITHOUT_ITEMS.register("ritual_chalk",
            () -> new ChalkBlock(RitualType.NORMAL, BlockBehaviour.Properties.copy(Blocks.CRIMSON_FUNGUS)));

    public static final RegistrySupplier<Block> GOLDEN_CHALK = BLOCKS_WITHOUT_ITEMS.register("golden_chalk",
            () -> new GoldenChalkBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_FUNGUS)));

    public static final RegistrySupplier<Block> OTHERWHERE_CHALK = BLOCKS_WITHOUT_ITEMS.register("otherwhere_chalk",
            () -> new ChalkBlock(RitualType.OTHERWHERE, BlockBehaviour.Properties.copy(Blocks.CRIMSON_FUNGUS)));

    public static final RegistrySupplier<Block> INFERNAL_CHALK = BLOCKS_WITHOUT_ITEMS.register("infernal_chalk",
            () -> new ChalkBlock(RitualType.INFERNAL, BlockBehaviour.Properties.copy(Blocks.CRIMSON_FUNGUS)));

    public static void register() {
        BLOCKS.register();
        BLOCKS_WITHOUT_ITEMS.register();

        for (RegistrySupplier<Block> block : BLOCKS) {
            ITEMS.register(block.getId(),
                    () -> new BlockItem(block.get(), new Item.Properties().tab(TATItems.TAB)));
        }

        ITEMS.register();
    }
}
