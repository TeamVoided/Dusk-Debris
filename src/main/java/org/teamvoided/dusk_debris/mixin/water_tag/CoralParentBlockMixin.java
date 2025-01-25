package org.teamvoided.dusk_debris.mixin.water_tag;


import net.minecraft.block.CoralParentBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CoralParentBlock.class)
public class CoralParentBlockMixin {


//    @Redirect(method = "isInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Holder;isRegistryKey(Lnet/minecraft/registry/RegistryKey;)Z"))
//    private boolean coralHydrates() {
//        if (waterTag = FluidTags.WATER) return fluid.isIn(DuskFluidTags.CORAL_HYDRATOR);
//    }
}
