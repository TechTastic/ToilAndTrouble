package net.techtastic.tat.block.custom;

public class FilteredFumeFunnelBlock extends FumeFunnelBlock {
    public FilteredFumeFunnelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public double getChance() {
        return 30.0;
    }
}
