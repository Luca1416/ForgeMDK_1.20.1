package net.superlucamon.luero.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.superlucamon.luero.networking.ModPackets;
import net.superlucamon.luero.networking.packet.BeamRenderS2CPacket;

import java.util.function.Supplier;

public class BeamRenderC2SPacket {
    private final Vec3 origin;
    private final Vec3 direction;
    private final boolean active;

    public BeamRenderC2SPacket(Vec3 origin, Vec3 direction, boolean active) {
        this.origin = origin;
        this.direction = direction;
        this.active = active;
    }

    public BeamRenderC2SPacket(FriendlyByteBuf buf) {
        this.origin = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.direction = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.active = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
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
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ModPackets.sendToAllTrackingAndSelf(
                        new BeamRenderS2CPacket(player.getUUID(), origin, direction, active), player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
