package net.techtastic.tat;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TATTags {
    public static class Blocks {
        public static final TagKey<Block> MOD_LOGS = createTag("mod_logs");

        public static final TagKey<Block> MUTANDIS = createTag("mutandis");

        public static final TagKey<Block> FIRE_SOURCE = createTag("fire_source");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(ToilAndTrouble.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> FOUL_FUME_WOOD_ASH = createTag("foul_fume_wood_ash");
        public static final TagKey<Item> CAST_IRON_OVEN_INPUTS = createTag("cast_iron_oven_inputs");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(ToilAndTrouble.MOD_ID, name));
        }
    }
}
