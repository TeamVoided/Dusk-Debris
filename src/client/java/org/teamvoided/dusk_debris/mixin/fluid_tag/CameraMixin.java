package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.client.Camera;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(Camera.class)
public class CameraMixin {
    @Redirect(method = "getFluidInCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean tagged(FluidState instance, TagKey<Fluid> tag) {
        if (tag == FluidTags.WATER)
            return instance.is(DuskFluidTags.INSTANCE.getENABLES_WATER_FOG());
        else if (tag == FluidTags.LAVA)
            return instance.is(DuskFluidTags.INSTANCE.getENABLES_LAVA_FOG());
        return instance.is(tag);
    }
}
