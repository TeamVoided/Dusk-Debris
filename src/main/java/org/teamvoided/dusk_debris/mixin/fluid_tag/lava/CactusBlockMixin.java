package org.teamvoided.dusk_debris.mixin.fluid_tag.lava;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(CactusBlock.class)
public class CactusBlockMixin {
    @Redirect(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean hydrates(FluidState instance, TagKey<Fluid> tag) {
        return instance.is(DuskFluidTags.INSTANCE.getBREAKS_CACTUS());
    }
}
