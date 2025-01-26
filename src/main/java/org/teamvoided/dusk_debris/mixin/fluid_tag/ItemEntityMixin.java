package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.entity.ItemEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;getFluidHeight(Lnet/minecraft/registry/tag/TagKey;)D"))
    private double fluidTag(ItemEntity instance, TagKey tagKey) {
        if (tagKey == FluidTags.WATER)
            return instance.getFluidHeight(DuskFluidTags.INSTANCE.getITEMS_AND_EXPERIENCE_ORBS_FLOAT_WATER());
        else if (tagKey == FluidTags.LAVA)
            return instance.getFluidHeight(DuskFluidTags.INSTANCE.getITEMS_AND_EXPERIENCE_ORBS_FLOAT_LAVA());
        else return instance.getFluidHeight(tagKey);
    }

//    @Redirect(method = "getPositionType", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
//    private boolean isInFluid() {
//        return instance.isIn(DuskFluidTags.INSTANCE.getFISHING_BOBBER_BOBS());
//    }
}
