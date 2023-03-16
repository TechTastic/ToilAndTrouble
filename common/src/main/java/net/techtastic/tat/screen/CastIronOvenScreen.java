package net.techtastic.tat.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.techtastic.tat.ToilAndTrouble;

public class CastIronOvenScreen extends AbstractContainerScreen<CastIronOvenMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ToilAndTrouble.MOD_ID, "textures/gui/cast_iron_oven_gui.png");

    public CastIronOvenScreen(CastIronOvenMenu abstractContainerMenu, Inventory inventory, Component title) {
        super(abstractContainerMenu, inventory, title);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            blit(poseStack, x + 62, y + 22, 176, 14, menu.getScaledProgress(), 48);
        }

        if (menu.isConsumingFuel()) {
            blit(poseStack, x + 45, y + 37 + 14 - menu.getScaledFuelProgress(), 176,
                    14 - menu.getScaledFuelProgress(), 14, menu.getScaledFuelProgress());
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}
