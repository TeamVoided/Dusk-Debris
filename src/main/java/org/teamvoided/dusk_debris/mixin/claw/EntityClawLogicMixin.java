package org.teamvoided.dusk_debris.mixin.claw;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.entity.helper.ClawLogic;
import org.teamvoided.dusk_debris.entity.helper.DuskClawStuff;

@Mixin(LivingEntity.class)
public abstract class EntityClawLogicMixin implements DuskClawStuff {

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getGravity()D"))
    public double travelClawed(LivingEntity entity) {
        return ClawLogic.slideDown(entity);
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"))
    public boolean shouldResetFallDistanceAndWallCheck(LivingEntity entity, Holder<MobEffect> effect) {
        if (canHang() && !entity.onGround() && entity.getDeltaMovement().y < 0) {
            ClawLogic.checkIfOnWall(entity);
            ClawLogic.particles(entity);
            if (DuskDebris$onWall) return true;
        } else {
            DuskDebris$onWall = false;
            DuskDebris$onWallDirection = Vec3.ZERO;
        }
        return entity.hasEffect(effect);
    }

//    @Inject(method = "tickMovement", at = @At("HEAD"))
//    public void tickClawLogics(CallbackInfo ci) {
//        if (true){
//          ClawLogic.INSTANCE.checkIfOnWall(instance);
//      }
//    }

    @Unique
    public boolean canHang() {
        return false;
    }

    @Unique
    public Vec3 DuskDebris$onWallDirection = Vec3.ZERO;
    @Unique
    public boolean DuskDebris$onWall = false;


    @NotNull
    @Override
    public Vec3 getHangingDirection() {
        return DuskDebris$onWallDirection;
    }

    @Override
    public void setHangingDirection(@NotNull Vec3 direction) {
        DuskDebris$onWallDirection = direction;
    }

    @NotNull
    @Override
    public boolean getHanging() {
        return DuskDebris$onWall;
    }

    @Override
    public void setHanging(@NotNull boolean hanging) {
        DuskDebris$onWall = hanging;
    }
}
