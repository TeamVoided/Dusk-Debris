package org.teamvoided.dusk_debris.mixin.fluid_tag.water;

import net.minecraft.world.level.ClipContext;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

@Debug(export = true)
@Mixin(ClipContext.Fluid.class)
public class RaycastContextMixin {
//    @Redirect(method = "predicate", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
//    private static boolean hydrates(FluidState instance, TagKey<Fluid> tag) {
//        return instance.isIn(DuskFluidTags.INSTANCE.getRESETS_FALL_DISTANCE());
//    }
}
