package net.superlucamon.luero.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.server.level.ServerPlayer;
import net.superlucamon.luero.server.HeroManager;
import net.superlucamon.luero.server.HeroProvider.Hero;

import java.util.function.Supplier;

public class AbilityKeyPressPacket {
    private final int abilityIndex;

    public AbilityKeyPressPacket(int abilityIndex) {
        this.abilityIndex = abilityIndex;
    }

    public AbilityKeyPressPacket(FriendlyByteBuf buf) {
        this.abilityIndex = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(abilityIndex);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Hero hero = HeroManager.getPlayerHero(player);
                if (hero != null) {
                    hero.activateAbility(abilityIndex, player); // See below!
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
