package org.teamvoided.dusk_debris.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
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

    @Shadow public abstract void push(Vec3 delta);

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickWind(CallbackInfo ci) {
        if (DuskDebris$velocityWind != Vec3.ZERO) {
            this.push(DuskDebris$velocityWind);
            DuskDebris$velocityWind = Vec3.ZERO;
        }
    }

    @Unique
    public Vec3 DuskDebris$velocityWind = Vec3.ZERO;


    @NotNull
    @Override
    public Vec3 getWind() {
        return DuskDebris$velocityWind;
    }

    @Override
    public void setWind(@NotNull Vec3 wind) {
        DuskDebris$velocityWind = wind;
    }
}
