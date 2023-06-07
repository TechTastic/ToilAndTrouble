package net.techtastic.tat.screen.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.techtastic.tat.TATTags;

public class TATOvenInput extends Slot {
    public TATOvenInput(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.is(TATTags.Items.CAST_IRON_OVEN_INPUTS);
    }
}
