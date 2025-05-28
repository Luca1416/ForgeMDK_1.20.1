/*package net.superlucamon.luero.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.superlucamon.luero.entity.renderer.ClientBeamData;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientOnlyStartBeamPacket {
    private final UUID playerId;

    public ClientOnlyStartBeamPacket(UUID id) {
        this.playerId = id;
    }

    public ClientOnlyStartBeamPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientBeamData.start(playerId));
        ctx.get().setPacketHandled(true);
    }
}

 */
