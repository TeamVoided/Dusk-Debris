package org.teamvoided.dusk_debris.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.util.Utils;

@Mixin(GameRenderer.class)

public class GameRendererMixin {
    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void betterFade(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
        MobEffectInstance statusEffectInstance = entity.getEffect(MobEffects.NIGHT_VISION);
        assert statusEffectInstance != null;
        int fadeTime = 200;
        if (statusEffectInstance.endsWithin(fadeTime)) {
            float rate = 0.05F;

            float duration = statusEffectInstance.getDuration();
            float dur = (duration - (duration <= 0 ? 0F : tickDelta));
            float mult = (1F - dur / fadeTime) / 2F;
//            Float ret = mult * MathHelper.cos((rate * dur * Utils.pi) + Utils.pi) - mult + 1F;
            Float ret = (mult / 2F) * Mth.cos((rate * dur * Utils.PI) + Utils.PI) - (1.5F * mult) + 1F; //alternative so the max decreases as well
            cir.setReturnValue(ret);
        } else {
            cir.setReturnValue(1F);
        }
    }
}
