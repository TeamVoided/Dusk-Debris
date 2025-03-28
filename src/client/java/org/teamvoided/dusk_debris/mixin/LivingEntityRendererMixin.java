package org.teamvoided.dusk_debris.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.entity.GloomEntity;
import org.teamvoided.dusk_debris.entity.skeleton.gloom.GloomEntityRenderer;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> {
    @Shadow public abstract void render(T livingEntity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i);

    //@Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    //public VertexConsumer getVC(VertexConsumerProvider vertexConsumer, RenderLayer renderLayer, @Local(ordinal = 1, argsOnly = true) float tickDelta, @Local(argsOnly = true) T livingEntity) {
    //    if (livingEntity instanceof GloomEntity gloom) {
    //        //return vertexConsumer.getBuffer(renderLayer).color(ColorUtil.Argb32.of(256, -1));
    //        VertexConsumer ret = GloomEntityRenderer.gloomTranslucency(gloom, vertexConsumer, renderLayer, tickDelta);
    //        if (ret != null)
    //            return ret;
    //    }
    //    return vertexConsumer.getBuffer(renderLayer);
    //}

    @Inject(at = @At("HEAD"), method = "renderFlipped", cancellable = true)
    private static void run(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        //if (entity instanceof PlayerEntity) {
            //String string = Formatting.strip(entity.getName().getString());
            //if ("Dinnerbone".equals(string) || "Grumm".equals(string)) {
            //cir.setReturnValue(((PlayerEntity) entity).isPartVisible(PlayerModelPart.CAPE));
            //}
        //}
    }
}
