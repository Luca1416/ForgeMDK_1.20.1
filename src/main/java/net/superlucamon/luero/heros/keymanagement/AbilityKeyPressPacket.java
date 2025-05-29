package net.superlucamon.luero.heros.keymanagement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class AbilityKeyPressPacket {
    private final int keyIndex;

    public AbilityKeyPressPacket(int keyIndex) {
        this.keyIndex = keyIndex;
    }

    public static void encode(AbilityKeyPressPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.keyIndex);
    }

    public static AbilityKeyPressPacket decode(FriendlyByteBuf buf) {
        return new AbilityKeyPressPacket(buf.readInt());
    }

    public static void handle(AbilityKeyPressPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            // Trigger the bound ability for the current hero
            var hero = net.superlucamon.luero.server.HeroManager.getPlayerHero(player);
            if (hero != null) {
                var bindings = hero.getBindings().entrySet().stream().toList();
                if (packet.keyIndex >= 0 && packet.keyIndex < bindings.size()) {
                    var handler = bindings.get(packet.keyIndex).getValue();
                    handler.handle(player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
