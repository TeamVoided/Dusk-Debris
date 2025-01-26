package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(BucketItem.class)
public class BucketItemMixin {
    @Redirect(method = "placeFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/Fluid;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean hydrates(Fluid instance, TagKey<Fluid> tag) {
        if (tag == FluidTags.WATER)return instance.isIn(DuskFluidTags.INSTANCE.getULTRAWARM_BUCKET_RESTRICTION());
        else return instance.isIn(tag);
    }
}
