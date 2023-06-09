package net.techtastic.tat.networking;

import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import net.techtastic.tat.ToilAndTrouble;
import net.techtastic.tat.networking.packet.ArthanaSyncS2CPacket;
import net.techtastic.tat.networking.packet.CandelabraSyncS2CPacket;
import net.techtastic.tat.networking.packet.FluidSyncS2CPacket;

public class TATNetworking {
    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation(ToilAndTrouble.MOD_ID, "networking_channel"));

    public static final ResourceLocation FLUID_SYNC_S2C_PACKET_ID =
            new ResourceLocation(ToilAndTrouble.MOD_ID, "fluid_sync_s2c_packet");
    public static final ResourceLocation ARTHANA_SYNC_S2C_PACKET_ID =
            new ResourceLocation(ToilAndTrouble.MOD_ID, "arthana_sync_s2c_packet");
    public static final ResourceLocation CANDELABRA_SYNC_S2C_PACKET_ID =
            new ResourceLocation(ToilAndTrouble.MOD_ID, "candelabra_sync_s2c_packet");

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, FLUID_SYNC_S2C_PACKET_ID,
                FluidSyncS2CPacket::new);
        CHANNEL.register(
                FluidSyncS2CPacket.class,
                FluidSyncS2CPacket::toBytes,
                FluidSyncS2CPacket::new,
                FluidSyncS2CPacket::apply
        );

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ARTHANA_SYNC_S2C_PACKET_ID,
                ArthanaSyncS2CPacket::new);
        CHANNEL.register(
                ArthanaSyncS2CPacket.class,
                ArthanaSyncS2CPacket::toBytes,
                ArthanaSyncS2CPacket::new,
                ArthanaSyncS2CPacket::apply
        );

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CANDELABRA_SYNC_S2C_PACKET_ID,
                CandelabraSyncS2CPacket::new);
        CHANNEL.register(
                CandelabraSyncS2CPacket.class,
                CandelabraSyncS2CPacket::toBytes,
                CandelabraSyncS2CPacket::new,
                CandelabraSyncS2CPacket::apply
        );
    }
}
