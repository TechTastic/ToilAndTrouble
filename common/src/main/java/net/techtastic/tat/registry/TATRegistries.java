package net.techtastic.tat.registry;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.techtastic.tat.ToilAndTrouble;

public class TATRegistries {
    Registrar<AltarAugmentProvider> REGISTRAR = Registries.get(ToilAndTrouble.MOD_ID).<AltarAugmentProvider>builder(new ResourceLocation(ToilAndTrouble.MOD_ID, "altar_augment_provider")).build();
}
