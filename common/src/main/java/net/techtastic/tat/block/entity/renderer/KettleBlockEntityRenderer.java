package net.techtastic.tat.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.techtastic.tat.block.entity.KettleBlockEntity;

import java.awt.*;

public class KettleBlockEntityRenderer implements BlockEntityRenderer<KettleBlockEntity> {
    public KettleBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(KettleBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        if (blockEntity.tank.getRemainingFluid() > 0) {
            VertexConsumer builder = multiBufferSource.getBuffer(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS));
            Matrix4f posMatrix = poseStack.last().pose();

            Color color = blockEntity.getRecipeColor();

            TextureAtlasSprite stillTexture = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(new ResourceLocation("minecraft", "block/water_still"));

            float u1 = stillTexture.getU(10);
            float u2 = stillTexture.getU(0);
            float v1 = stillTexture.getV(10);
            float v2 = stillTexture.getV(0);

            float y = 5f/16f;

            builder.vertex(posMatrix, 5f/16f, y, 11f/16f)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .uv(u1, v2)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(0xF000F0)
                    .normal(0, 1f, 0)
                    .endVertex();
            builder.vertex(posMatrix, 11f/16f, y, 11f/16f)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .uv(u2, v2)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(0xF000F0)
                    .normal(0, 1f, 0)
                    .endVertex();
            builder.vertex(posMatrix, 11f/16f, y, 5f/16f)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .uv(u2, v1)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(0xF000F0)
                    .normal(0, 1f, 0)
                    .endVertex();
            builder.vertex(posMatrix, 5f/16f, y, 5f/16f)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .uv(u1, v1)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(0xF000F0)
                    .normal(0, 1f, 0)
                    .endVertex();
        }
    }
}
