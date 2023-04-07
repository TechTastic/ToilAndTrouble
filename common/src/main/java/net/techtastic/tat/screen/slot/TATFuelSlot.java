package net.techtastic.tat.screen.slot;

import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.techtastic.tat.TATTags;
import net.techtastic.tat.block.entity.CastIronOvenBlockEntity;
import org.jetbrains.annotations.NotNull;

public class TATFuelSlot extends Slot {
    private final CastIronOvenBlockEntity entity;

    public TATFuelSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);

        this.entity = (CastIronOvenBlockEntity) container;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack) {
        if (FuelRegistry.get(itemStack) == 0) return false;

        if (!itemStack.is(TATTags.Items.CAST_IRON_OVEN_INPUTS)) return true;

        return !(entity.getItem(0).isEmpty() ||
                entity.getItem(0).is(itemStack.getItem()) &&
                        entity.getItem(0).getCount() < entity.getMaxStackSize());
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack itemStack) {
        return isBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack);
    }

    public boolean isBucket(ItemStack stack) {
        return stack.is(Items.BUCKET);
    }
}
