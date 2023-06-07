package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.techtastic.tat.item.TATItems;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class MandrakeCropBlock extends CropBlock {
    private final IntegerProperty AGE = BlockStateProperties.AGE_5;

    private boolean spawnMandrake = false;

    public MandrakeCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return 5;
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return TATItems.MANDRAKE_SEEDS.get();
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        if (this.spawnMandrake)
            return List.of(new ItemStack(TATItems.MANDRAKE_SEEDS.get()));

        return super.getDrops(blockState, builder);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        double chance = 50.0;
        Random rand = new Random();
        if (level.isNight())
            chance = 75.0;
        double random = rand.nextDouble(100);

        System.err.println("Chance: " + chance);
        System.err.println("Random: " + random);

        if (blockState.hasProperty(AGE) && blockState.getValue(AGE) == 5 && random < chance) {
            this.spawnMandrake = true;
            Sheep sheep = new Sheep(EntityType.SHEEP, level);
            sheep.teleportTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            level.addFreshEntity(sheep);
        }

        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }
}
