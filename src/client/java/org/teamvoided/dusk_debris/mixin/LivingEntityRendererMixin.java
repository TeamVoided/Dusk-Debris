package org.teamvoided.dusk_debris.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.dusk_debris.init.DuskRenderTypes;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Shadow
    public abstract void render(T livingEntity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i);

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

    @ModifyReturnValue(method = "renderFlipped", at = @At("RETURN"))
    private static boolean run(boolean original) {
        return original;
    }

    @ModifyReturnValue(method = "getRenderLayer", at = @At("RETURN"))
    RenderLayer modifyLayer(@Nullable RenderLayer original, T entity, boolean showBody, boolean translucent, boolean showOutline) {
        return original;
    }
}
