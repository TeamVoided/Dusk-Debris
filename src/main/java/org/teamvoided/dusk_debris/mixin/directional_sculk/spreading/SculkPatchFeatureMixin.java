package org.teamvoided.dusk_debris.mixin.directional_sculk.spreading;

import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SculkPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.block.mixin.SculkDirectionalStuff;

@Debug(export = true)
@Mixin(SculkPatchFeature.class)
public class SculkPatchFeatureMixin {
    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;below()Lnet/minecraft/core/BlockPos;", ordinal = 0), cancellable = true)
    private void catalystAndShrieker(FeaturePlaceContext<SculkPatchConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        SculkDirectionalStuff.featureCatalystAndShrieker(context);
        cir.setReturnValue(true);
    }
}