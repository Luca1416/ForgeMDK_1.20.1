package net.superlucamon.luero.item;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ModArmorMaterials3DModels implements ArmorMaterial {
    IRONMAN_TESTMAT("ironman_testmat", 25, Util.make(new EnumMap<>(ArmorItem.Type.class), (ironmanTest) -> {
        ironmanTest.put(ArmorItem.Type.BOOTS, 3);
        ironmanTest.put(ArmorItem.Type.LEGGINGS, 6);
        ironmanTest.put(ArmorItem.Type.CHESTPLATE, 8);
        ironmanTest.put(ArmorItem.Type.HELMET, 3);
    }), 19, SoundEvents.ARMOR_EQUIP_IRON, 3.0F, 0.1F, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    });

    public static final StringRepresentable.EnumCodec<ArmorMaterials> CODEC = StringRepresentable.fromEnum(ArmorMaterials::values);
    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (armor) -> {
        armor.put(ArmorItem.Type.BOOTS, 13);
        armor.put(ArmorItem.Type.LEGGINGS, 15);
        armor.put(ArmorItem.Type.CHESTPLATE, 16);
        armor.put(ArmorItem.Type.HELMET, 11);
    });
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModArmorMaterials3DModels(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionFunctionForType, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionFunctionForType = protectionFunctionForType;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    public int getDurabilityForType(ArmorItem.Type pArmorType) {
        return HEALTH_FUNCTION_FOR_TYPE.get(pArmorType) * this.durabilityMultiplier;
    }

    public int getDefenseForType(ArmorItem.Type pArmorType) {
        return this.protectionFunctionForType.get(pArmorType);
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public String getSerializedName() {
        return this.name;
    }
}

