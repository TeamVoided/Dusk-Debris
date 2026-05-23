package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(LavaFluid.class)
public class LavaFluidMixin {
    @Redirect(method = "spreadTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/LavaFluid;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean flow(LavaFluid instance, TagKey tagKey) {
        return instance.is(DuskFluidTags.INSTANCE.getLAVA_COMPONENT_STONE());
    }
    @Redirect(method = "spreadTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean flow(FluidState instance, TagKey<Fluid> tag) {
         return instance.is(DuskFluidTags.INSTANCE.getWATER_COMPONENT_STONE());
    }

    @Redirect(method = "canBeReplacedWith", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/Fluid;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean replacedWith(Fluid instance, TagKey<Fluid> tag) {
       return instance.is(DuskFluidTags.INSTANCE.getREPLACES_LAVA());
    }
}
