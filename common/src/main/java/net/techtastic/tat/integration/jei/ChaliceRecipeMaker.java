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
import net.techtastic.tat.api.KeyHelper;
import net.techtastic.tat.item.TATItems;

import java.util.List;
import java.util.UUID;

public final class ChaliceRecipeMaker {
    public static List<CraftingRecipe> createRecipes(IStackHelper stackHelper) {
        String group = "tat.chalice";

        ItemStack filled = new ItemStack(TATItems.CHALICE.get(), 1);
        filled.getOrCreateTag().putBoolean("ToilAndTrouble$soup", true);

        return List.of(
                new ShapedRecipe(
                        new ResourceLocation(ModIds.MINECRAFT_ID, "tat.key_ring.only_keys"),
                        group,
                        2,
                        2,
                        NonNullList.of(Ingredient.EMPTY,
                                Ingredient.of(TATItems.CHALICE.get()),
                                Ingredient.of(TATItems.REDSTONE_SOUP.get())
                        ),
                        filled
                )
        );
    }

    private ChaliceRecipeMaker() {
    }
}
