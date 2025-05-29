package net.superlucamon.luero.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BeamStopS2CPacket {
    private final UUID uuid;

    public BeamStopS2CPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public BeamStopS2CPacket(FriendlyByteBuf buf) {
        this.uuid = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Update client-side data to stop showing the beam for this player
            //ClientBeamData.stopFiring(uuid);
        });
        ctx.get().setPacketHandled(true);
    }
}
