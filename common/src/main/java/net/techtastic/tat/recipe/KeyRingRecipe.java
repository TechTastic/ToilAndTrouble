package net.techtastic.tat.recipe;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.TATRecipes;
import net.techtastic.tat.api.KeyHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KeyRingRecipe extends CustomRecipe {
    private ItemStack remainder = ItemStack.EMPTY;

    public KeyRingRecipe(ResourceLocation id) {
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
        if (itemStacks.size() == 1) {
            ItemStack keyRing = itemStacks.get(0);
            return keyRing.is(TATItems.KEY_RING.get());
        }

        if (itemStacks.size() != 2) return false;
        ItemStack itemStack1 = itemStacks.get(0);
        ItemStack itemStack2 = itemStacks.get(1);
        if (itemStack2.is(TATItems.KEY_RING.get())) {
            ItemStack tmp = itemStack1;
            itemStack1 = itemStack2;
            itemStack2 = tmp;
        }

        return (itemStack1.is(TATItems.KEY_RING.get()) && itemStack2.is(TATItems.KEY.get())) ||
                (itemStack1.is(TATItems.KEY.get()) && itemStack2.is(TATItems.KEY.get()));
    }

    @Override
    public ItemStack assemble(@NotNull CraftingContainer inventory) {
        List<ItemStack> itemStacks = getItems(inventory);

        /*if (itemStacks.size() == 1) {
            ItemStack ring = itemStacks.get(0);
            if (ring.is(TATItems.KEY_RING.get())) {
                CompoundTag ringTag = ring.getOrCreateTag();
                if (ringTag.contains("ToilAndTrouble$keyList")) {
                    ListTag listTag = (ListTag) ringTag.get("ToilAndTrouble$keyList");

                    CompoundTag keyTag = (CompoundTag) listTag.stream().findFirst().get();
                    listTag.remove(keyTag);
                    ringTag.put("ToilAndTrouble$keyList", listTag);

                    if (listTag.size() > 1) {
                        this.remainder = new ItemStack(TATItems.KEY_RING.get());
                        this.remainder.setTag(ringTag);
                    } else if (listTag.size() == 1) {
                        this.remainder = new ItemStack(TATItems.KEY.get());
                        this.remainder.setTag((CompoundTag) listTag.stream().findFirst().get());
                    }

                    if (!this.remainder.isEmpty()) {
                        int slot = 0;
                        while (slot < inventory.getContainerSize()) {
                            if (inventory.canPlaceItem(slot, this.remainder)) {
                                inventory.setItem(0, this.remainder);
                                inventory.setChanged();
                                break;
                            }
                            slot++;
                        }
                    }

                    ItemStack key = new ItemStack(TATItems.KEY.get());
                    key.setTag(keyTag);
                    return key;
                }
            }
        }*/

        if (itemStacks.size() != 2) return null;

        ItemStack itemStack1 = itemStacks.get(0);
        ItemStack itemStack2 = itemStacks.get(1);

        if (itemStack1.is(TATItems.KEY.get()) && itemStack2.is(TATItems.KEY.get())) {

            ItemStack keyRing = new ItemStack(TATItems.KEY_RING.get());

            CompoundTag pTag = itemStack1.getOrCreateTag();
            if (!pTag.contains("ToilAndTrouble$keyId")) return null;
            KeyHelper.addToKeyList(
                    pTag.getUUID("ToilAndTrouble$keyId"),
                    keyRing,
                    BlockPos.of(pTag.getLong("ToilAndTrouble$lockedBlockPos"))
            );

            CompoundTag sTag = itemStack2.getOrCreateTag();
            if (!sTag.contains("ToilAndTrouble$keyId")) return null;
            KeyHelper.addToKeyList(
                    sTag.getUUID("ToilAndTrouble$keyId"),
                    keyRing,
                    BlockPos.of(sTag.getLong("ToilAndTrouble$lockedBlockPos"))
            );

            return keyRing;
        } else if (itemStack2.is(TATItems.KEY_RING.get())) {
            ItemStack tmp = itemStack1;
            itemStack1 = itemStack2;
            itemStack2 = tmp;
        }

        ItemStack newRing = itemStack1.copy();
        CompoundTag newKeyTag = itemStack2.getOrCreateTag();
        if (!newKeyTag.contains("ToilAndTrouble$keyId")) return null;
        KeyHelper.addToKeyList(
                newKeyTag.getUUID("ToilAndTrouble$keyId"),
                newRing,
                BlockPos.of(newKeyTag.getLong("ToilAndTrouble$lockedBlockPos"))
        );

        return newRing;
    }



    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j > 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TATRecipes.KEY_RING_SERIALIZER.get();
    }
}
