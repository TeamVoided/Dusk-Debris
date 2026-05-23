package org.teamvoided.dusk_debris.mixin.fluid_tag.water;


import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(FishingHook.class)
public class FishingHookMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean fluidTag(FluidState instance, TagKey<Fluid> tag) {
        return instance.is(DuskFluidTags.INSTANCE.getFISHING_BOBBER_BOBS());
    }

//    @Redirect(method = "getPositionType", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
//    private boolean isInFluid() {
//        return instance.isIn(DuskFluidTags.INSTANCE.getFISHING_BOBBER_BOBS());
//    }
}
