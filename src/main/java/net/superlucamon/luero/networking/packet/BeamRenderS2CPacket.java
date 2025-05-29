package net.superlucamon.luero.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.superlucamon.luero.entity.renderer.ClientBeamData;

import java.util.UUID;
import java.util.function.Supplier;

public class BeamRenderS2CPacket {
    private final UUID playerId;
    private final Vec3 origin;
    private final Vec3 direction;
    private final boolean active;

    public BeamRenderS2CPacket(UUID playerId, Vec3 origin, Vec3 direction, boolean active) {
        this.playerId = playerId;
        this.origin = origin;
        this.direction = direction;
        this.active = active;
    }

    public BeamRenderS2CPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
        this.origin = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.direction = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.active = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
        buf.writeDouble(origin.x);
        buf.writeDouble(origin.y);
        buf.writeDouble(origin.z);
        buf.writeDouble(direction.x);
        buf.writeDouble(direction.y);
        buf.writeDouble(direction.z);
        buf.writeBoolean(active);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (active) {
                ClientBeamData.set(playerId, origin, direction);
            } else {
                ClientBeamData.remove(playerId);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
