package org.teamvoided.dusk_debris.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.world.WaterColors;

@Mixin(Biome.class)
public class BiomeWaterColorMixin {

    @Shadow
    @Final
    private BiomeSpecialEffects specialEffects;

    @Shadow
    @Final
    private Biome.ClimateSettings climateSettings;

    @Shadow
    @Final
    private static PerlinSimplexNoise TEMPERATURE_NOISE;

    @Shadow
    @Final
    private BiomeGenerationSettings generationSettings;

    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    public void getNewWaterColor(CallbackInfoReturnable<Integer> cir) {
//        if (new Holder.Direct((Biome) (Object) this).isIn(DuskBiomeTags.getWORLDNOISE_WATER()) ) {
//            cir.setReturnValue(getCustomWaterColor(this.TEMPERATURE_NOISE, this.TEMPERATURE_NOISE));
//        } else
        if (this.specialEffects.getWaterColor() == 4159204) {
            cir.setReturnValue(getCustomWaterColor());
        }
    }

    @Unique
    private int getCustomWaterColor() {
        double d = (double) Mth.clamp(this.climateSettings.temperature(), 0.0F, 1.0F);
        double e = (double) Mth.clamp(this.climateSettings.downfall(), 0.0F, 1.0F);
        return WaterColors.getColor(d, e);
    }

    @Unique
    private int getCustomWaterColor(Float temperature, Float downfall) {
        return WaterColors.getColor(temperature, downfall);
    }
}
