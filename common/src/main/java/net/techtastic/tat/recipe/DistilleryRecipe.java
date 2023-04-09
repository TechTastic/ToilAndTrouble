package net.techtastic.tat.recipe;

import com.google.gson.JsonArray;
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
        if (level.isClientSide) { return false; }


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
        outputItems.forEach(ing -> outputs.add(ing.getItems()[0]));

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
            NonNullList<Ingredient> inputs = NonNullList.create();

            ingredients.forEach(element ->
                inputs.add(Ingredient.fromJson(element))
            );

            while (inputs.size() < 4) {
                inputs.add(Ingredient.of(ItemStack.EMPTY));
            }

            // OUTPUTS

            JsonArray outputs = GsonHelper.getAsJsonArray(json, "results");
            NonNullList<Ingredient> results = NonNullList.create();

            outputs.forEach(element -> {
                Ingredient ing = Ingredient.fromJson(element);

                if (element.getAsJsonObject().has("count")) {
                    ItemStack stack = ing.getItems()[0];
                    stack.setCount(element.getAsJsonObject().get("count").getAsInt());

                    ing = Ingredient.of(stack);
                }

                results.add(ing);
            });

            while (outputs.size() < 4) {
                inputs.add(Ingredient.of(ItemStack.EMPTY));
            }

            return new DistilleryRecipe(id, inputs, results, jarCount);
        }

        @Override
        public DistilleryRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            int inputSize = buf.readInt();
            NonNullList<Ingredient> inputs = NonNullList.create();

            for (int i = 0; i < inputSize; i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            int outputSize = buf.readInt();
            NonNullList<Ingredient> results = NonNullList.create();

            for (int i = 0; i < outputSize; i++) {
                results.set(i, Ingredient.fromNetwork(buf));
            }

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
