package org.teamvoided.dusk_debris.mixin.fluid_tag;

import net.minecraft.entity.Entity;
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
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Redirect(method = "updateSwimming", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean isSwimmable(FluidState instance, TagKey<Fluid> tag) {
         return instance.isIn(DuskFluidTags.INSTANCE.getENTITY_WATER_MOVEMENT());
    }
    @Redirect(method = "checkWaterState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;updateMovementInFluid(Lnet/minecraft/registry/tag/TagKey;D)Z"))
    private boolean isSwimmable(Entity instance, TagKey<Fluid> key, double velocity) {
        return instance.updateMovementInFluid(DuskFluidTags.INSTANCE.getENTITY_WATER_MOVEMENT(), velocity);
    }
    @Redirect(method = "updateSubmergedInWaterState", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean isSwimmable(Entity instance, TagKey<Fluid> key) {
        return instance.isSubmergedIn(DuskFluidTags.INSTANCE.getENTITY_WATER_MOVEMENT());
    }
}
