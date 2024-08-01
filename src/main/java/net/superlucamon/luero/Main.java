package net.superlucamon.luero;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.superlucamon.luero.block.ModBlocks;
import net.superlucamon.luero.block.entity.ModBlockEntities;
import net.superlucamon.luero.block.recipe.ModRecipes;
import net.superlucamon.luero.server.HeroRegistry;
import net.superlucamon.luero.client.command.HeroCommand;
import net.superlucamon.luero.entity.renderer.CustomLineRenderer;
import net.superlucamon.luero.entity.renderer.SentryEntityRenderer;
import net.superlucamon.luero.heros.ironman.abilities.SentryMode;
import net.superlucamon.luero.item.ModCreativeModTabs;
import net.superlucamon.luero.item.ModItems;
import net.superlucamon.luero.networking.ModPackets;
import net.superlucamon.luero.screen.GemPolishingStationScreen;
import net.superlucamon.luero.screen.ModMenuTypes;
import net.superlucamon.luero.test.CustomMissileRenderer;
import net.superlucamon.luero.test.TargetMarkerEntityRenderer;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

import static net.superlucamon.luero.test.CustomEntityRegister.*;

@Mod(Main.MOD_ID)
public class Main
{
    public static final String MOD_ID = "heromod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);

        ModRecipes.register(modEventBus);
        ModCreativeModTabs.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        GeckoLib.initialize();

        ENTITY_TYPES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new SentryMode());
        //CustomEntityRegister.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        HeroCommand.register(event.getDispatcher());
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            HeroRegistry.initializeHeroes();
        });

        ModPackets.register();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void registerRenderers(final FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                EntityRenderers.register(CUSTOM_SMALL_FIREBALL.get(), CustomMissileRenderer::new);
                EntityRenderers.register(TARGET_MARKER.get(), TargetMarkerEntityRenderer::new);
                EntityRenderers.register(CUSTOM_LINE.get(), CustomLineRenderer::new);
                EntityRenderers.register(SENTRY_ENTITY.get(), SentryEntityRenderer::new);
             //  EntityRendererProvider.Context context = Minecraft.getInstance().getEntityRenderDispatcher().();
             //  boolean useSmallArms = false; // Change this if you want small arms model
             //  Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().put("default", new CustomLineRenderer(context, useSmallArms));
             //  Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().put("slim", new CustomLineRenderer(context, true));
            });

        }
        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.AddLayers event) {
            //Obtain the renderer context
          // EntityRendererProvider.Context context = event.getContext();

          // boolean useSmallArms = false; // Change this if you want small arms model
          // Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().put("default", new CustomLineRenderer(context, useSmallArms));
          // Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().put("slim", new CustomLineRenderer(context, true));
        }
       /* @SubscribeEvent
        public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(CustomRocketModel.LAYER_LOCATION, CustomRocketModel::createBodyLayer);
        }
        */


        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRendererProvider.Context rendererContext = new EntityRendererProvider.Context(
                    Minecraft.getInstance().getEntityRenderDispatcher(),
                    Minecraft.getInstance().getItemRenderer(),
                    Minecraft.getInstance().getBlockRenderer(),
                    Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer(),
                    Minecraft.getInstance().getResourceManager(),
                    Minecraft.getInstance().getEntityModels(),
                    Minecraft.getInstance().font
            );
            MenuScreens.register(ModMenuTypes.GEM_POLISHING_MENU.get(), GemPolishingStationScreen::new);

        }

    }
}
