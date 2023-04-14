package net.techtastic.tat.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.techtastic.tat.block.TATBlocks;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.ToilAndTrouble;
import net.techtastic.tat.recipe.CastIronOvenRecipe;

import javax.annotation.Nonnull;

public class CastIronOvenRecipeCategory implements IRecipeCategory<CastIronOvenRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(ToilAndTrouble.MOD_ID, "oven_fumigation");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(ToilAndTrouble.MOD_ID, "textures/gui/cast_iron_oven_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public CastIronOvenRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 3, 169, 82);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(TATBlocks.CAST_IRON_OVEN.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CastIronOvenRecipe> getRecipeClass() {
        return CastIronOvenRecipe.class;
    }

    @Override
    public RecipeType<CastIronOvenRecipe> getRecipeType() {
        return new RecipeType<>(CastIronOvenRecipeCategory.UID, CastIronOvenRecipe.class);
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("block.tat.cast_iron_oven.jei");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull CastIronOvenRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 19).addIngredients(recipe.getIngredients().get(0));
        if (ToilAndTrouble.FUELS != null) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 44, 54).addIngredients(Ingredient.of(ToilAndTrouble.FUELS.toArray(new ItemLike[0])));
        } else {
            builder.addSlot(RecipeIngredientRole.CATALYST, 44, 54).addIngredients(Ingredient.of(ItemTags.COALS));
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 54).addIngredients(Ingredient.of(TATItems.CLAY_JAR.get()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 19).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 54).addItemStack(recipe.getSecondOutput());
    }
}