package net.techtastic.tat.block.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.techtastic.tat.TATTags;
import net.techtastic.tat.api.altar.source.AltarSources;
import net.techtastic.tat.api.altar.source.IAltarSource;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.block.custom.KettleBlock;
import net.techtastic.tat.recipe.CastIronOvenRecipe;
import net.techtastic.tat.recipe.DistilleryRecipe;
import net.techtastic.tat.recipe.KettleRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class KettleBlockEntity extends BaseContainerBlockEntity implements StackedContentsCompatible, WorldlyContainer {
    public NonNullList<ItemStack> inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    private boolean isHeated = false;
    private ItemStack output = ItemStack.EMPTY;

    public KettleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TATBlockEntities.KETTLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, KettleBlockEntity kettle) {
        BlockState fire = level.getBlockState(pos.below());

        kettle.isHeated = fire.is(TATTags.Blocks.FIRE_SOURCE);

        if (!kettle.hasEnoughFluid(kettle) || !kettle.output.isEmpty()) {
            Collections.fill(kettle.inventory, ItemStack.EMPTY);
            kettle.setChanged();
            return;
        }

        if (!hasRecipe(kettle)) {
            kettle.setChanged();
            return;
        }

        BlockPos altarPos = findNearestAltar(level, kettle.worldPosition);
        IAltarSource altar = AltarSources.testForAltarSource(level, altarPos);

        if (altar != null && altar.drawPowerFromAltar(level, kettle.worldPosition, altarPos, getPowerRequired(kettle)))
            kettle.output = getRecipeOutput(kettle);

        kettle.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        ContainerHelper.saveAllItems(compoundTag, this.inventory);

        compoundTag.putBoolean("ToilAndTrouble$isHeated", this.isHeated);

        ContainerHelper.saveAllItems(compoundTag, NonNullList.of(ItemStack.EMPTY, this.output));

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.inventory);

        this.isHeated = compoundTag.getBoolean("ToilAndTrouble$isHeated");

        NonNullList<ItemStack> temp = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, temp);
        this.output = temp.get(0);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("block.tat.kettle");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return direction != null && direction.equals(Direction.UP);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 6;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return this.inventory.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack itemStack = ContainerHelper.removeItem(this.inventory, i, j);
        if (!itemStack.isEmpty())
            this.setChanged();
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack itemStack = this.inventory.get(i);
        if (itemStack.isEmpty())
            return ItemStack.EMPTY;
        this.inventory.set(i, ItemStack.EMPTY);
        return itemStack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.inventory.set(i, itemStack);
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getMaxStackSize())
            itemStack.setCount(this.getMaxStackSize());
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
        this.setChanged();
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        this.inventory.forEach(stackedContents::accountStack);
    }

    public boolean hasEnoughFluid(KettleBlockEntity kettle) {
        return false;
    }

    public abstract boolean hasEnoughForBottling(KettleBlockEntity kettle);

    public abstract boolean tryInsertFluid(KettleBlockEntity kettle, ItemStack stack);

    public abstract boolean tryExtractFluid(KettleBlockEntity kettle, ItemStack stack);

    public static ItemStack getRecipeOutput(KettleBlockEntity kettle) {
        Level level = kettle.level;

        assert level != null;
        Optional<KettleRecipe> match = level.getRecipeManager()
                .getRecipeFor(KettleRecipe.Type.INSTANCE, kettle.getContainer(), level);

        return match.isPresent() ? match.get().getOutput() : ItemStack.EMPTY;
    }

    public static int getPowerRequired(KettleBlockEntity kettle) {
        Level level = kettle.level;

        assert level != null;
        Optional<KettleRecipe> match = level.getRecipeManager()
                .getRecipeFor(KettleRecipe.Type.INSTANCE, kettle.getContainer(), level);

        return match.map(KettleRecipe::getPower).orElse(0);
    }

    public static boolean hasRecipe(KettleBlockEntity kettle) {
        Level level = kettle.level;

        assert level != null;
        Optional<KettleRecipe> match = level.getRecipeManager()
                .getRecipeFor(KettleRecipe.Type.INSTANCE, kettle.getContainer(), level);

        return match.isPresent() &&
                kettle.hasEnoughFluid(kettle) && kettle.isHeated;
    }

    public static BlockPos findNearestAltar(Level level, BlockPos center) {
        center = center.immutable();
        List<BlockPos> allPositions =
                BlockPos.betweenClosedStream(
                        BoundingBox.fromCorners(
                                center.immutable().offset(-15, -15, -15),
                                center.immutable().offset(15, 15, 15))).map(BlockPos::immutable).toList();

        BlockPos closest = null;

        for (BlockPos pos : allPositions) {
            if (AltarSources.testForAltarSource(level, pos.immutable()) == null)
                continue;

            if (closest == null || center.distSqr(closest) > center.distSqr(pos))
                closest = pos;
        }

        return closest;
    }

    private SimpleContainer getContainer() {
        SimpleContainer container = new SimpleContainer(7);

        this.inventory.forEach(stack ->
                container.setItem(this.inventory.indexOf(stack), stack)
        );

        return container;
    }

    public boolean testForNextIngredient(Level level, ItemStack stack) {
        RecipeManager rm = level.getRecipeManager();
        List<KettleRecipe> recipes = rm.getAllRecipesFor(KettleRecipe.Type.INSTANCE);

        NonNullList<ItemStack> currInv = this.inventory;
        currInv.removeIf(ItemStack::isEmpty);
        recipes.removeIf(recipe -> !recipe.getIngredients().get(
                currInv.isEmpty() ? 0 : currInv.size() - 1
        ).test(stack));
        return !recipes.isEmpty();
    }

    public void clearOutput() {
        this.output = ItemStack.EMPTY;
        this.setChanged();
    }

    public void shrinkOutput() {
        this.output.shrink(1);
        this.setChanged();
    }
}
