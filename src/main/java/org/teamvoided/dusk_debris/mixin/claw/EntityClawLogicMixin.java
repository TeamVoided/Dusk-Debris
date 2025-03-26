package org.teamvoided.dusk_debris.mixin.claw;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Holder;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.entity.helper.ClawLogic;
import org.teamvoided.dusk_debris.entity.helper.DuskClawStuff;

@Mixin(LivingEntity.class)
public abstract class EntityClawLogicMixin implements DuskClawStuff {

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getGravity()D"))
    public double travelClawed(LivingEntity entity) {
        return ClawLogic.slideDown(entity);
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/Holder;)Z"))
    public boolean shouldResetFallDistanceAndWallCheck(LivingEntity entity, Holder<StatusEffect> effect) {
        if (isHanging() || canHang()) {
            if (!entity.isOnGround() && entity.getVelocity().y < 0) {
                ClawLogic.checkIfOnWall(entity);
            } else {
                DuskDebris$onWallDirection = Vec3d.ZERO;
            }
        }
        return entity.hasStatusEffect(effect) || isHanging();
    }

//    @Inject(method = "tickMovement", at = @At("HEAD"))
//    public void tickClawLogics(CallbackInfo ci) {
//        if (true){
//        ClawLogic.INSTANCE.checkIfOnWall(instance);
//    }
//    }

    @Unique
    public boolean isHanging() {
        return DuskDebris$onWallDirection != Vec3d.ZERO;
    }

    @Unique
    public boolean canHang() {
        return true;
    }

    @Unique
    public Vec3d DuskDebris$onWallDirection = Vec3d.ZERO;
    //@Unique
    //public Boolean DuskDebris$onWall = false;


    @NotNull
    @Override
    public Vec3d getHangingDirection() {
        return DuskDebris$onWallDirection;
    }

    @Override
    public void setHangingDirection(@NotNull Vec3d direction) {
        DuskDebris$onWallDirection = direction;
    }
    //@NotNull
    //@Override
    //public boolean getHanging() {
    //    return DuskDebris$onWall;
    //}

    //@Override
    //public void setHanging(@NotNull Boolean hanging) {
    //    DuskDebris$onWall = hanging;
    //}
}
