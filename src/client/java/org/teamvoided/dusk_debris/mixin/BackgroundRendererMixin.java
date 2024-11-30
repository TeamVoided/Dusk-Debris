package org.teamvoided.dusk_debris.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.teamvoided.dusk_debris.util.FogModifierKt.customizeFog;

@Debug(export = true)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void dusk_debris$applyCustomFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci,
                                                   @Local BackgroundRenderer.FogParameters fogParameters) {
        customizeFog(camera, camera.getFocusedEntity(), fogType, viewDistance, thickFog, tickDelta, fogParameters);
    }

}
