package net.techtastic.tat;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.techtastic.tat.item.custom.*;

public class TATItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.ITEM_REGISTRY);

    public static final CreativeModeTab TAB = CreativeTabRegistry.create(new ResourceLocation(ToilAndTrouble.MOD_ID, "tat_tab"), () ->
            new ItemStack(TATItems.CLAY_JAR.get()));

    public static final RegistrySupplier<Item> DEBUG_ITEM = registerItem("debug_item",
            new DebugItem(new Item.Properties().tab(TAB)));

    /*public static final RegistrySupplier<Item> ROWAN_BERRIES = registerItem("rowan_berries",
            new Item(new Item.Properties().food(new FoodProperties.Builder().build()).tab(TAB)));

    /* ALTAR AUGMENTS */

    /*public static final RegistrySupplier<Item> CHALICE = registerItem("chalice",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ARTHANA = registerItem("arthana",
        new ArthanaSwordItem(ToolMaterials.GOLD, 4, 4.0f, new FabricItemSettings().group(ModItemGroup.WITCHERYRESTITCHED).maxDamage(251)));*/

    /*public static final RegistrySupplier<Item> ARTHANA = registerItem("arthana",
            new Item(new Item.Properties().tab(TAB)));*/

    /* BOOKS */

    /*public static final RegistrySupplier<Item> BOOK_BREWS_AND_INFUSIONS = registerItem("book_brews_and_infusions",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_CIRCLE_MAGIC = registerItem("book_circle_magic",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_COLLECTING_FUMES = registerItem("book_collecting_fumes",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_CONJURATION_AND_FETISHES = registerItem("book_conjuration_and_fetishes",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_DISTILLING = registerItem("book_distilling",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_HERBOLOGY = registerItem("book_herbology",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_OBSERVATIONS_OF_AN_IMMORTAL = registerItem("book_observations_of_an_immortal",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_OF_BIOMES = registerItem("book_of_biomes",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BOOK_SYMBOLOGY = registerItem("book_symbology",
            new Item(new Item.Properties().tab(TAB)));*/

    /* JAR ITEMS */

    public static final RegistrySupplier<Item> UNFIRED_CLAY_JAR = registerItem("unfired_clay_jar",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> CLAY_JAR = registerItem("clay_jar",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> FOUL_FUME = registerItem("foul_fume",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> EXHALE_OF_THE_HORNED_ONE = registerItem("exhale_of_the_horned_one",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BREATH_OF_THE_GODDESS = registerItem("breath_of_the_goddess",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> HINT_OF_REBIRTH = registerItem("hint_of_rebirth",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WHIFF_OF_MAGIC = registerItem("whiff_of_magic",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> REEK_OF_MISFORTUNE = registerItem("reek_of_misfortune",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ODOR_OF_PURITY = registerItem("odor_of_purity",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> OIL_OF_VITRIOL = registerItem("oil_of_vitriol",
            new Item(new Item.Properties().tab(TAB)));

    /*public static final RegistrySupplier<Item> TEAR_OF_THE_GODDESS = registerItem("tear_of_the_goddess",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> DIAMOND_VAPOR = registerItem("diamond_vapor",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ENDER_DEW = registerItem("ender_dew",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> DEMONIC_BLOOD = registerItem("demonic_blood",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> DROP_OF_LUCK = registerItem("drop_of_luck",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> PURIFIED_MILK = registerItem("purified_milk",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> FOCUSED_WILL = registerItem("focused_will",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> CONDENSED_FEAR = registerItem("condensed_fear",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> MELLIFLUOUS_HUNGER = registerItem("mellifluous_hunger",
            new Item(new Item.Properties().tab(TAB)));*/

    /* PLANTS */

    /*public static final RegistrySupplier<Item> BELLADONNA_FLOWER = registerItem("belladonna_flower",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BELLADONNA_SEEDS = registerItem("belladonna_seeds",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> MANDRAKE_ROOT = registerItem("mandrake_root",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> MANDRAKE_SEEDS = registerItem("mandrake_seeds",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WATER_ARTICHOKE_GLOBE = registerItem("water_artichoke_globe",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> GARLIC = registerItem("garlic",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WOLFSBANE_FLOWER = registerItem("wolfsbane_flower",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WORMWOOD = registerItem("wormwood",
            new Item(new Item.Properties().tab(TAB)));*/

    /* KEYS */

    public static final RegistrySupplier<Item> KEY = registerItem("key",
            new KeyItem(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> KEY_RING = registerItem("key_ring",
            new KeyRingItem(new Item.Properties().tab(TAB)));

    /* MISC */

    public static final RegistrySupplier<Item> WOOD_ASH = registerItem("wood_ash",
            new Item(new Item.Properties().tab(TAB)));

    /*public static final RegistrySupplier<Item> QUICKLIME = registerItem("quicklime",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> GYPSUM = registerItem("gypsum",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ICY_NEEDLE = registerItem("icy_needle",
            new Item(new Item.Properties().tab(TAB)));*/

    public static final RegistrySupplier<Item> MUTANDIS = registerItem("mutandis",
            new MutandisItem(new Item.Properties().tab(TAB)));

    /*public static final RegistrySupplier<Item> MUTANDIS_EXTREMIS = registerItem("mutandis_extremis",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WAYSTONE = registerItem("waystone",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> BONE_NEEDLE = registerItem("bone_needle",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ATTUNED_STONE = registerItem("attuned_stone",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ATTUNED_STONE_CHARGED = registerItem("attuned_stone_charged",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WITCHS_LADDER = registerItem("witchs_ladder",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> WITCHS_LADDER_SHRIEKING = registerItem("witchs_ladder_shrieking",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> REFINED_EVIL = registerItem("refined_evil",
            new Item(new Item.Properties().tab(TAB)));*/

    /* BOTTLES */

    public static final RegistrySupplier<Item> TAGLOCK = registerItem("taglock",
            new TaglockItem(new Item.Properties().tab(TAB)));

    /*public static final RegistrySupplier<Item> REDSTONE_SOUP = registerItem("redstone_soup",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> FLYING_OINTMENT = registerItem("flying_ointment",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> GHOST_OF_THE_LIGHT = registerItem("ghost_of_the_light",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> SOUL_OF_THE_WORLD = registerItem("soul_of_the_world",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> INFERNAL_ANIMUS = registerItem("infernal_animus",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> SPIRIT_OF_OTHERWHERE = registerItem("spirit_of_otherwhere",
            new Item(new Item.Properties().tab(TAB)));*/

    /* ANIMAL PARTS */

    /*public static final RegistrySupplier<Item> WOOL_OF_BAT = registerItem("wool_of_bat",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> CONDENSED_BAT_BALL = registerItem("condensed_bat_ball",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> TONGUE_OF_DOG = registerItem("tongue_of_dog",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> CREEPER_HEART = registerItem("creeper_heart",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> TOE_OF_FROG = registerItem("toe_of_frog",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> OWLET_WING = registerItem("owlet_wing",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> ENT_TWIG = registerItem("ent_twig",
            new Item(new Item.Properties().tab(TAB)));

    public static final RegistrySupplier<Item> HEART_OF_GOLD = registerItem("heart_of_gold",
            new Item(new Item.Properties().tab(TAB)));*/

    public static RegistrySupplier<Item> registerItem(String name, Item item) {
        return ITEMS.register(name, () -> item);
    }

    public static void register() {
        ITEMS.register();
    }
}
