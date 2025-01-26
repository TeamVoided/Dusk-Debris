package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.client.render.Camera;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(Camera.class)
public class CameraMixin {
    @Redirect(method = "getSubmersionType", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean tagged(FluidState instance, TagKey<Fluid> tag) {
        if (tag == FluidTags.WATER)
            return instance.isIn(DuskFluidTags.INSTANCE.getENABLES_WATER_FOG());
        else if (tag == FluidTags.LAVA)
            return instance.isIn(DuskFluidTags.INSTANCE.getENABLES_LAVA_FOG());
        return instance.isIn(tag);
    }
}
