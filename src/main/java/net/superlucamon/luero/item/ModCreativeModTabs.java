package net.superlucamon.luero.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.block.ModBlocks;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("hero_test_tap",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SAPPHIRE.get()))
                    .title(Component.translatable("creativetab.hero_mod"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.SAPPHIRE.get());
                        pOutput.accept(ModItems.RAW_SAPPHIRE.get());
                        pOutput.accept(ModItems.BEAMITEM_TEST.get());
                        pOutput.accept(ModItems.REACTOR_TEST1.get());
                        pOutput.accept(ModItems.UNIBEAMITEM_TEST.get());


                        pOutput.accept(Items.DIAMOND);

                        pOutput.accept(ModItems.STRAWBERRY.get());

                        pOutput.accept(ModBlocks.SAPPHIRE_BLOCK.get());
                        pOutput.accept(ModBlocks.RAW_SAPPHIRE_BLOCK.get());

                        pOutput.accept(ModBlocks.SAPPHIRE_ORE.get());

                        pOutput.accept(ModBlocks.SOUND_BLOCK.get());
                        pOutput.accept(ModBlocks.GEM_POLISHING_STATION.get());

                        pOutput.accept(ModItems.IRONMAN_HELMET_MK47.get());
                        pOutput.accept(ModItems.IRONMAN_CHESTPLATE_MK47.get());
                        pOutput.accept(ModItems.IRONMAN_LEGGINGS_MK47.get());
                        pOutput.accept(ModItems.IRONMAN_BOOTS_MK47.get());

                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}