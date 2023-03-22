package net.techtastic.tat.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.techtastic.tat.api.TaglockHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TaglockItem extends Item {
    public TaglockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        if (!level.isClientSide()) {
            BlockPos hitPos = useOnContext.getClickedPos();
            Player player = useOnContext.getPlayer();
            InteractionHand hand = useOnContext.getHand();
            BlockState block = level.getBlockState(hitPos);
            BlockEntity be = level.getBlockEntity(hitPos);
            ItemStack stack = player.getItemInHand(hand);

            if (stack.getCount() == 1) {
                if (!TaglockHelper.getTaglockFromBlock(stack, block.getBlock())) {
                    TaglockHelper.getTaglockFromBlockEntity(stack, be);
                }
            } else {
                ItemStack taglock = new ItemStack(stack.getItem());
                if (TaglockHelper.getTaglockFromBlock(taglock, block.getBlock())) {
                    if (player.addItem(taglock)) player.getItemInHand(hand).shrink(1);
                } else if (TaglockHelper.getTaglockFromBlockEntity(taglock, be)) {
                    if (player.addItem(taglock)) player.getItemInHand(hand).shrink(1);
                }
            }
        }

        return InteractionResult.sidedSuccess(true);
    }

    @Override
    public InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (wasTaglockSuccessful(player, entity)) {
            if (stack.getCount() == 1) {
                if (entity instanceof Player target) {
                    target.sendMessage(new TranslatableComponent("item.tat.taglock"), player.getUUID());
                    TaglockHelper.taglockPlayer(stack, target);
                } else {
                    TaglockHelper.taglockEntity(stack, entity);
                }
                player.setItemInHand(hand, stack);
            } else {
                ItemStack taglock = new ItemStack(stack.getItem());
                if (entity instanceof Player target) {
                    TaglockHelper.taglockPlayer(taglock, target);
                } else {
                    TaglockHelper.taglockEntity(taglock, entity);
                }
                if (player.addItem(taglock.copy())) player.getItemInHand(hand).shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(true);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        if (!TaglockHelper.isTaglockEmpty(itemStack)) {
            if (TaglockHelper.getDecayTicks(itemStack) <= 0) {
                list.add(new TranslatableComponent("item.tat.taglock.decayed").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000))));
            } else {
                list.add(new TextComponent(TaglockHelper.getTaglockedNameOrEmpty(itemStack)).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF00FF))));
            }
        }
    }

    public static boolean wasTaglockSuccessful(Player player, Entity target) {
        Vec3 a = player.getLookAngle();
        double ax = player.getLookAngle().x;
        double ay = player.getLookAngle().y;
        double az = player.getLookAngle().z;
        double aSqr = Math.sqrt(ax*ax + ay*ay + az*az);

        Vec3 b = target.getLookAngle();
        double bx = target.getLookAngle().x;
        double by = target.getLookAngle().y;
        double bz = target.getLookAngle().z;
        double bSqr = Math.sqrt(bx*bx + by*by + bz*bz);

        double angleBetween = Math.acos(a.dot(b) / (aSqr * bSqr));

        return angleBetween > 2.2 && angleBetween < 2.4 && !player.level.isClientSide;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);

        if (TaglockHelper.isTaglockEmpty(itemStack)) {
            int decayTimer = TaglockHelper.getDecayTicks(itemStack);
            if (decayTimer == 0) {
                TaglockHelper.removeTaglockedEntityData(itemStack);
            } else {
                decayTimer--;
                TaglockHelper.setDecayTicks(itemStack, decayTimer);
            }
        }
    }

    @Override
    public Component getName(@NotNull ItemStack itemStack) {
        if (TaglockHelper.isTaglockEmpty(itemStack)) {
            return new TranslatableComponent("item.tat.taglock").append(" (").append(new TranslatableComponent("item.tat.taglock.empty")).append(")");
        }

        if (TaglockHelper.getDecayTicks(itemStack) <= 0) {
            return new TranslatableComponent("item.tat.taglock").append(" (").append(new TranslatableComponent("item.tat.taglock.decayed")).append(")");
        }

        return new TranslatableComponent("item.tat.taglock").append(" (").append(new TranslatableComponent("item.tat.taglock.filled")).append(")");
    }
}
