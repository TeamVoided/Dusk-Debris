package org.teamvoided.dusk_debris.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Shadow
    public abstract void render(T livingEntity, float f, float g, PoseStack matrices, MultiBufferSource vertexConsumers, int i);

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

    @ModifyReturnValue(method = "isEntityUpsideDown", at = @At("RETURN"))
    private static boolean run(boolean original) {
        return original;
    }

    @ModifyReturnValue(method = "getRenderType", at = @At("RETURN"))
    RenderType modifyLayer(@Nullable RenderType original, T entity, boolean showBody, boolean translucent, boolean showOutline) {
        return original;
    }
}
