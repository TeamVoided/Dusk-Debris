package org.teamvoided.dusk_debris.mixin.fluid_tag.lava;

import net.minecraft.block.CactusBlock;
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
@Mixin(CactusBlock.class)
public class CactusBlockMixin {
    @Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean hydrates(FluidState instance, TagKey<Fluid> tag) {
        return instance.isIn(DuskFluidTags.INSTANCE.getBREAKS_CACTUS());
    }
}
