package net.superlucamon.luero.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BeamStartS2CPacket {
    private final UUID uuid;

    public BeamStartS2CPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public BeamStartS2CPacket(FriendlyByteBuf buf) {
        this.uuid = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Update client-side data to show the beam for this player
           //ClientBeamData(uuid);
        });
        ctx.get().setPacketHandled(true);
    }
}
