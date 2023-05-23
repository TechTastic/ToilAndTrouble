package net.techtastic.tat.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.techtastic.tat.block.entity.KettleBlockEntity;
import org.jetbrains.annotations.NotNull;

public class KettleBlockEntityRenderer implements BlockEntityRenderer<KettleBlockEntity> {
    public KettleBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(KettleBlockEntity blockEntity, float f, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource multiBufferSource, int i, int j) {
        if (blockEntity.tank.getRemainingFluid() <= 0)
            return;

        BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();

        FluidState fluid = blockEntity.tank.getFluid().defaultFluidState();

        renderer.renderLiquid(
                blockEntity.getBlockPos(),
                blockEntity.getLevel(),
                multiBufferSource.getBuffer(RenderType.translucent()),
                Blocks.WATER.defaultBlockState(),
                fluid
        );
    }
}