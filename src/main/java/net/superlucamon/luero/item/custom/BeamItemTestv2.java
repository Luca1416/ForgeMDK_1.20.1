package net.superlucamon.luero.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.superlucamon.luero.entity.projectile.UniBeamEntity;
import net.superlucamon.luero.test.CustomEntityRegister;

public class BeamItemTestv2 extends Item{
    public BeamItemTestv2(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        Vec3 look = player.getLookAngle(); // normalized direction
        Vec3 origin = player.position().add(0, 1.3, 0); // chest height

        UniBeamEntity beam = new UniBeamEntity(CustomEntityRegister.CUSTOM_BEAM.get(), level);
        beam.setPlayer(player);
        beam.setPos(origin.x, origin.y, origin.z);
        beam.setDeltaMovement(look.scale(1)); // optional
        beam.setYRot((float) (Math.toDegrees(Math.atan2(look.z, look.x)) - 90));
        beam.setXRot((float) (-Math.toDegrees(Math.asin(look.y))));
        level.addFreshEntity(beam);

        return super.use(level, player, hand);
    }
}
