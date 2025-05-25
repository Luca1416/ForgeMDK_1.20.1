package net.superlucamon.luero.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.superlucamon.luero.heros.ironman.otherstuff.BeamTracking;
import net.superlucamon.luero.networking.ModPackets;

import java.util.function.Supplier;

public class StartBeamPacket {

    public StartBeamPacket() {
        // No data needed
    }

    public StartBeamPacket(FriendlyByteBuf buf) {
        // No data to decode
    }

    public void encode(FriendlyByteBuf buf) {
        // No data to encode
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                BeamTracking.startBeam(player); // server marks it
                ModPackets.sendToClientsTracking(new BeamStartedPacket(player.getUUID()), player);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
