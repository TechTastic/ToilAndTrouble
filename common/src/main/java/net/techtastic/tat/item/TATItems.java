package net.techtastic.tat.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.techtastic.tat.ToilAndTrouble;
import net.techtastic.tat.api.RitualType;
import net.techtastic.tat.block.TATBlocks;
import net.techtastic.tat.item.custom.*;

public class TATItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.ITEM_REGISTRY);

    public static final CreativeModeTab TAB = CreativeTabRegistry.create(new ResourceLocation(ToilAndTrouble.MOD_ID, "tat_tab"), () ->
            new ItemStack(TATItems.CLAY_JAR.get()));

    public static final RegistrySupplier<Item> DEBUG_ITEM = ITEMS.register("debug_item",
            () -> new DebugItem(new Item.Properties().tab(TAB)));

    /* CHALKS */

    public static final RegistrySupplier<Item> RITUAL_CHALK = ITEMS.register("ritual_chalk",
            () -> new ChalkBlockItem(RitualType.NORMAL, TATBlocks.RITUAL_CHALK.get(), new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> GOLDEN_CHALK = ITEMS.register("golden_chalk",
            () -> new ChalkBlockItem(RitualType.GOLDEN, TATBlocks.GOLDEN_CHALK.get(), new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> OTHERWHERE_CHALK = ITEMS.register("otherwhere_chalk",
            () -> new ChalkBlockItem(RitualType.OTHERWHERE, TATBlocks.OTHERWHERE_CHALK.get(), new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> INFERAL_CHALK = ITEMS.register("infernal_chalk",
            () -> new ChalkBlockItem(RitualType.INFERNAL, TATBlocks.INFERNAL_CHALK.get(), new Item.Properties().tab(TAB)));

    /*public static final RegistrySupplier<Item> ROWAN_BERRIES = ITEMS.register("rowan_berries",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().build()).tab(TAB)));

    /* ALTAR AUGMENTS */

    /*public static final RegistrySupplier<Item> CHALICE = ITEMS.register("chalice",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ARTHANA = ITEMS.register("arthana",
        new ArthanaSwordItem(ToolMaterials.GOLD, 4, 4.0f, new FabricItemSettings().group(ModItemGroup.WITCHERYRESTITCHED).maxDamage(251)));*/

    public static final RegistrySupplier<Item> ARTHANA = ITEMS.register("arthana",
            () -> new ArthanaSwordItem(Tiers.GOLD, 4, 4.0f,new Item.Properties().tab(TAB).defaultDurability(251)));

    /* BOOKS */

    /*public static final RegistrySupplier<Item> BOOK_BREWS_AND_INFUSIONS = ITEMS.register("book_brews_and_infusions",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_CIRCLE_MAGIC = ITEMS.register("book_circle_magic",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_COLLECTING_FUMES = ITEMS.register("book_collecting_fumes",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_CONJURATION_AND_FETISHES = ITEMS.register("book_conjuration_and_fetishes",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_DISTILLING = ITEMS.register("book_distilling",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_HERBOLOGY = ITEMS.register("book_herbology",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_OBSERVATIONS_OF_AN_IMMORTAL = ITEMS.register("book_observations_of_an_immortal",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_OF_BIOMES = ITEMS.register("book_of_biomes",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_SYMBOLOGY = ITEMS.register("book_symbology",
            () -> new Item(new Item.Properties().tab(TAB)));*/

    /* JAR ITEMS */

    public static final RegistrySupplier<Item> UNFIRED_CLAY_JAR = ITEMS.register("unfired_clay_jar",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> CLAY_JAR = ITEMS.register("clay_jar",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> FOUL_FUME = ITEMS.register("foul_fume",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> EXHALE_OF_THE_HORNED_ONE = ITEMS.register("exhale_of_the_horned_one",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BREATH_OF_THE_GODDESS = ITEMS.register("breath_of_the_goddess",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> HINT_OF_REBIRTH = ITEMS.register("hint_of_rebirth",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WHIFF_OF_MAGIC = ITEMS.register("whiff_of_magic",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> REEK_OF_MISFORTUNE = ITEMS.register("reek_of_misfortune",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ODOR_OF_PURITY = ITEMS.register("odor_of_purity",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> OIL_OF_VITRIOL = ITEMS.register("oil_of_vitriol",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> TEAR_OF_THE_GODDESS = ITEMS.register("tear_of_the_goddess",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> DIAMOND_VAPOR = ITEMS.register("diamond_vapor",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ENDER_DEW = ITEMS.register("ender_dew",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> DEMONIC_BLOOD = ITEMS.register("demonic_blood",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> DROP_OF_LUCK = ITEMS.register("drop_of_luck",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> PURIFIED_MILK = ITEMS.register("purified_milk",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> FOCUSED_WILL = ITEMS.register("focused_will",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> CONDENSED_FEAR = ITEMS.register("condensed_fear",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> MELLIFLUOUS_HUNGER = ITEMS.register("mellifluous_hunger",
            () -> new Item(new Item.Properties().tab(TAB)));

    /* PLANTS */

    public static final RegistrySupplier<Item> BELLADONNA_FLOWER = ITEMS.register("belladonna_flower",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BELLADONNA_SEEDS = ITEMS.register("belladonna_seeds",
            () -> new ItemNameBlockItem(TATBlocks.BELLADONNA_PLANT.get(), new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> MANDRAKE_ROOT = ITEMS.register("mandrake_root",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> MANDRAKE_SEEDS = ITEMS.register("mandrake_seeds",
            () -> new ItemNameBlockItem(TATBlocks.MANDRAKE_PLANT.get(), new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WATER_ARTICHOKE_GLOBE = ITEMS.register("water_artichoke_globe",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WATER_ARTICHOKE_SEEDS = ITEMS.register("water_artichoke_seeds",
            () -> new ItemNameBlockItem(TATBlocks.WATER_ARTICHOKE_PLANT.get(), new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> GARLIC = ITEMS.register("garlic",
            () -> new ItemNameBlockItem(TATBlocks.GARLIC_PLANT.get(), new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WOLFSBANE_FLOWER = ITEMS.register("wolfsbane_flower",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WOLFSBANE_SEEDS = ITEMS.register("wolfsbane_seeds",
            () -> new ItemNameBlockItem(TATBlocks.WOLFSBANE_PLANT.get(), new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WORMWOOD = ITEMS.register("wormwood",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WORMWOOD_SEEDS = ITEMS.register("wormwood_seeds",
            () -> new ItemNameBlockItem(TATBlocks.WORMWOOD_PLANT.get(), new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ICY_NEEDLE = ITEMS.register("icy_needle",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> SNOWBELL_SEEDS = ITEMS.register("snowbell_seeds",
            () -> new ItemNameBlockItem(TATBlocks.SNOWBELL_PLANT.get(), new Item.Properties().tab(TAB)));

    /* KEYS */

    public static final RegistrySupplier<Item> KEY = ITEMS.register("key",
            () -> new KeyItem(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> KEY_RING = ITEMS.register("key_ring",
            () -> new KeyRingItem(new Item.Properties().tab(TAB)));

    /* MISC */

    public static final RegistrySupplier<Item> WOOD_ASH = ITEMS.register("wood_ash",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> QUICKLIME = ITEMS.register("quicklime",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> GYPSUM = ITEMS.register("gypsum",
            () -> new Item(new Item.Properties().tab(TAB)));

    /*public static final RegistrySupplier<Item> ICY_NEEDLE = ITEMS.register("icy_needle",
            () -> new Item(new Item.Properties().tab(TAB)));*/

    public static final RegistrySupplier<Item> MUTANDIS = ITEMS.register("mutandis",
            () -> new MutandisItem(new Item.Properties().tab(TAB)));

    /*public static final RegistrySupplier<Item> MUTANDIS_EXTREMIS = ITEMS.register("mutandis_extremis",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WAYSTONE = ITEMS.register("waystone",
            () -> new Item(new Item.Properties().tab(TAB)));*/

    public static final RegistrySupplier<Item> BONE_NEEDLE = ITEMS.register("bone_needle",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ATTUNED_STONE = ITEMS.register("attuned_stone",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ATTUNED_STONE_CHARGED = ITEMS.register("attuned_stone_charged",
            () -> new Item(new Item.Properties().tab(TAB)));

    /*public static final RegistrySupplier<Item> WITCHS_LADDER = ITEMS.register("witchs_ladder",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WITCHS_LADDER_SHRIEKING = ITEMS.register("witchs_ladder_shrieking",
            () -> new Item(new Item.Properties().tab(TAB)));*/

    public static final RegistrySupplier<Item> REFINED_EVIL = ITEMS.register("refined_evil",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> FUME_FILTER = ITEMS.register("fume_filter",
            () -> new Item(new Item.Properties().tab(TAB)));

    /* BOTTLES */

    public static final RegistrySupplier<Item> TAGLOCK = ITEMS.register("taglock",
            () -> new TaglockItem(new Item.Properties().tab(TAB)));

    /*public static final RegistrySupplier<Item> REDSTONE_SOUP = ITEMS.register("redstone_soup",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> FLYING_OINTMENT = ITEMS.register("flying_ointment",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> GHOST_OF_THE_LIGHT = ITEMS.register("ghost_of_the_light",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> SOUL_OF_THE_WORLD = ITEMS.register("soul_of_the_world",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> INFERNAL_ANIMUS = ITEMS.register("infernal_animus",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> SPIRIT_OF_OTHERWHERE = ITEMS.register("spirit_of_otherwhere",
            () -> new Item(new Item.Properties().tab(TAB)));*/

    /* ANIMAL PARTS */

    /*public static final RegistrySupplier<Item> WOOL_OF_BAT = ITEMS.register("wool_of_bat",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> CONDENSED_BAT_BALL = ITEMS.register("condensed_bat_ball",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> TONGUE_OF_DOG = ITEMS.register("tongue_of_dog",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> CREEPER_HEART = ITEMS.register("creeper_heart",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> TOE_OF_FROG = ITEMS.register("toe_of_frog",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> OWLET_WING = ITEMS.register("owlet_wing",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ENT_TWIG = ITEMS.register("ent_twig",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> HEART_OF_GOLD = ITEMS.register("heart_of_gold",
            () -> new Item(new Item.Properties().tab(TAB)));*/

    /* BREWS */

    public static final RegistrySupplier<Item> BREW_OF_FLOWING_SPIRIT = ITEMS.register("brew_of_flowing_spirit",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BREW_OF_HOLLOW_TEARS = ITEMS.register("brew_of_hollow_tears",
            () -> new Item(new Item.Properties().tab(TAB)));

    public static void register() {
        ITEMS.register();
    }
}
