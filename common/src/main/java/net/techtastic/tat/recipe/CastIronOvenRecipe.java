package net.techtastic.tat.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CastIronOvenRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final ItemStack secondOutput;
    private final Ingredient inputItem;

    public CastIronOvenRecipe(ResourceLocation id, ItemStack output, ItemStack secondOutput, Ingredient inputItem) {
        this.id = id;
        this.output = output;
        this.secondOutput = secondOutput;
        this.inputItem = inputItem;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return inputItem.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer container) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    public ItemStack getSecondOutput() {
        return secondOutput.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.createWithCapacity(1);
        list.add(inputItem);
        return list;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CastIronOvenRecipe> {
        private Type() {}
        public static RecipeType<CastIronOvenRecipe> INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<CastIronOvenRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public CastIronOvenRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            ItemStack jar = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "jar"));
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));

            return new CastIronOvenRecipe(id, output, jar, input);
        }

        @Override
        public CastIronOvenRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack jar = buf.readItem();
            ItemStack output = buf.readItem();
            return new CastIronOvenRecipe(id, output, jar, input);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CastIronOvenRecipe recipe) {
            recipe.getIngredients().get(0).toNetwork(buf);
            buf.writeItem(recipe.getSecondOutput());
            buf.writeItem(recipe.getResultItem());
        }
    }
}
