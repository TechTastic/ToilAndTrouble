package net.techtastic.tat.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.techtastic.tat.api.altar.source.AltarSources;
import net.techtastic.tat.block.TATBlocks;
import net.techtastic.tat.block.custom.ArthanaBlock;

public class ArthanaSwordItem extends SwordItem {
    public ArthanaSwordItem(Tier tier, int i, float f, Properties properties) {
        super(tier, i, f, properties);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return new TranslatableComponent("block.tat.arthana");
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos pos = useOnContext.getClickedPos();
        Direction horizontal = useOnContext.getHorizontalDirection();
        Player player = useOnContext.getPlayer();
        InteractionHand hand = useOnContext.getHand();

        if (level.isClientSide || player == null || !player.isCrouching())
            return super.useOn(useOnContext);

        ItemStack arthana = useOnContext.getItemInHand();
        ItemStack newArthana = arthana.copy();
        if (arthana.getCount() > 1) {
            arthana.shrink(1);
            newArthana.setCount(1);
        } else
            player.setItemInHand(hand, ItemStack.EMPTY);

        ArthanaBlock.placeArthana(level,
                pos.above(),
                TATBlocks.ARTHANA.get().defaultBlockState()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, horizontal.getOpposite()),
                newArthana.copy()
        );

        return InteractionResult.CONSUME;
    }
}
