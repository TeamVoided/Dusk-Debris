package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(LavaFluid.class)
public class LavaFluidMixin {
    @Redirect(method = "flow", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/LavaFluid;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean flow(LavaFluid instance, TagKey tagKey) {
        return instance.isIn(DuskFluidTags.INSTANCE.getLAVA_COMPONENT_STONE());
    }
    @Redirect(method = "flow", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean flow(FluidState instance, TagKey<Fluid> tag) {
         return instance.isIn(DuskFluidTags.INSTANCE.getWATER_COMPONENT_STONE());
    }

    @Redirect(method = "canBeReplacedWith", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/Fluid;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean replacedWith(Fluid instance, TagKey<Fluid> tag) {
       return instance.isIn(DuskFluidTags.INSTANCE.getREPLACES_LAVA());
    }
}
