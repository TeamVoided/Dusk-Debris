package org.teamvoided.dusk_debris.mixin.fluid_tag;


import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
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
@Mixin(FluidBlock.class)
public class FluidBlockMixin {

    @Redirect(method = "canPathfindThrough", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FlowableFluid;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean pathfindProvider(FlowableFluid instance, TagKey tagKey) {
            return instance.isIn(DuskFluidTags.INSTANCE.getENTITIES_DONT_PATHFIND_THROUGH());
    }

    @Redirect(method = "receiveNeighborFluids", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FlowableFluid;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean lavaProvider(FlowableFluid instance, TagKey tagKey) {
            return instance.isIn(DuskFluidTags.INSTANCE.getLAVA_COMPONENT_BASALT_AND_COBBLESTONES());
    }

    @Redirect(method = "receiveNeighborFluids", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean waterProvider(FluidState instance, TagKey<Fluid> tag) {
            return instance.isIn(DuskFluidTags.INSTANCE.getWATER_COMPONENT_COBBLESTONES());
    }
}
