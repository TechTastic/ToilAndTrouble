package net.techtastic.tat.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.techtastic.tat.TATBlocks;
import net.techtastic.tat.ToilAndTrouble;
import net.techtastic.tat.recipe.CastIronOvenRecipe;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIToilAndTroublePlugin implements IModPlugin {
    private RecipeType<CastIronOvenRecipe> OVEN_FUMIGATION = new RecipeType<>(CastIronOvenRecipeCategory.UID, CastIronOvenRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ToilAndTrouble.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CastIronOvenRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<CastIronOvenRecipe> cioRecipes = rm.getAllRecipesFor(CastIronOvenRecipe.Type.INSTANCE);
        registration.addRecipes(OVEN_FUMIGATION, cioRecipes);

        registration.addRecipes(RecipeTypes.CRAFTING, KeyRingRecipeMaker.createRecipes(registration.getJeiHelpers().getStackHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TATBlocks.CAST_IRON_OVEN.get().asItem()), OVEN_FUMIGATION);
    }
}
