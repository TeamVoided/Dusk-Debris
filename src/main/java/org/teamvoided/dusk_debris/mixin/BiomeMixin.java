package org.teamvoided.dusk_debris.mixin;

import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.world.WaterColors;

@Mixin(Biome.class)
public class BiomeMixin {

    @Shadow
    @Final
    private BiomeEffects effects;

    @Shadow
    @Final
    private Biome.Weather weather;

//    public BiomeMixin(Biome.Weather weather) {
//        this.weather = weather;
//    }

    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    public void getNewWaterColor(CallbackInfoReturnable<Integer> cir) {
        var waterColor = this.effects.getWaterColor();
        if (waterColor == 4159204) {
            cir.setReturnValue(getCustomWaterColor());
        }
    }

    @Unique
    private int getCustomWaterColor() {
        double d = (double) MathHelper.clamp(this.weather.temperature(), 0.0F, 1.0F);
        double e = (double) MathHelper.clamp(this.weather.downfall(), 0.0F, 1.0F);
        return WaterColors.getColor(d, e);
    }
}
