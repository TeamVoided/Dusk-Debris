package org.teamvoided.dusk_debris.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.util.Utils;

import static org.teamvoided.dusk_debris.util.UtilsClientHelperFunctionsKt.sendMessageIngame;

@Mixin(GameRenderer.class)

public class GameRendererMixin {
    @Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
    private static void betterFade(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
        StatusEffectInstance statusEffectInstance = entity.getStatusEffect(StatusEffects.NIGHT_VISION);
        assert statusEffectInstance != null;
        int fadeTime = 200;
        if (statusEffectInstance.endsWithin(fadeTime)) {
            float rate = 0.05F;

            float duration = statusEffectInstance.getDuration();
            float dur = (duration - (duration <= 0 ? 0F : tickDelta));
            float mult = (1F - dur / fadeTime) / 2F;
//            Float ret = mult * MathHelper.cos((rate * dur * Utils.pi) + Utils.pi) - mult + 1F;
            Float ret = (mult / 2F) * MathHelper.cos((rate * dur * Utils.pi) + Utils.pi) - (1.5F * mult) + 1F; //alternative so the max decreases as well
            cir.setReturnValue(ret);
        } else {
            cir.setReturnValue(1F);
        }
    }
}
