package net.superlucamon.luero.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.superlucamon.luero.networking.ModPackets;
import net.superlucamon.luero.networking.packet.RenderAbilitiesSyncS2Packet;
import net.superlucamon.luero.server.HeroProvider.Hero;
import net.superlucamon.luero.server.HeroManager;
import net.superlucamon.luero.server.HeroRegistry;

public class HeroCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sethero")
                .then(Commands.argument("hero", StringArgumentType.string())
                        .executes(context -> {
                            String heroName = StringArgumentType.getString(context, "hero");
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Hero hero = HeroRegistry.getHeroByName(heroName);
                            if (hero != null) {
                                ModPackets.sendToPlayer(new RenderAbilitiesSyncS2Packet(hero.getName()), player);
                                HeroManager.setPlayerHero(player, hero);
                                context.getSource().sendSuccess(() -> Component.literal("Set hero to " + heroName), true);
                                return 1;
                            } else {
                                context.getSource().sendFailure(Component.literal("Hero not found"));
                                return 0;
                            }
                        })));
        dispatcher.register(Commands.literal("setHeroNull")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Hero hero = HeroManager.getPlayerHero(player);
                            if (hero != null) {
                                ModPackets.sendToPlayer(new RenderAbilitiesSyncS2Packet("null"), player);
                                HeroManager.setPlayerHero(player, null);
                                context.getSource().sendSuccess(() -> Component.literal("Hero removed"), true);
                                return 1;
                            } else {
                                context.getSource().sendFailure(Component.literal("Could not remove hero Because you are not a Hero"));
                                return 0;
                            }
                        }));

        dispatcher.register(Commands.literal("getHero")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Hero hero = HeroManager.getPlayerHero(player);
                            if (hero != null) {
                                ModPackets.sendToPlayer(new RenderAbilitiesSyncS2Packet(hero.getName()), player);
                                HeroManager.updatePlayerAbilities(player, hero);
                                return 1;
                            } else {
                                context.getSource().sendFailure(Component.literal("Hero not found"));
                                return 0;
                            }
                        }));
    }
}
