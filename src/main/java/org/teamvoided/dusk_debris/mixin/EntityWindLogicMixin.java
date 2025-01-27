package org.teamvoided.dusk_debris.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.dusk_debris.entity.helper.DuskVelocityWind;

@Mixin(Entity.class)
public abstract class EntityWindLogicMixin implements DuskVelocityWind {

    @Shadow public abstract void addVelocity(Vec3d delta);

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickWind(CallbackInfo ci) {
        if (DuskDebris$velocityWind != Vec3d.ZERO) {
            this.addVelocity(DuskDebris$velocityWind);
            DuskDebris$velocityWind = Vec3d.ZERO;
        }
    }

    @Unique
    public Vec3d DuskDebris$velocityWind = Vec3d.ZERO;


    @NotNull
    @Override
    public Vec3d getWind() {
        return DuskDebris$velocityWind;
    }

    @Override
    public void setWind(@NotNull Vec3d wind) {
        DuskDebris$velocityWind = wind;
    }
}
