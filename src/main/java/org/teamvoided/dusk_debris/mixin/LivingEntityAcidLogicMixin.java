package org.teamvoided.dusk_debris.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAcidLogicMixin extends Entity {

    public LivingEntityAcidLogicMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public boolean hurtByWater() {
        return false;
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void tickMovementAcid(CallbackInfo ci) {
        if (!this.getWorld().isClient && this.hurtByWater() && this.isWet()) {
            this.damage(this.getDamageSources().drown(), 1.0F);
        }
    }

}
