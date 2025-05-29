package net.superlucamon.luero.entity.renderer;

import net.minecraft.world.phys.Vec3;

public class BeamRenderInfo {
    public Vec3 prevEyePos;
    public Vec3 currentEyePos;

    public Vec3 prevLookVec;
    public Vec3 currentLookVec;

    public void update(Vec3 eyePos, Vec3 lookVec) {
        this.prevEyePos = this.currentEyePos != null ? this.currentEyePos : eyePos;
        this.prevLookVec = this.currentLookVec != null ? this.currentLookVec : lookVec;
        this.currentEyePos = eyePos;
        this.currentLookVec = lookVec;
    }

    public Vec3 getInterpolatedEyePos(float partialTicks) {
        return interpolate(prevEyePos, currentEyePos, partialTicks);
    }

    public Vec3 getInterpolatedLookVec(float partialTicks) {
        return interpolate(prevLookVec, currentLookVec, partialTicks);
    }

    private Vec3 interpolate(Vec3 from, Vec3 to, float alpha) {
        if (from == null || to == null) return Vec3.ZERO;
        return from.lerp(to, alpha);
    }
}
