package org.teamvoided.dusk_debris.mixin.fluid_tag.water;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean baseTickTag(LivingEntity instance, TagKey tagKey) {
        return instance.isSubmergedIn(DuskFluidTags.INSTANCE.getENTITIES_DROWN_IN());
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;swimUpward(Lnet/minecraft/registry/tag/TagKey;)V"))
    private void tickMovementTag(LivingEntity instance, TagKey tagKey) {
        if (tagKey == FluidTags.WATER) instance.swimUpward(DuskFluidTags.INSTANCE.getENTITY_SWIMABLE());
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getFluidHeight(Lnet/minecraft/registry/tag/TagKey;)D"))
    private double tickMovementHeightTag(LivingEntity instance, TagKey tagKey) {
        if (tagKey == FluidTags.WATER) return instance.getFluidHeight(DuskFluidTags.INSTANCE.getENTITY_SWIMABLE());
        return 0;
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private void baseTickDrowningParticle(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
//        var particle = parameters;
//        if (this.isSubmergedIn(DuskFluidTags.INSTANCE.getACID_BUBBLE_PARTICLES()))
//            particle = DuskParticles.INSTANCE.getACID_BUBBLE()
//        instance.addParticle(particle, x, y, z, velocityX, velocityY, velocityZ);

        instance.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
    }
}