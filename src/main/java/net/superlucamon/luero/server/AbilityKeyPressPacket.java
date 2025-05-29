package net.superlucamon.luero.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.superlucamon.luero.server.HeroProvider.Hero;

import java.util.function.Supplier;

// NETWORKING: Used by all ability keys for any hero/ability
public class AbilityKeyPressPacket {
    private final int abilityIndex;
    public AbilityKeyPressPacket(int index) { this.abilityIndex = index; }
    public AbilityKeyPressPacket(FriendlyByteBuf buf) { this.abilityIndex = buf.readInt(); }
    public void encode(FriendlyByteBuf buf) { buf.writeInt(abilityIndex); }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Hero hero = HeroManager.getPlayerHero(player);
                if (hero != null) {
                    hero.activateAbility(abilityIndex, player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
