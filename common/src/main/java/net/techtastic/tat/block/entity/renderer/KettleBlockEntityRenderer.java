package net.techtastic.tat.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.techtastic.tat.block.entity.KettleBlockEntity;

public class KettleBlockEntityRenderer implements BlockEntityRenderer<KettleBlockEntity> {
    public KettleBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(KettleBlockEntity blockEntity, float f, PoseStack poseStack,
                       MultiBufferSource multiBufferSource, int i, int j) {
        if (blockEntity.tank.getRemainingFluid() <= 0)
            return;

        BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();

        renderer.renderLiquid(
                blockEntity.getBlockPos(),
                blockEntity.getLevel(),
                multiBufferSource.getBuffer(RenderType.translucent()),
                blockEntity.getBlockState(),
                blockEntity.tank.getFluid().defaultFluidState().setValue(
                        LiquidBlock.LEVEL, (int) Math.ceil(blockEntity.tank.getPercentage() * 8))
        );
    }
}
