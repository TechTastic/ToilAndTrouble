package net.techtastic.tat.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.block.entity.BaseLockedBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class LockedPressurePlateBlock extends PressurePlateBlock implements EntityBlock {
    private final Sensitivity sensitivity;

    public LockedPressurePlateBlock(Sensitivity sensitivity, Properties properties) {
        super(sensitivity, properties);
        this.sensitivity = sensitivity;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BaseLockedBlockEntity(blockPos, blockState, UUID.randomUUID());
    }

    @Override
    protected int getSignalStrength(Level level, BlockPos blockPos) {
        net.minecraft.world.phys.AABB aABB = TOUCH_AABB.move(blockPos);
        List list;
        switch (this.sensitivity) {
            case EVERYTHING:
                list = level.getEntities((Entity)null, aABB);
                break;
            case MOBS:
                list = level.getEntitiesOfClass(LivingEntity.class, aABB);
                break;
            default:
                return 0;
        }

        if (!list.isEmpty()) {

            for (Object o : list) {
                Entity entity = (Entity) o;
                if (!entity.isIgnoringBlockTriggers()) {
                    boolean canUnlock = false;
                    BaseLockedBlockEntity locked = (BaseLockedBlockEntity) level.getBlockEntity(blockPos);
                    for (ItemStack hand : entity.getHandSlots()) {
                        if (canUnlock) continue;

                        assert locked != null;
                        canUnlock = locked.canUnlock(locked, hand);
                    }

                    if (canUnlock) {
                        return 15;
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        BaseLockedBlockEntity locked = (BaseLockedBlockEntity) level.getBlockEntity(blockPos);
        locked.makeInitialKey(locked, blockPos);
    }
}
