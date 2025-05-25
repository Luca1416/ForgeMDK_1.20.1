package net.superlucamon.luero.item.custom;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.superlucamon.luero.item.ModItems;

public class ReactorTest extends Item {
    public ReactorTest(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
       // Hero hero = HeroRegistry.getHeroByName("IronMan");
       // ClientHeroData.set("IronMan");
        //HeroManager.setPlayerHero(player, hero);
       // HeroManager.updatePlayerAbilities(player, hero);
        if (player.getItemBySlot(EquipmentSlot.HEAD) == ItemStack.EMPTY) {
            player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.IRONMAN_HELMET_MK47.get()));
        }
        if (player.getItemBySlot(EquipmentSlot.CHEST) == ItemStack.EMPTY) {
            player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ModItems.IRONMAN_CHESTPLATE_MK47.get()));
        }
        if (player.getItemBySlot(EquipmentSlot.LEGS) == ItemStack.EMPTY) {
            player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.IRONMAN_LEGGINGS_MK47.get()));
        }
        if (player.getItemBySlot(EquipmentSlot.FEET) == ItemStack.EMPTY) {
            player.setItemSlot(EquipmentSlot.FEET, new ItemStack(ModItems.IRONMAN_BOOTS_MK47.get()));
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        itemStack.setCount(0);
        return super.use(level, player, hand);
    }
}
