package org.teamvoided.dusk_debris.mixin.snow;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Holder;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;
import org.teamvoided.dusk_debris.world.WaterColors;

@Mixin(Biome.class)
public class BiomeIceSnowMixin {
    @Shadow
    @Final
    private Biome.Weather weather;

    @Redirect(method = "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z", at = @At("RETURN"))
    public void canSetIceAbove(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) WorldView world, @Local(argsOnly = true) BlockPos pos) {
        if (cir.getReturnValue() && weather.temperatureModifier != Biome.TemperatureModifier.FROZEN) {
            if (world.getFluidState(pos.down()).isIn(FluidTags.WATER) && world.getFluidState(pos.down(2)).isIn(FluidTags.WATER)) {

                cir.setReturnValue(false);
            }
        }
    }
}
