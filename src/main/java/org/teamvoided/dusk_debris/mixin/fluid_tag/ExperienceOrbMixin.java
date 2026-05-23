package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean fluidTag(ExperienceOrb instance, TagKey tagKey) {
        return instance.isEyeInFluid(DuskFluidTags.INSTANCE.getITEMS_AND_EXPERIENCE_ORBS_WATER_LOGIC());
    }
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean fluidTag(FluidState instance, TagKey<Fluid> tag) {
        return instance.is(DuskFluidTags.INSTANCE.getITEMS_AND_EXPERIENCE_ORBS_LAVA_LOGIC());
    }
}
