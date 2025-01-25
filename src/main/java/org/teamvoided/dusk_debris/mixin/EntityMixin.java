package org.teamvoided.dusk_debris.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract void addVelocity(Vec3d delta);

    @Unique
    private Vec3d velocityWind;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickWind(CallbackInfo ci) {
        if (velocityWind != Vec3d.ZERO) {
            this.addVelocity(velocityWind);
            velocityWind = Vec3d.ZERO;
        }
    }
}
