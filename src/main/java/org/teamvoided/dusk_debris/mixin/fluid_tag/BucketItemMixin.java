package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(BucketItem.class)
public class BucketItemMixin {
    @Redirect(method = "emptyContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/Fluid;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean hydrates(Fluid instance, TagKey<Fluid> tag) {
        if (tag == FluidTags.WATER)return instance.is(DuskFluidTags.INSTANCE.getULTRAWARM_BUCKET_RESTRICTION());
        else return instance.is(tag);
    }
}
