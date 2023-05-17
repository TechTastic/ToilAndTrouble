package net.techtastic.tat.networking;

import dev.architectury.fluid.FluidStack;
import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.techtastic.tat.ToilAndTrouble;
import net.techtastic.tat.networking.packet.ArthanaSyncS2CPacket;
import net.techtastic.tat.networking.packet.FluidSyncS2CPacket;

public class TATNetworking {
    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation(ToilAndTrouble.MOD_ID, "networking_channel"));

    public static final ResourceLocation FLUID_SYNC_S2C_PACKET_ID = new ResourceLocation(ToilAndTrouble.MOD_ID, "fluid_sync_s2c_packet");
    public static final ResourceLocation ARTHANA_SYNC_S2C_PACKET_ID = new ResourceLocation(ToilAndTrouble.MOD_ID, "arthana_sync_s2c_packet");

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, FLUID_SYNC_S2C_PACKET_ID,
                (buf, context) -> {
            FluidStack stack = FluidStack.read(buf);
            BlockPos pos = buf.readBlockPos();
        });
        CHANNEL.register(
                FluidSyncS2CPacket.class,
                FluidSyncS2CPacket::toBytes,
                FluidSyncS2CPacket::new,
                FluidSyncS2CPacket::apply
        );

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ARTHANA_SYNC_S2C_PACKET_ID,
                (buf, context) -> new ArthanaSyncS2CPacket(buf).apply(() -> context));
        CHANNEL.register(
                ArthanaSyncS2CPacket.class,
                ArthanaSyncS2CPacket::toBytes,
                ArthanaSyncS2CPacket::new,
                ArthanaSyncS2CPacket::apply
        );
    }
}
