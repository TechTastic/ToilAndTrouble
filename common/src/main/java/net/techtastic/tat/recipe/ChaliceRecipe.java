package net.techtastic.tat.recipe;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.techtastic.tat.TATRecipes;
import net.techtastic.tat.api.KeyHelper;
import net.techtastic.tat.item.TATItems;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChaliceRecipe extends CustomRecipe {

    public ChaliceRecipe(ResourceLocation id) {
        super(id);
    }

    private List<ItemStack> getItems(CraftingContainer inventory) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack itemStack = inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                itemStacks.add(itemStack);
            }
        }
        return itemStacks;
    }

    @Override
    public boolean matches(CraftingContainer inventory, Level world) {
        List<ItemStack> itemStacks = getItems(inventory);
        if (itemStacks.size() != 2) return false;

        ItemStack itemStack1 = itemStacks.get(0);
        ItemStack itemStack2 = itemStacks.get(1);
        if (itemStack2.is(TATItems.CHALICE.get())) {
            ItemStack tmp = itemStack1;
            itemStack1 = itemStack2;
            itemStack2 = tmp;
        }

        return itemStack1.is(TATItems.CHALICE.get()) && itemStack2.is(TATItems.REDSTONE_SOUP.get());
    }

    @Override
    public ItemStack assemble(@NotNull CraftingContainer inventory) {
        List<ItemStack> itemStacks = getItems(inventory);

        ItemStack itemStack1 = itemStacks.get(0);
        ItemStack itemStack2 = itemStacks.get(1);

        if (!(itemStack1.is(TATItems.CHALICE.get()) &&
                itemStack2.is(TATItems.REDSTONE_SOUP.get())))
            return null;

        CompoundTag tag = itemStack1.getOrCreateTag();

        if (!tag.contains("ToilAndTrouble$soup") ||
                tag.getBoolean("ToilAndTrouble$soup"))
            return null;

        ItemStack filled = itemStack1.copy();
        filled.setCount(1);
        filled.getOrCreateTag().putBoolean("ToilAndTrouble$soup", true);
        return filled;
    }



    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j > 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TATRecipes.CHALICE_SERIALIZER.get();
    }
}
