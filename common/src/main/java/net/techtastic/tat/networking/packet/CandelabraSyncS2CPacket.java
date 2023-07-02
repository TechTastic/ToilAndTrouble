package net.techtastic.tat.networking.packet;

import dev.architectury.fluid.FluidStack;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.techtastic.tat.block.entity.CandelabraBlockEntity;
import net.techtastic.tat.block.entity.KettleBlockEntity;

import java.util.function.Supplier;

public class CandelabraSyncS2CPacket {
    private final int northColor;
    private final int southColor;
    private final int eastColor;
    private final int westColor;
    private final int centerColor;
    private final BlockPos pos;

    public CandelabraSyncS2CPacket(int northColor, int southColor, int eastColor, int westColor, int centerColor, BlockPos pos) {
        this.northColor = northColor;
        this.southColor = southColor;
        this.eastColor = eastColor;
        this.westColor = westColor;
        this.centerColor = centerColor;
        this.pos = pos;
    }

    public CandelabraSyncS2CPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readBlockPos());
    }

    public CandelabraSyncS2CPacket(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        this(buf);
        this.apply(() -> context);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(northColor);
        buf.writeInt(southColor);
        buf.writeInt(eastColor);
        buf.writeInt(westColor);
        buf.writeInt(centerColor);
        buf.writeBlockPos(pos);
    }

    public void apply(Supplier<NetworkManager.PacketContext> contextSupplier) {
        contextSupplier.get().queue(() -> {
            assert Minecraft.getInstance().level != null;
            BlockEntity be = Minecraft.getInstance().level.getBlockEntity(pos);
            if (be instanceof CandelabraBlockEntity candelabra) {
                candelabra.setNorthCandleColor(this.northColor);
                candelabra.setSouthCandleColor(this.southColor);
                candelabra.setEastCandleColor(this.eastColor);
                candelabra.setWestCandleColor(this.westColor);
                candelabra.setCenterCandleColor(this.centerColor);
            }
        });
    }
}
