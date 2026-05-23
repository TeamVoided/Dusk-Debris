package org.teamvoided.dusk_debris.mixin.snow;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomeIceSnowMixin {
    @Shadow
    @Final
    private Biome.ClimateSettings climateSettings;

    @Redirect(method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z", at = @At("RETURN"))
    public void canSetIceAbove(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) LevelReader world, @Local(argsOnly = true) BlockPos pos) {
        if (cir.getReturnValue() && climateSettings.temperatureModifier != Biome.TemperatureModifier.FROZEN) {
            if (world.getFluidState(pos.below()).is(FluidTags.WATER) && world.getFluidState(pos.below(2)).is(FluidTags.WATER)) {

                cir.setReturnValue(false);
            }
        }
    }
}
