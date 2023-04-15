package net.techtastic.tat.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.techtastic.tat.ToilAndTrouble;
import net.techtastic.tat.block.TATBlocks;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.recipe.DistilleryRecipe;

import javax.annotation.Nonnull;

public class DistilleryRecipeCategory implements IRecipeCategory<DistilleryRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(ToilAndTrouble.MOD_ID, "distilling");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(ToilAndTrouble.MOD_ID, "textures/gui/distillery_gui.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic powerProgress;
    private final IDrawableAnimated powerProgressAnim;
    private final IDrawableStatic craftProgress;
    private final IDrawableAnimated craftProgressAnim;

    public DistilleryRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 3, 169, 82);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(TATBlocks.DISTILLERY.get()));

        this.powerProgress = helper.createDrawable(TEXTURE, 176, 0, 11, 29);
        this.powerProgressAnim = helper.createAnimatedDrawable(powerProgress, 5, IDrawableAnimated.StartDirection.BOTTOM, false);
        this.craftProgress = helper.createDrawable(TEXTURE, 194, 0, 37, 8);
        this.craftProgressAnim = helper.createAnimatedDrawable(craftProgress, 39, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends DistilleryRecipe> getRecipeClass() {
        return DistilleryRecipe.class;
    }

    @Override
    public RecipeType<DistilleryRecipe> getRecipeType() {
        return new RecipeType<>(DistilleryRecipeCategory.UID, DistilleryRecipe.class);
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("block.tat.distillery.jei");
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull DistilleryRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 15).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 33).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 53).addIngredients(Ingredient.of(new ItemStack(TATItems.CLAY_JAR.get(), recipe.getJarCount())));

        NonNullList<ItemStack> outputs = recipe.getOutputs();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 105, 15).addItemStack(outputs.get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 123, 15).addItemStack(outputs.get(1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 105, 33).addItemStack(outputs.get(2));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 123, 33).addItemStack(outputs.get(3));
    }

    @Override
    public void draw(DistilleryRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        powerProgressAnim.draw(stack, 30, 20);
        craftProgressAnim.draw(stack, 64, 18);
        craftProgressAnim.draw(stack, 64, 28);
        craftProgressAnim.draw(stack, 64, 38);

        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
}