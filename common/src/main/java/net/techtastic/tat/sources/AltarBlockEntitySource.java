package net.techtastic.tat.sources;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.techtastic.tat.api.altar.source.IAltarSource;
import net.techtastic.tat.block.entity.AltarBlockEntity;

public class AltarBlockEntitySource implements IAltarSource {
    private final AltarBlockEntity altar;

    public AltarBlockEntitySource(AltarBlockEntity altar) {
        this.altar = altar;
    }

    @Override
    public double getMaxPower() {
        return this.altar.getMaxPower();
    }

    @Override
    public double getCurrentPower() {
        return this.altar.getCurrentPower();
    }

    @Override
    public double getRange() {
        return this.altar.getRange();
    }

    @Override
    public double getRate() {
        return this.altar.getRate();
    }

    @Override
    public void setMaxPower(double newMaxPower) {
        this.altar.setMaxPower(newMaxPower);
    }

    @Override
    public void setCurrentPower(double newPower) {
        this.altar.setCurrentPower(newPower);
    }

    @Override
    public void setRange(double newRange) {
        this.altar.setRange(newRange);
    }

    @Override
    public void setRate(double newRate) {
        this.altar.setRate(newRate);
    }

    @Override
    public boolean drawPowerFromAltar(Level level, BlockPos sink, BlockPos source, double amount) {
        return this.altar.drawPowerFromAltar(level, sink, source, amount);
    }
}
