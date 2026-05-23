package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(LiquidBlock.class)
public class LiquidBlockMixin {

    @Redirect(method = "isPathfindable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FlowingFluid;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean pathfindProvider(FlowingFluid instance, TagKey tagKey) {
            return instance.is(DuskFluidTags.INSTANCE.getENTITIES_DONT_PATHFIND_THROUGH());
    }

    @Redirect(method = "shouldSpreadLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FlowingFluid;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean lavaProvider(FlowingFluid instance, TagKey tagKey) {
            return instance.is(DuskFluidTags.INSTANCE.getLAVA_COMPONENT_BASALT_AND_COBBLESTONES());
    }

    @Redirect(method = "shouldSpreadLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean waterProvider(FluidState instance, TagKey<Fluid> tag) {
            return instance.is(DuskFluidTags.INSTANCE.getWATER_COMPONENT_COBBLESTONES());
    }
}
