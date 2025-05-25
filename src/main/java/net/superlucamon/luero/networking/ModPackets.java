package net.superlucamon.luero.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.heros.keymanagement.AbilityKeyPressPacket;
import net.superlucamon.luero.networking.packet.ExampleC2SPacket;
import net.superlucamon.luero.networking.packet.RenderAbilitiesSyncS2Packet;

public class ModPackets {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Main.MOD_ID, "packets"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;


        net.messageBuilder(ExampleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ExampleC2SPacket::new)
                .encoder(ExampleC2SPacket::toBytes)
                .consumerMainThread(ExampleC2SPacket::handle)
                .add();
        net.messageBuilder(RenderAbilitiesSyncS2Packet.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RenderAbilitiesSyncS2Packet::new)
                .encoder(RenderAbilitiesSyncS2Packet::toBytes)
                .consumerMainThread(RenderAbilitiesSyncS2Packet::handle)
                .add();
        net.registerMessage(packetId++,
                AbilityKeyPressPacket.class,
                AbilityKeyPressPacket::encode,
                AbilityKeyPressPacket::decode,
                AbilityKeyPressPacket::handle);
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
