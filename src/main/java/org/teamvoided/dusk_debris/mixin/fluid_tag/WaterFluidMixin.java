package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(WaterFluid.class)
public class WaterFluidMixin {
    @Redirect(method = "canBeReplacedWith", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/Fluid;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean replacedWith(Fluid instance, TagKey<Fluid> tag) {
        return instance.isIn(DuskFluidTags.INSTANCE.getWATER_DOES_NOT_REPLACE_BELOW());
    }
}
