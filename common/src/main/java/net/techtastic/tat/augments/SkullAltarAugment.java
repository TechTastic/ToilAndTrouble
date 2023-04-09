package net.techtastic.tat.augments;

import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.techtastic.tat.api.IAltarAugment;

public class SkullAltarAugment implements IAltarAugment {
    private final BlockState state;

    public SkullAltarAugment(BlockState state) {
        this.state = state;
    }

    @Override
    public String getType() {
        return "head";
    }

    @Override
    public int getTypePriority() {
        SkullBlock.Type type = ((AbstractSkullBlock)state.getBlock()).getType();
        return switch (SkullBlock.Types.valueOf(type.toString())) {
            case PLAYER -> 5;
            case WITHER_SKELETON -> 10;
            case SKELETON -> 15;
            default -> 0;
        };
    }

    @Override
    public boolean matches(IAltarAugment augment) {
        return augment instanceof SkullAltarAugment;
    }

    @Override
    public double boostAltarRechargeRate(double initRate) {
        SkullBlock.Type type = ((AbstractSkullBlock)state.getBlock()).getType();
        return initRate += switch (SkullBlock.Types.valueOf(type.toString())) {
            case PLAYER -> 3;
            case WITHER_SKELETON -> 2;
            case SKELETON -> 1;
            default -> 0;
        };
    }

    @Override
    public double modifyMaxAltarPower(double initPower) {
        SkullBlock.Type type = ((AbstractSkullBlock)state.getBlock()).getType();
        return initPower += switch (SkullBlock.Types.valueOf(type.toString())) {
            case PLAYER -> initPower *= 2.5;
            case WITHER_SKELETON -> initPower *= 2;
            case SKELETON -> initPower *= 1;
            default -> 0;
        };
    }
}
