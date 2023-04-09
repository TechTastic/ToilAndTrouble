package net.techtastic.tat.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.techtastic.tat.item.TATItems;
import net.techtastic.tat.api.ITaglockedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class MixinBedBlock {
    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    private void ToilAndTrouble$newSleptIn(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!cir.getReturnValue().equals(InteractionResult.SUCCESS)) cir.cancel();
        if (player.getItemInHand(interactionHand).is(TATItems.TAGLOCK.get()) && player.isCrouching()) cir.cancel();

        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof ITaglockedBlock duck) {
            duck.setTaggedEntity(player);
        }
    }
}
