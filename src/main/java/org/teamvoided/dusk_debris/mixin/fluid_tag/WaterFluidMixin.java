package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(WaterFluid.class)
public class WaterFluidMixin {
    @Redirect(method = "canBeReplacedWith", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/Fluid;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean replacedWith(Fluid instance, TagKey<Fluid> tag) {
        return instance.is(DuskFluidTags.INSTANCE.getWATER_DOES_NOT_REPLACE_BELOW());
    }
}
