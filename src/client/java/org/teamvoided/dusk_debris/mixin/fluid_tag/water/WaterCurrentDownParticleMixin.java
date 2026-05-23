package org.teamvoided.dusk_debris.mixin.fluid_tag.water;


import net.minecraft.client.particle.WaterCurrentDownParticle;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(WaterCurrentDownParticle.class)
public class WaterCurrentDownParticleMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean hydrates(FluidState instance, TagKey<Fluid> tag) {
        return instance.is(DuskFluidTags.INSTANCE.getWATER_BUBBLE_PARTICLE_PERSISTS());
    }
}
