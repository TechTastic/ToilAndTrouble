package net.techtastic.tat.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DistilleryRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> recipeItems;
    private final NonNullList<Ingredient> outputItems;
    private final int jarCount;

    public DistilleryRecipe(ResourceLocation id, NonNullList<Ingredient> recipeItems, NonNullList<Ingredient> outputItems, int jarCount) {
        this.id = id;
        this.recipeItems = recipeItems;
        this.outputItems = outputItems;
        this.jarCount = jarCount;
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

    public NonNullList<Ingredient> getResults() {
        return outputItems;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    public ItemStack getOutput() {
        return ItemStack.EMPTY;
    } // Was Needed

    public NonNullList<ItemStack> getOutputs() {
        NonNullList<ItemStack> outputs = NonNullList.create();
        outputItems.forEach(ing -> outputs.add(Arrays.stream(ing.getItems()).findAny().orElse(ItemStack.EMPTY)));

        return outputs;
    }

    public int getJarCount() {
        return jarCount;
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

    public static class Type implements RecipeType<DistilleryRecipe> {
        private Type() {}
        public static RecipeType<DistilleryRecipe> INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<DistilleryRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public DistilleryRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            int jarCount = GsonHelper.getAsInt(json, "jars");

            // INPUTS

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            inputs.replaceAll(ignored ->
                ingredients.size() <= inputs.indexOf(ignored) ?
                        Ingredient.EMPTY : Ingredient.fromJson(ingredients.get(inputs.indexOf(ignored)))
            );

            // OUTPUTS

            JsonArray outputs = GsonHelper.getAsJsonArray(json, "results");
            NonNullList<Ingredient> results = NonNullList.withSize(4, Ingredient.EMPTY);

            results.replaceAll(ignored -> {
                if (outputs.size() <= results.indexOf(ignored)) return Ingredient.EMPTY;

                JsonElement element = outputs.get(results.indexOf(ignored));
                Ingredient ing = Ingredient.fromJson(element);

                if (element.getAsJsonObject().has("count")) {
                    ItemStack stack = ing.getItems()[0];
                    stack.setCount(element.getAsJsonObject().get("count").getAsInt());

                    ing = Ingredient.of(stack);
                }

                return ing;
            });

            return new DistilleryRecipe(id, inputs, results, jarCount);
        }

        @Override
        public DistilleryRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buf));

            NonNullList<Ingredient> results = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            results.replaceAll(ignored -> Ingredient.fromNetwork(buf));

            int jarCount = buf.readInt();

            return new DistilleryRecipe(id, inputs, results, jarCount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, DistilleryRecipe recipe) {
            NonNullList<Ingredient> input = recipe.getIngredients();
            buf.writeInt(input.size());
            input.forEach(ing -> ing.toNetwork(buf));

            NonNullList<Ingredient> output = recipe.getResults();
            buf.writeInt(output.size());
            output.forEach(ing -> ing.toNetwork(buf));

            buf.writeInt(recipe.getJarCount());
        }
    }
}
