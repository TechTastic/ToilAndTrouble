package net.techtastic.tat.block.entity;

import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.techtastic.tat.block.TATBlockEntities;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.api.IFumeFunnel;
import net.techtastic.tat.block.custom.FumeFunnelBlock;
import net.techtastic.tat.recipe.CastIronOvenRecipe;
import net.techtastic.tat.screen.CastIronOvenMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class CastIronOvenBlockEntity extends BaseContainerBlockEntity implements StackedContentsCompatible, WorldlyContainer, ExtendedMenuProvider {
    public NonNullList<ItemStack> inventory;
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public CastIronOvenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TATBlockEntities.CAST_IRON_OVEN_BLOCK_ENTITY.get(), blockPos, blockState);
        this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> CastIronOvenBlockEntity.this.progress;
                    case 1 -> CastIronOvenBlockEntity.this.maxProgress;
                    case 2 -> CastIronOvenBlockEntity.this.fuelTime;
                    case 3 -> CastIronOvenBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int j) {
                switch (i) {
                    case 0 -> CastIronOvenBlockEntity.this.progress = j;
                    case 1 -> CastIronOvenBlockEntity.this.maxProgress = j;
                    case 2 -> CastIronOvenBlockEntity.this.fuelTime = j;
                    case 3 -> CastIronOvenBlockEntity.this.maxFuelTime = j;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.tat.cast_iron_oven");
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("block.tat.cast_iron_oven");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new CastIronOvenMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        ContainerHelper.saveAllItems(compoundTag, this.inventory);

        compoundTag.putInt("ToilAndTrouble$progress", this.progress);
        compoundTag.putInt("ToilAndTrouble$fuelTime", this.fuelTime);
        compoundTag.putInt("ToilAndTrouble$maxFuelTime", this.maxFuelTime);

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);

        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.inventory);

        this.progress = compoundTag.getInt("ToilAndTrouble$progress");
        this.fuelTime = compoundTag.getInt("ToilAndTrouble$fuelTime");
        this.maxFuelTime = compoundTag.getInt("ToilAndTrouble$maxFuelTime");
    }

    public void drops() {
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, this);
    }

    public SimpleContainer getContainer() {
        SimpleContainer container = new SimpleContainer(5);

        this.inventory.forEach(stack ->
            container.setItem(this.inventory.indexOf(stack), stack)
        );

        return container;
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, CastIronOvenBlockEntity entity) {
        if(entity.isConsumingFuel(entity)) {
            if (!pState.getValue(BlockStateProperties.LIT)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.LIT, true));
                updateAllFumeFunnels(pLevel, pPos, pState);
            }
            entity.fuelTime--;
        } else {
            if (pState.getValue(BlockStateProperties.LIT)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.LIT, false));
                updateAllFumeFunnels(pLevel, pPos, pState);
            }
        }

        if (!hasRecipe(entity)) {
            entity.resetProgress();
            setChanged(pLevel, pPos, pState);
            return;
        }

        if(hasFuelInFuelSlot(entity) && !entity.isConsumingFuel(entity))
            entity.consumeFuel(entity);

        if (!entity.isConsumingFuel(entity))
            return;

        entity.progress++;
        if (entity.progress > entity.maxProgress) {
            craftItem(entity);
            entity.resetProgress();
        }

        setChanged(pLevel, pPos, pState);
    }

    private static void updateAllFumeFunnels(Level level, BlockPos pos, BlockState state) {
        BooleanProperty LIT = BlockStateProperties.LIT;

        getAllFumeFunnels(level, state, pos).forEach((testState, testPos) -> {
            if (testState.getValue(LIT).equals(state.getValue(LIT)))
                return;

            testState.setValue(LIT, state.getValue(LIT));
            level.setBlockAndUpdate(testPos, testState);
        });
    }

    private static boolean hasRecipe(CastIronOvenBlockEntity entity) {
        Level level = entity.level;

        assert level != null;
        Optional<CastIronOvenRecipe> match = level.getRecipeManager()
                .getRecipeFor(CastIronOvenRecipe.Type.INSTANCE, entity.getContainer(), level);

        if (match.isEmpty())
            return false;

        int defaultMaxProgress = 200;

        //If the recipe is food or charcoal, 10% quicker
        Ingredient input = match.get().getIngredients().get(0);
        ItemStack stack = input.getItems()[0];
        if (stack.isEdible() || stack.is(ItemTags.LOGS))
            defaultMaxProgress -= 20;


        //For all funnels, 10% quicker
        defaultMaxProgress -= (20 * getAllFunnels(entity).size());

        if (entity.maxProgress != defaultMaxProgress) {
            entity.maxProgress = defaultMaxProgress;
            entity.setChanged();
        }

        return canInsertAmountIntoOutputSlot(entity) && canInsertItemIntoOutputSlot(entity, match.get().getResultItem());
    }

    private static boolean hasJarInSlot(CastIronOvenBlockEntity entity) {
        return entity.getItem(2).is(TATItems.CLAY_JAR.get());
    }

    private static boolean canFitInJarOutput(CastIronOvenBlockEntity entity, ItemStack stack) {
        ItemStack currOutput = entity.getItem(4);
        return currOutput.isEmpty() || (currOutput.is(stack.getItem()) && currOutput.getCount() < currOutput.getMaxStackSize());
    }

    private static double jarChance(CastIronOvenBlockEntity entity) {
        AtomicReference<Double> totalChance = new AtomicReference<>(10.0);

        getAllFunnels(entity).forEach(funnel ->
            totalChance.updateAndGet(v -> v + funnel.getChance())
        );

        return totalChance.get();
    }

    public static List<IFumeFunnel> getAllFunnels(CastIronOvenBlockEntity oven) {
        Level level = oven.level;
        Direction init = oven.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        List<IFumeFunnel> list = new ArrayList<>(3);

        List.of(init.getCounterClockWise(), init.getClockWise(), Direction.UP).forEach(direction -> {
            BlockPos testBlock = oven.worldPosition.relative(direction);
            assert level != null;
            if (level.getBlockState(testBlock).getBlock() instanceof IFumeFunnel funnel)
                list.add(funnel);
            else if (level.getBlockEntity(testBlock) instanceof IFumeFunnel funnel)
                list.add(funnel);
        });

        return list;
    }

    public static HashMap<BlockState, BlockPos> getAllFumeFunnels(Level level, BlockState state, BlockPos pos) {
        HashMap<BlockState, BlockPos> list = new HashMap<>(3);
        Direction init = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

        List.of(init.getCounterClockWise(), init.getClockWise(), Direction.UP).forEach(direction -> {
            BlockPos testBlock = pos.relative(direction);
            BlockState testState = level.getBlockState(testBlock);
            if (!(testState.getBlock() instanceof FumeFunnelBlock))
                return;
            if (!testState.getValue(BlockStateProperties.HORIZONTAL_FACING).equals(init))
                return;
            list.put(testState, testBlock);
        });

        return list;
    }

    private static void craftItem(CastIronOvenBlockEntity entity) {
        Level level = entity.level;

        assert level != null;
        Optional<CastIronOvenRecipe> match = level.getRecipeManager()
                .getRecipeFor(CastIronOvenRecipe.Type.INSTANCE, entity.getContainer(), level);

        if (match.isEmpty()) return;

        entity.removeItem(0, 1);

        entity.setItem(3, new ItemStack(match.get().getResultItem().getItem(),
                entity.getItem(3).getCount() + 1));

        double ran = new Random().nextDouble(100);
        if (!(hasJarInSlot(entity) && jarChance(entity) > ran && canFitInJarOutput(entity, match.get().getSecondOutput())))
            return;

        entity.removeItem(2, 1);
        entity.setItem(4, new ItemStack(match.get().getSecondOutput().getItem(),
                entity.getItem(4).getCount() + 1));
    }

    private void consumeFuel(CastIronOvenBlockEntity entity) {
        if (entity.getItem(1).isEmpty())
            return;

        this.fuelTime = FuelRegistry.get(entity.removeItem(1, 1));
        this.maxFuelTime = this.fuelTime;
        this.setChanged();
    }

    private static boolean hasFuelInFuelSlot(CastIronOvenBlockEntity entity) {
        return !entity.getItem(1).isEmpty() && FuelRegistry.get(entity.getItem(1)) != 0;
    }

    public boolean isConsumingFuel(CastIronOvenBlockEntity entity) {
        return entity.fuelTime > 0;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static boolean canInsertItemIntoOutputSlot(CastIronOvenBlockEntity inventory, ItemStack output) {
        return inventory.getItem(3).getItem() == output.getItem() || inventory.getItem(3).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(CastIronOvenBlockEntity inventory) {
        return inventory.getItem(3).getMaxStackSize() > inventory.getItem(3).getCount();
    }

    @Override
    public int getContainerSize() {
        return 5;
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
            case DOWN -> new int[] {3, 4};
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return switch (i) {
            case 0 -> true;
            case 1 -> FuelRegistry.get(itemStack) > 0;
            case 2 -> itemStack.is(TATItems.CLAY_JAR.get());
            default -> false;
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return switch (i) {
            case 3, 4 -> true;
            default -> false;
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
