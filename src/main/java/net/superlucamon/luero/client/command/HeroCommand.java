package net.superlucamon.luero.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.superlucamon.luero.client.CSHeroProvider.Hero;
import net.superlucamon.luero.client.HeroManager;
import net.superlucamon.luero.client.HeroRegistry;

public class HeroCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sethero")
                .then(Commands.argument("hero", StringArgumentType.string())
                        .executes(context -> {
                            String heroName = StringArgumentType.getString(context, "hero");
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Hero hero = HeroRegistry.getHeroByName(heroName);
                            if (hero != null) {
                                HeroManager.setPlayerHero(player, hero);
                                context.getSource().sendSuccess(() -> Component.literal("Set hero to " + heroName), true);
                                return 1;
                            } else {
                                context.getSource().sendFailure(Component.literal("Hero not found"));
                                return 0;
                            }
                        })));
        dispatcher.register(Commands.literal("getHero")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Hero hero = HeroManager.getPlayerHero(player);
                           // HeroManager.setPlayerHero(player, hero);
                            if (hero != null) {
                                HeroManager.updatePlayerAbilities(player, hero);
                                context.getSource().sendSuccess(() -> Component.literal("you are: " + hero.getName()), true);
                                return 1;
                            } else {
                                context.getSource().sendFailure(Component.literal("Hero not found"));
                                return 0;
                            }
                        }));
    }
}
