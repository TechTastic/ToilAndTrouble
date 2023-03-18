package net.techtastic.tat.dataloader.altar.augment;

import net.minecraft.resources.ResourceLocation;

record AltarAugmentBlocksInfo(
        ResourceLocation id, int priority, AugmentType type, int typePriority
) {
}
