package net.techtastic.tat.item.custom;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DebugItem extends Item {
    public DebugItem(Properties properties) {
        super(properties);
    }

    public static double radToDeg(double rad) {
        return rad * 180/Math.PI;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
        if (!player.level.isClientSide) {
            player.sendMessage(new TextComponent("Player Looking Angle X: " + radToDeg(player.getLookAngle().x)), player.getUUID());
            player.sendMessage(new TextComponent("Player Looking Angle Y: " + radToDeg(player.getLookAngle().y)), player.getUUID());
            player.sendMessage(new TextComponent("Player Looking Angle Z: " + radToDeg(player.getLookAngle().z)), player.getUUID());
            player.sendMessage(new TextComponent("Entity Looking Angle X: " + radToDeg(livingEntity.getLookAngle().x)), player.getUUID());
            player.sendMessage(new TextComponent("Entity Looking Angle Y: " + radToDeg(livingEntity.getLookAngle().y)), player.getUUID());
            player.sendMessage(new TextComponent("Entity Looking Angle Z: " + radToDeg(livingEntity.getLookAngle().z)), player.getUUID());

            Vec3 a = player.getLookAngle();
            double ax = player.getLookAngle().x;
            double ay = player.getLookAngle().y;
            double az = player.getLookAngle().z;
            Vec3 b = livingEntity.getLookAngle();
            double bx = livingEntity.getLookAngle().x;
            double by = livingEntity.getLookAngle().y;
            double bz = livingEntity.getLookAngle().z;

            double aSqr = Math.sqrt(ax*ax + ay*ay + az*az);
            double bSqr = Math.sqrt(bx*bx + by*by + bz*bz);

            double angleBetween = Math.acos(a.dot(b) / (aSqr * bSqr));
            player.sendMessage(new TextComponent("Angle Between Vectors: " + angleBetween), player.getUUID());
        }

        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }
}
