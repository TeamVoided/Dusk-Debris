package org.teamvoided.dusk_debris.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAcidLogicMixin extends Entity {

    public LivingEntityAcidLogicMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    public boolean isSensitiveToWater() {
        return false;
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    public void tickMovementAcid(CallbackInfo ci) {
        if (!this.level().isClientSide && this.isSensitiveToWater() && this.isInWaterRainOrBubble()) {
            this.hurt(this.damageSources().drown(), 1.0F);
        }
    }

}
