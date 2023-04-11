package net.techtastic.tat.block.entity;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.techtastic.tat.api.altar.source.AltarSources;
import net.techtastic.tat.api.altar.source.IAltarSource;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.block.custom.DistilleryBlock;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.recipe.DistilleryRecipe;
import net.techtastic.tat.screen.DistilleryMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DistilleryBlockEntity extends BaseContainerBlockEntity implements StackedContentsCompatible, WorldlyContainer, ExtendedMenuProvider {
    public NonNullList<ItemStack> inventory;
    protected final ContainerData data;
    private int craftProgress = 0;
    private int maxCraftProgress = 38;
    private int powerProgress = 0;
    private int maxPowerProgress = 4;
    private boolean hasAltar = false;
    private int ticks = 1;
    private BlockPos altarPos = null;

    public DistilleryBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TATBlockEntities.DISTILLERY_BLOCK_ENTITY.get(), blockPos, blockState);
        this.inventory = NonNullList.withSize(7, ItemStack.EMPTY);

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> DistilleryBlockEntity.this.craftProgress;
                    case 1 -> DistilleryBlockEntity.this.maxCraftProgress;
                    case 2 -> DistilleryBlockEntity.this.powerProgress;
                    case 3 -> DistilleryBlockEntity.this.maxPowerProgress;
                    case 4 -> DistilleryBlockEntity.this.hasAltar ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int j) {
                switch (i) {
                    case 0 -> DistilleryBlockEntity.this.craftProgress = j;
                    case 1 -> DistilleryBlockEntity.this.maxCraftProgress = j;
                    case 2 -> DistilleryBlockEntity.this.powerProgress = j;
                    case 3 -> DistilleryBlockEntity.this.maxPowerProgress = j;
                    case 4 -> DistilleryBlockEntity.this.hasAltar = j == 1;
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.tat.distillery");
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("block.tat.distillery");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory) {
        return new DistilleryMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        ContainerHelper.saveAllItems(compoundTag, this.inventory);

        compoundTag.putInt("ToilAndTrouble$craftProgress", this.craftProgress);
        compoundTag.putInt("ToilAndTrouble$powerProgress", this.powerProgress);
        compoundTag.putBoolean("ToilAndTrouble$hasAltar", this.hasAltar);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);

        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.inventory);

        this.craftProgress = compoundTag.getInt("ToilAndTrouble$craftProgress");
        this.powerProgress = compoundTag.getInt("ToilAndTrouble$powerProgress");
        this.hasAltar = compoundTag.getBoolean("ToilAndTrouble$hasAltar");
    }

    public void drops() {
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, this);
    }

    public SimpleContainer getContainer() {
        SimpleContainer container = new SimpleContainer(7);

        this.inventory.forEach(stack ->
            container.setItem(this.inventory.indexOf(stack), stack)
        );

        return container;
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, DistilleryBlockEntity entity) {
        if (pLevel.isClientSide) return;
        int ticks = entity.getTicks();

        if (ticks % 20 == 0 || entity.altarPos == null) {
            entity.altarPos = findNearestAltar(pLevel, pPos);
            entity.hasAltar = AltarSources.testForAltarSource(pLevel, entity.altarPos) != null;
            entity.resetTicks();
            entity.setChanged();
        }

        int jars = getJarCount(entity);
        if (pState.getValue(DistilleryBlock.JARS) != jars) {
            pState.setValue(DistilleryBlock.JARS, switch (jars) {
                case 0, 1, 2, 3, 4 -> jars;
                default -> 4;
            });
        }

        if (!hasRecipe(entity)) {
            if (pState.getValue(DistilleryBlock.POWERED))
                pLevel.setBlockAndUpdate(pPos, pState.setValue(DistilleryBlock.POWERED, false));
            entity.resetCraftProgress();
            entity.resetPowerProgress();
            entity.setChanged();
            return;
        }

        if (ticks % 5 == 0) {
            IAltarSource altar = AltarSources.testForAltarSource(pLevel, entity.altarPos);
            if (altar == null)
                return;

            boolean pullPower = altar.drawPowerFromAltar(pLevel, pPos, entity.altarPos, 2);
            if (!pullPower) {
                if (pState.getValue(DistilleryBlock.POWERED))
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(DistilleryBlock.POWERED, false));
                entity.hasAltar = false;
                entity.incrementTicks();
                entity.setChanged();
                return;
            }

            if (!pState.getValue(DistilleryBlock.POWERED))
                pLevel.setBlockAndUpdate(pPos, pState.setValue(DistilleryBlock.POWERED, true));

            entity.powerProgress++;
            if (entity.powerProgress > entity.maxPowerProgress) {
                entity.craftProgress++;
                entity.resetPowerProgress();
            }

            if (entity.craftProgress > entity.maxCraftProgress) {
                craftItem(entity);
                entity.resetCraftProgress();
            }
        }

        entity.incrementTicks();
        entity.setChanged();
    }

    public int getTicks() {
        return this.ticks;
    }

    public void incrementTicks() {
        this.ticks += 1;
    }

    public void resetTicks() {
        this.ticks = 1;
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

    private static boolean hasRecipe(DistilleryBlockEntity entity) {
        Level level = entity.level;

        assert level != null;
        Optional<DistilleryRecipe> match = level.getRecipeManager()
                .getRecipeFor(DistilleryRecipe.Type.INSTANCE, entity.getContainer(), level);

        return match.isPresent() &&
                canInsertItemsIntoOutputSlots(entity, match.get().getOutputs()) &&
                hasJarInSlot(entity, match.get().getJarCount()) && entity.hasAltar;
    }

    private static boolean hasJarInSlot(DistilleryBlockEntity entity, int required) {
        return entity.getItem(2).is(TATItems.CLAY_JAR.get()) && entity.getItem(2).getCount() >= required;
    }

    private static int getJarCount(DistilleryBlockEntity entity) {
        return entity.getItem(2).getCount();
    }

    private static void craftItem(DistilleryBlockEntity entity) {
        Level level = entity.level;

        assert level != null;
        Optional<DistilleryRecipe> match = level.getRecipeManager()
                .getRecipeFor(DistilleryRecipe.Type.INSTANCE, entity.getContainer(), level);

        if (match.isEmpty()) return;

        DistilleryRecipe recipe = match.get();
        NonNullList<ItemStack> outputs = recipe.getOutputs();

        entity.removeItem(0, 1);
        entity.removeItem(1, 1);
        entity.removeItem(2, recipe.getJarCount());

        addOutput(entity, outputs.get(0), 3);
        addOutput(entity, outputs.get(1), 4);
        addOutput(entity, outputs.get(2), 5);
        addOutput(entity, outputs.get(3), 6);
    }

    private static void addOutput(DistilleryBlockEntity entity, ItemStack recipeOutput, int slot) {
        entity.setItem(slot, new ItemStack(recipeOutput.getItem(), entity.getItem(slot).getCount() + recipeOutput.getCount()));
    }

    private void resetCraftProgress() {
        this.craftProgress = 0;
    }

    private void resetPowerProgress() {
        this.powerProgress = 0;
    }

    private static boolean canInsertItemIntoOutputSlot(DistilleryBlockEntity inventory, ItemStack output, int slot) {
        return inventory.getItem(slot).getItem() == output.getItem() || inventory.getItem(slot).isEmpty();
    }

    private static boolean canInsertItemsIntoOutputSlots(DistilleryBlockEntity inventory, List<ItemStack> results) {
        return canInsertItemIntoOutputSlot(inventory, results.get(0), 3) && canInsertAmountIntoOutputSlot(inventory, 3) &&
                canInsertItemIntoOutputSlot(inventory, results.get(1), 4) && canInsertAmountIntoOutputSlot(inventory, 4) &&
                canInsertItemIntoOutputSlot(inventory, results.get(2), 5) && canInsertAmountIntoOutputSlot(inventory, 5) &&
                canInsertItemIntoOutputSlot(inventory, results.get(3), 6) && canInsertAmountIntoOutputSlot(inventory, 6);
    }

    private static boolean canInsertAmountIntoOutputSlot(DistilleryBlockEntity inventory, int slot) {
        return inventory.getItem(slot).getMaxStackSize() > inventory.getItem(slot).getCount();
    }

    @Override
    public int getContainerSize() {
        return 7;
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
    public void setItem(int i, @NotNull ItemStack itemStack) {
        this.inventory.set(i, itemStack);
        if (!itemStack.isEmpty() && itemStack.getCount() > this.getMaxStackSize())
            itemStack.setCount(this.getMaxStackSize());
        this.setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
        this.setChanged();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return switch (direction) {
            case UP, NORTH, SOUTH, WEST, EAST -> new int[] {0, 1, 2};
            case DOWN -> new int[] {3, 4, 5, 6};
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return switch (i) {
            case 0, 1 -> true;
            case 2 -> itemStack.is(TATItems.CLAY_JAR.get());
            default -> false;
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return switch (i) {
            case 0, 1, 2 -> false;
            default -> true;
        };
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        this.inventory.forEach(stackedContents::accountStack);
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
    }
}
