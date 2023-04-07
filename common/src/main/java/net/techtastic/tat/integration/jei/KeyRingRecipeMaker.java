package net.techtastic.tat.integration.jei;

import mezz.jei.api.constants.ModIds;
import mezz.jei.api.helpers.IStackHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.api.KeyHelper;

import java.util.List;
import java.util.UUID;

public final class KeyRingRecipeMaker {
    public static List<CraftingRecipe> createRecipes(IStackHelper stackHelper) {
        String group = "tat.key_ring";

        UUID kId1 = UUID.randomUUID();
        ItemStack key1 = new ItemStack(TATItems.KEY.get());
        KeyHelper.setKeyId(kId1, key1);
        KeyHelper.setKeyPos(new BlockPos(0, 0, 0), key1);

        UUID kId2 = UUID.randomUUID();
        ItemStack key2 = new ItemStack(TATItems.KEY.get());
        KeyHelper.setKeyId(kId2, key2);
        KeyHelper.setKeyPos(new BlockPos(1, 1, 1), key2);

        ItemStack twoKeyRing = new ItemStack(TATItems.KEY_RING.get());
        KeyHelper.addToKeyList(kId1, twoKeyRing, new BlockPos(0, 0, 0));
        KeyHelper.addToKeyList(kId2, twoKeyRing, new BlockPos(1, 1, 1));

        ItemStack oneKeyRing = new ItemStack(TATItems.KEY_RING.get());
        KeyHelper.addToKeyList(kId1, oneKeyRing, new BlockPos(0, 0, 0));

        return List.of(
                new ShapedRecipe(
                        new ResourceLocation(ModIds.MINECRAFT_ID, "tat.key_ring.only_keys"),
                        group,
                        2,
                        2,
                        NonNullList.of(Ingredient.EMPTY,
                                Ingredient.of(key1),
                                Ingredient.of(key2)
                        ),
                        twoKeyRing
                ),
                new ShapedRecipe(
                        new ResourceLocation(ModIds.MINECRAFT_ID, "tat.key_ring.with_ring"),
                        group,
                        2,
                        2,
                        NonNullList.of(Ingredient.EMPTY,
                                Ingredient.of(oneKeyRing),
                                Ingredient.of(key2)
                        ),
                        twoKeyRing
                )
        );
    }

    private KeyRingRecipeMaker() {
    }
}
