package net.superlucamon.luero.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.superlucamon.luero.networking.ModPackets;

import java.util.function.Supplier;

public class StopBeamPacket {
    public StopBeamPacket() {}

    public StopBeamPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ModPackets.sendToClientsTracking(player, new ClientOnlyStopBeamPacket(player.getUUID()));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
