package net.techtastic.tat.block.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.techtastic.tat.block.entity.KettleBlockEntity;

import java.awt.*;

public class KettleBlockEntityRenderer implements BlockEntityRenderer<KettleBlockEntity> {
    @Override
    public void render(KettleBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        BlockEntityRenderDispatcher renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher();

        VertexConsumer builder = multiBufferSource.getBuffer(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS));
        Matrix4f posMatrix = poseStack.last().pose();

        //The burning generator can only hold bioethanol, so no reason to do any fancy stuff here
        float[] colors = Color.GREEN.getRGB();

        TextureAtlasSprite stillTexture = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ForgeHooksClient.getFluidSprites(null, null, YOOUR_FLUID_STATE)[0].getName());

        float u1 = stillTexture.getU(10);
        float u2 = stillTexture.getU(0);
        float v1 = stillTexture.getV(10);
        float v2 = stillTexture.getV(0);

        float y = Mth.clampedLerp(0.1f, 0.85f, (float) blockEntity.amountOfFuel / BiomassBurningGeneratorTile.MAX_FUEL_CAPACITY);

        builder.vertex(posMatrix, 0.1875f, y, 0.8125f).color(colors[0], colors[1], colors[2], colors[3]).uv(u1, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(0, 1f, 0).endVertex();
        builder.vertex(posMatrix, 0.8125f, y, 0.8125f).color(colors[0], colors[1], colors[2], colors[3]).uv(u2, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(0, 1f, 0).endVertex();
        builder.vertex(posMatrix, 0.8125f, y, 0.1875f).color(colors[0], colors[1], colors[2], colors[3]).uv(u2, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(0, 1f, 0).endVertex();
        builder.vertex(posMatrix, 0.1875f, y, 0.1875f).color(colors[0], colors[1], colors[2], colors[3]).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(0, 1f, 0).endVertex();
    }
}
