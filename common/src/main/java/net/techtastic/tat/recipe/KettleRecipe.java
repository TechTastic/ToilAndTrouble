package net.techtastic.tat.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class KettleRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> recipeItems;
    private final ItemStack output;
    private final int power;
    private final Color color;
    private final boolean extra;

    public KettleRecipe(ResourceLocation id, NonNullList<Ingredient> recipeItems, ItemStack output, int power, Color color, boolean extra) {
        this.id = id;
        this.recipeItems = recipeItems;
        this.output = output;
        this.power = power;
        this.color = color;
        this.extra = extra;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer container, Level level) {
        if (level.isClientSide) return false;

        for (int i = 0; i < recipeItems.size(); i++) {
            if (!recipeItems.get(i).test(container.getItem(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(@NotNull SimpleContainer container) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    public ItemStack getOutput() {
        return this.output.copy();
    }

    public int getPower() {
        return this.power;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean canGetExtra() {
        return this.extra;
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

    public static class Type implements RecipeType<KettleRecipe> {
        private Type() {}
        public static RecipeType<KettleRecipe> INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<KettleRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public KettleRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            // INPUTS
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            inputs.replaceAll(ignored ->
                ingredients.size() <= inputs.indexOf(ignored) ?
                        Ingredient.EMPTY : Ingredient.fromJson(ingredients.get(inputs.indexOf(ignored)))
            );

            // OUTPUT
            ItemStack output = Ingredient.fromJson(json.getAsJsonObject("result")).getItems()[0];

            // POWER
            int power = GsonHelper.getAsInt(json, "power", 0);

            // RENDERING SOUP COLOR
            String color = GsonHelper.getAsString(json, "color", "green");

            // CAN OUTPUT EXTRA BREW
            boolean extra = GsonHelper.getAsBoolean(json, "extra", true);

            return new KettleRecipe(id, inputs, output, power, Color.getColor(color), extra);
        }

        @Override
        public KettleRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buf));

            ItemStack output = Ingredient.fromNetwork(buf).getItems()[0];

            int power = buf.readInt();

            String color = Objects.requireNonNull(buf.readNbt()).getString("ToilAndTrouble$color");

            boolean extra = buf.readBoolean();

            return new KettleRecipe(id, inputs, output, power, Color.getColor(color), extra);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, KettleRecipe recipe) {
            NonNullList<Ingredient> input = recipe.getIngredients();
            buf.writeInt(input.size());
            input.forEach(ing -> ing.toNetwork(buf));

            Ingredient.of(recipe.getOutput()).toNetwork(buf);

            buf.writeInt(recipe.getPower());

            CompoundTag tag = new CompoundTag();
            tag.putString("ToilAndTrouble$color", recipe.getColor().toString());
            buf.writeNbt(tag);

            buf.writeBoolean(recipe.canGetExtra());
        }
    }
}
