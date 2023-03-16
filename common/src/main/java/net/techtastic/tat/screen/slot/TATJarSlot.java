package net.techtastic.tat.screen.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.techtastic.tat.TATItems;

public class TATJarSlot extends Slot {
    public TATJarSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.is(TATItems.CLAY_JAR.get());
    }
}
