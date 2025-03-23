package org.teamvoided.dusk_debris.mixin.fluid_tag.water;


import net.minecraft.block.dispenser.DispenserBehavior;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

@Debug(export = true)
@Mixin(DispenserBehavior.class)
public class DispenserBehaviorMixin {
//    @Redirect(method = "dispenseSilently", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
//    private boolean hydrates(FluidState instance, TagKey<Fluid> tag) {
//        return instance.isIn(DuskFluidTags.INSTANCE.getGLASS_BOTTLES_FILL_WATER());
//    }
}
