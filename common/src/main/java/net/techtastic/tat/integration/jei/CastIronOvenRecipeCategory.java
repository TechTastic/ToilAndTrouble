package net.techtastic.tat.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.util.SuppressForbidden;
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
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

public class CastIronOvenRecipeCategory implements IRecipeCategory<CastIronOvenRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(ToilAndTrouble.MOD_ID, "oven_fumigation");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(ToilAndTrouble.MOD_ID, "textures/gui/cast_iron_oven_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableStatic flame;
    private final IDrawableAnimated flameAnim;
    private final IDrawableStatic progress;
    private final IDrawableAnimated progressAnim;

    public CastIronOvenRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4, 3, 169, 82);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(TATBlocks.CAST_IRON_OVEN.get()));

        this.flame = helper.createDrawable(TEXTURE, 176, 0, 14, 14);
        this.flameAnim = helper.createAnimatedDrawable(flame, 300, IDrawableAnimated.StartDirection.TOP, false);
        this.progress = helper.createDrawable(TEXTURE, 176, 14, 48, 10);
        this.progressAnim = helper.createAnimatedDrawable(progress, 200, IDrawableAnimated.StartDirection.LEFT, false);
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
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 16).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.CATALYST, 40, 51).addIngredients(
                ToilAndTrouble.FUELS != null ? Ingredient.of(ToilAndTrouble.FUELS.toArray(new ItemLike[0])) : Ingredient.of(ItemTags.COALS)
        );
        builder.addSlot(RecipeIngredientRole.INPUT, 76, 51).addIngredients(Ingredient.of(TATItems.CLAY_JAR.get()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 16).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 51).addItemStack(recipe.getSecondOutput());
    }

    @Override
    public void draw(CastIronOvenRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        flameAnim.draw(stack, 41, 34);
        progressAnim.draw(stack, 58, 19);

        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
}