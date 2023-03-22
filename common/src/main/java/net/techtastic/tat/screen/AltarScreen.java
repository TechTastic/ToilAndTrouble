package net.techtastic.tat.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.techtastic.tat.ToilAndTrouble;

import java.util.Optional;

public class AltarScreen extends AbstractContainerScreen<AltarMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(ToilAndTrouble.MOD_ID, "textures/gui/altar_gui.png");

    public AltarScreen(AltarMenu abstractContainerMenu, Inventory inventory, Component title) {
        super(abstractContainerMenu, new Inventory(inventory.player) {
            @Override
            public Component getDisplayName() {
                return new TextComponent("");
            }
        }, title);
    }

    @Override
    protected void init() {
        this.imageWidth = 154;
        this.imageHeight = 81;
        this.titleLabelX = (this.imageWidth - this.font.width(title)) / 2;
        this.titleLabelY = this.imageHeight - 70;
        super.init();
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);

        int currentPower = menu.getCurrentPower();
        int maxPower = menu.getMaxPower();
        int rate = menu.getRate();

        TextComponent altarDisplay = new TextComponent(currentPower + " / " + maxPower + " (x" + rate + ")");
        int altarDisplayX = this.width / 2;
        int altarDisplayY = this.height / 2;

        drawCenteredString(poseStack, this.font, altarDisplay, altarDisplayX, altarDisplayY, 0xFFFFFF);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
    }
}
