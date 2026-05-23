package org.teamvoided.dusk_debris.mixin.fluid_tag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Redirect(method = "updateSwimming", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean isSwimmable(FluidState instance, TagKey<Fluid> tag) {
         return instance.is(DuskFluidTags.INSTANCE.getENTITY_WATER_MOVEMENT());
    }
    @Redirect(method = "updateInWaterStateAndDoWaterCurrentPushing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;updateFluidHeightAndDoFluidPushing(Lnet/minecraft/tags/TagKey;D)Z"))
    private boolean isSwimmable(Entity instance, TagKey<Fluid> key, double velocity) {
        return instance.updateFluidHeightAndDoFluidPushing(DuskFluidTags.INSTANCE.getENTITY_WATER_MOVEMENT(), velocity);
    }
    @Redirect(method = "updateFluidOnEyes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean isSwimmable(Entity instance, TagKey<Fluid> key) {
        return instance.isEyeInFluid(DuskFluidTags.INSTANCE.getENTITY_WATER_MOVEMENT());
    }
}
