package net.techtastic.tat.block.entity.renderer;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.techtastic.tat.TATPartials;
import net.techtastic.tat.block.entity.CandelabraBlockEntity;
import net.techtastic.tat.util.BakedModelRenderHelper;
import net.techtastic.tat.util.SuperByteBuffer;

public class CandelabraBlockEntityRenderer implements BlockEntityRenderer<CandelabraBlockEntity> {
    public CandelabraBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CandelabraBlockEntity be, float f, PoseStack ps, MultiBufferSource buffer, int i, int j) {
        VertexConsumer vc = buffer.getBuffer(RenderType.cutout());
        BlockState state = be.getBlockState();
        boolean lit = state.getValue(BlockStateProperties.LIT);

        placeCandleWithOffset(ps, vc, state, lit, -3, 6, 0, be.getNorthCandleColor());
    }

    private void placeCandleWithOffset(PoseStack ps, VertexConsumer vc, BlockState state,
                                       boolean lit, double xOffset, double yOffset, double zOffset, int color) {
        SuperByteBuffer candle = BakedModelRenderHelper.standardModelRender(
                lit ? TATPartials.CANDLE_LIT.get() : TATPartials.CANDLE.get(), state, ps);

        if (color != -1)
            candle = candle.color(color);

        candle.light()
                .translateX(xOffset)
                .translateY(yOffset)
                .translateZ(zOffset)
                .renderInto(ps, vc);
    }
}
