package org.teamvoided.dusk_debris.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.teamvoided.dusk_debris.util.FogModifierKt.customizeFog;

@Debug(export = true)
@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    @Shadow
    @Nullable
    protected static FogRenderer.MobEffectFogFunction getPriorityFogFunction(Entity entity, float tickDelta) {
        return null;
    }

    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void dusk_debris$applyCustomFog(Camera camera, FogRenderer.FogMode fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci,
                                                   @Local FogRenderer.FogData fogParameters) {
        customizeFog(camera, camera.getEntity(), fogType, viewDistance, thickFog, tickDelta, fogParameters, getPriorityFogFunction(camera.getEntity(), tickDelta));
    }
}
