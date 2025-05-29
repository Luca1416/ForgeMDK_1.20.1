package net.superlucamon.luero.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.superlucamon.luero.heros.ironman.otherstuff.BeamTracking;

import java.util.UUID;
import java.util.function.Supplier;

public class StartBeamPacket {
    private final UUID uuid;

    public StartBeamPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public StartBeamPacket(FriendlyByteBuf buf) {
        this.uuid = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ServerPlayer sender = ctx.get().getSender();
                if (sender != null) {
                    BeamTracking.startBeam(sender);
                }
            } else {
               // ClientBeamData.startFiring(uuid);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
