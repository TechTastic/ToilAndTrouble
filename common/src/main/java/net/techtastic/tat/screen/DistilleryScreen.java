package net.techtastic.tat.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.techtastic.tat.ToilAndTrouble;

public class DistilleryScreen extends AbstractContainerScreen<DistilleryMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ToilAndTrouble.MOD_ID, "textures/gui/distillery_gui.png");

    public DistilleryScreen(DistilleryMenu abstractContainerMenu, Inventory inventory, Component title) {
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
            blit(poseStack, x + 34, y + 25 + 27 - menu.getScaledPowerProgress(), 176, 29 - menu.getScaledPowerProgress(), 11, menu.getScaledPowerProgress());
            blit(poseStack, x + 68, y + 21, 194, 0, menu.getScaledCraftingProgress(), 7);
            blit(poseStack, x + 68, y + 31, 194, 0, menu.getScaledCraftingProgress(), 7);
            blit(poseStack, x + 68, y + 41, 194, 0, menu.getScaledCraftingProgress(), 7);
        }

        if (!menu.hasPower())
            blit(poseStack, x + 35, y + 56, 187, 0, 7, 7);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}
