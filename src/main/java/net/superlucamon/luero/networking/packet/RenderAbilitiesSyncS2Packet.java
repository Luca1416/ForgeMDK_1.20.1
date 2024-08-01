package net.superlucamon.luero.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.superlucamon.luero.client.ClientHeroData;

import java.util.function.Supplier;

public class RenderAbilitiesSyncS2Packet {
    private final String hero;
    public RenderAbilitiesSyncS2Packet(String hero) {
        this.hero = hero;
    }

    public RenderAbilitiesSyncS2Packet(FriendlyByteBuf buf) {
        this.hero = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(hero);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientHeroData.set(hero);
        });
        return true;
    }
}
