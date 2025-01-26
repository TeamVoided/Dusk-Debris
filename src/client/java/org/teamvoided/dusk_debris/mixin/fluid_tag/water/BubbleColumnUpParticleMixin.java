package org.teamvoided.dusk_debris.mixin.fluid_tag.water;


import net.minecraft.client.particle.BubbleColumnUpParticle;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(BubbleColumnUpParticle.class)
public class BubbleColumnUpParticleMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean hydrates(FluidState instance, TagKey<Fluid> tag) {
        return instance.isIn(DuskFluidTags.INSTANCE.getWATER_BUBBLE_PARTICLE_PERSISTS());
    }
}
