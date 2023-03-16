package net.techtastic.tat.forge.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.techtastic.tat.TATBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IForgeBlock.class)
public class MixinIForgeBlock {
    @Inject(
            method = "getToolModifiedState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/context/UseOnContext;Lnet/minecraftforge/common/ToolAction;Z)Lnet/minecraft/world/level/block/state/BlockState;",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void ToilAndTrouble$getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate, CallbackInfoReturnable<BlockState> cir) {
        EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

        if(context.getItemInHand().getItem() instanceof AxeItem) {
            if (state.is(TATBlocks.ROWAN_WOOD.get())) cir.setReturnValue(TATBlocks.STRIPPED_ROWAN_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS)));
            if (state.is(TATBlocks.ROWAN_LOG.get())) cir.setReturnValue(TATBlocks.STRIPPED_ROWAN_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS)));
            if (state.is(TATBlocks.HAWTHORN_WOOD.get())) cir.setReturnValue(TATBlocks.STRIPPED_HAWTHORN_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS)));
            if (state.is(TATBlocks.HAWTHORN_LOG.get())) cir.setReturnValue(TATBlocks.STRIPPED_HAWTHORN_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS)));
            if (state.is(TATBlocks.ALDER_WOOD.get())) cir.setReturnValue(TATBlocks.STRIPPED_ALDER_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS)));
            if (state.is(TATBlocks.ALDER_LOG.get())) cir.setReturnValue(TATBlocks.STRIPPED_ALDER_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS)));
        }
    }
}
