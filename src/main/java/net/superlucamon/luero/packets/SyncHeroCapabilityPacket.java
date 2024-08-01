package net.superlucamon.luero.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.superlucamon.luero.server.HeroCapabilityProvider;
import net.superlucamon.luero.server.IHeroCapability;

import java.util.function.Supplier;

public class SyncHeroCapabilityPacket {

    private CompoundTag  data;

    public SyncHeroCapabilityPacket(CompoundTag data) {
        this.data = data;
    }

    public static void encode(SyncHeroCapabilityPacket message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.data);
    }

    public static SyncHeroCapabilityPacket decode(FriendlyByteBuf buffer) {
        return new SyncHeroCapabilityPacket(buffer.readNbt());
    }

    public static void handle(SyncHeroCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                IHeroCapability heroCapability = player.getCapability(HeroCapabilityProvider.HERO_CAPABILITY).orElse(null);
                if (heroCapability != null) {
                    heroCapability.deserializeNBT(message.data);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}