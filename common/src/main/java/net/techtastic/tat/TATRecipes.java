package net.techtastic.tat;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mezz.jei.api.constants.RecipeTypes;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.item.crafting.*;
import net.techtastic.tat.recipe.*;

public class TATRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.RECIPE_SERIALIZER_REGISTRY);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ToilAndTrouble.MOD_ID, Registry.RECIPE_TYPE_REGISTRY);

    public static final RegistrySupplier<RecipeSerializer<CastIronOvenRecipe>> CAST_IRON_OVEN_SERIALIZER =
            SERIALIZERS.register("oven_fumigation", () -> CastIronOvenRecipe.Serializer.INSTANCE);
    public static final RegistrySupplier<RecipeType<CastIronOvenRecipe>> CAST_IRON_OVEN_TYPE =
            TYPES.register("oven_fumigation", () -> CastIronOvenRecipe.Type.INSTANCE);

    public static final RegistrySupplier<RecipeSerializer<DistilleryRecipe>> DISTILLERY_SERIALIZER =
            SERIALIZERS.register("distilling", () -> DistilleryRecipe.Serializer.INSTANCE);
    public static final RegistrySupplier<RecipeType<DistilleryRecipe>> DISTILLERY_TYPE =
            TYPES.register("distilling", () -> DistilleryRecipe.Type.INSTANCE);

    public static final RegistrySupplier<RecipeSerializer<KettleRecipe>> KETTLE_SERIALIZER =
            SERIALIZERS.register("kettle_brewing", () -> KettleRecipe.Serializer.INSTANCE);
    public static final RegistrySupplier<RecipeType<KettleRecipe>> KETTLE_TYPE =
            TYPES.register("kettle_brewing", () -> KettleRecipe.Type.INSTANCE);

    public static final RegistrySupplier<SimpleRecipeSerializer<KeyRingRecipe>> KEY_RING_SERIALIZER =
            SERIALIZERS.register("key_ring", () -> new SimpleRecipeSerializer<>(KeyRingRecipe::new));

    public static final RegistrySupplier<SimpleRecipeSerializer<ChaliceRecipe>> CHALICE_SERIALIZER =
            SERIALIZERS.register("chalice", () -> new SimpleRecipeSerializer<>(ChaliceRecipe::new));

    public static void register() {
        SERIALIZERS.register();
        TYPES.register();
    }
}
