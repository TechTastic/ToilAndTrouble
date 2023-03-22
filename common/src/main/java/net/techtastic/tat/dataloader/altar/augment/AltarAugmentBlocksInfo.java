package net.techtastic.tat.dataloader.altar.augment;

import net.minecraft.resources.ResourceLocation;

public record AltarAugmentBlocksInfo(
        ResourceLocation id, int priority, AugmentType type, int typePriority, java.util.Set<AugmentEffect> effects
) {
}
