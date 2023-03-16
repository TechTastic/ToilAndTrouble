package net.techtastic.tat.integration;

import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class TATFuels {
    public static List<ItemLike> getAllFuels() {
        int count = 0;
        for (Item item : Registry.ITEM) {
            if (FuelRegistry.get(new ItemStack(item)) != 0) {
                count++;
            }
        }

        List<ItemLike> list = new ArrayList<>(count);

        for (Item item : Registry.ITEM) {
            if (FuelRegistry.get(new ItemStack(item)) != 0) {
                list.add(item);
            }
        }

        return list;
    }
}
