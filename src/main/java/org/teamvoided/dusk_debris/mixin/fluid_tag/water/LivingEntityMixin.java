package org.teamvoided.dusk_debris.mixin.fluid_tag.water;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean baseTickTag(LivingEntity instance, TagKey tagKey) {
        return instance.isEyeInFluid(DuskFluidTags.INSTANCE.getENTITIES_DROWN_IN());
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;jumpInLiquid(Lnet/minecraft/tags/TagKey;)V"))
    private void tickMovementTag(LivingEntity instance, TagKey tagKey) {
        if (tagKey == FluidTags.WATER) instance.jumpInLiquid(DuskFluidTags.INSTANCE.getENTITY_SWIMABLE());
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getFluidHeight(Lnet/minecraft/tags/TagKey;)D"))
    private double tickMovementHeightTag(LivingEntity instance, TagKey tagKey) {
        if (tagKey == FluidTags.WATER) return instance.getFluidHeight(DuskFluidTags.INSTANCE.getENTITY_SWIMABLE());
        return 0;
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void baseTickDrowningParticle(Level instance, ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
//        var particle = parameters;
//        if (this.isSubmergedIn(DuskFluidTags.INSTANCE.getACID_BUBBLE_PARTICLES()))
//            particle = DuskParticles.INSTANCE.getACID_BUBBLE()
//        instance.addParticle(particle, x, y, z, velocityX, velocityY, velocityZ);

        instance.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
    }
}