package org.teamvoided.dusk_debris.mixin.directional_sculk.spreading;

import net.minecraft.world.gen.feature.SculkPatchFeature;
import net.minecraft.world.gen.feature.SculkPatchFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.block.mixin.SculkDirectionalStuff;

@Debug(export = true)
@Mixin(SculkPatchFeature.class)
public class SculkPatchFeatureMixin {
    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;down()Lnet/minecraft/util/math/BlockPos;", ordinal = 0), cancellable = true)
    private void catalystAndShrieker(FeatureContext<SculkPatchFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        SculkDirectionalStuff.featureCatalystAndShrieker(context);
        cir.setReturnValue(true);
    }
}