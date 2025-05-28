package net.superlucamon.luero.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.superlucamon.luero.Main;
import net.superlucamon.luero.item.custom.BeamItemTestv2;
import net.superlucamon.luero.item.custom.IronmanMark1TestArmorItem;
import net.superlucamon.luero.item.custom.ReactorTest;
import net.superlucamon.luero.item.shitToFix.BeamItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);

    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire",
            () -> new BeamItem(new Item.Properties()));
    public static final RegistryObject<Item> BEAMITEM_TEST = ITEMS.register("test",
            () -> new BeamItem(new Item.Properties()));
    public static final RegistryObject<Item> UNIBEAMITEM_TEST = ITEMS.register("unibeam_test",
            () -> new BeamItemTestv2(new Item.Properties()));
    public static final RegistryObject<Item> RAW_SAPPHIRE = ITEMS.register("raw_sapphire",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REACTOR_TEST1 = ITEMS.register("reactor_test1",
            () -> new ReactorTest(new Item.Properties()));
    public static final RegistryObject<Item> STRAWBERRY = ITEMS.register("strawberry",
            () -> new Item(new Item.Properties().food(ModFoods.STRAWBERRY)));
    public static final RegistryObject<Item> IRONMAN_HELMET_MK47 = ITEMS.register("ironman_helmet_mk47",
            () -> new IronmanMark1TestArmorItem(ModArmorMaterials3DModels.IRONMAN_TESTMAT, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> IRONMAN_CHESTPLATE_MK47 = ITEMS.register("ironman_chestplate_mk47",
            () -> new IronmanMark1TestArmorItem(ModArmorMaterials3DModels.IRONMAN_TESTMAT, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> IRONMAN_LEGGINGS_MK47 = ITEMS.register("ironman_leggings_mk47",
            () -> new IronmanMark1TestArmorItem(ModArmorMaterials3DModels.IRONMAN_TESTMAT, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> IRONMAN_BOOTS_MK47 = ITEMS.register("ironman_boots_mk47",
            () -> new IronmanMark1TestArmorItem(ModArmorMaterials3DModels.IRONMAN_TESTMAT, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
