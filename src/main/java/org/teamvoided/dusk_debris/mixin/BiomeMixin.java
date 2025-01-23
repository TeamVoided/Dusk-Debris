package org.teamvoided.dusk_debris.mixin;

import net.minecraft.registry.Holder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.data.tags.DuskBiomeTags;
import org.teamvoided.dusk_debris.world.WaterColors;

@Mixin(Biome.class)
public class BiomeMixin {

    @Shadow
    @Final
    private BiomeEffects effects;

    @Shadow
    @Final
    private Biome.Weather weather;

    @Shadow
    @Final
    private static OctaveSimplexNoiseSampler TEMPERATURE_NOISE;

    @Shadow
    @Final
    private GenerationSettings generationSettings;

    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    public void getNewWaterColor(CallbackInfoReturnable<Integer> cir) {
//        if (new Holder.Direct((Biome) (Object) this).isIn(DuskBiomeTags.getWORLDNOISE_WATER()) ) {
//            cir.setReturnValue(getCustomWaterColor(this.TEMPERATURE_NOISE, this.TEMPERATURE_NOISE));
//        } else
        if (this.effects.getWaterColor() == 4159204) {
            cir.setReturnValue(getCustomWaterColor());
        }
    }

    @Unique
    private int getCustomWaterColor() {
        double d = (double) MathHelper.clamp(this.weather.temperature(), 0.0F, 1.0F);
        double e = (double) MathHelper.clamp(this.weather.downfall(), 0.0F, 1.0F);
        return WaterColors.getColor(d, e);
    }

    @Unique
    private int getCustomWaterColor(Float temperature, Float downfall) {
        return WaterColors.getColor(temperature, downfall);
    }
}
