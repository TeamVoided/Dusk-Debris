package org.teamvoided.dusk_debris.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.dusks_and_dungeons.init.DnDItems;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {

    @Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"))
    private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (stack.isOf(DnDItems.INSTANCE.getBROOM()) && entity instanceof PlayerEntity player) {
            float g = (float) (player.getItemUseTimeLeft() % 10);
            float h = g - 0 + 1F;
            float i = 1F - h / 10F;
            float o = -15F + 75F * MathHelper.cos(i * 2F * 3.1415927F);

            matrices.rotate(Axis.X_POSITIVE.rotationDegrees(90F));
            matrices.translate(1.0, -.7, -.4);

            boolean isRightArm = arm == Arm.RIGHT;
            if (!isRightArm) {
                matrices.translate(0.1, 0.83, 0.35);
                matrices.rotate(Axis.X_POSITIVE.rotationDegrees(-80F));
                matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(-90F));
                matrices.rotate(Axis.X_POSITIVE.rotationDegrees(o));
                matrices.translate(-0.3, 0.22, 0.35);
            }
            else {
                matrices.translate(-0.25, 0.22, 0.35);
                matrices.rotate(Axis.X_POSITIVE.rotationDegrees(-80F));
                matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(90F));
                matrices.rotate(Axis.Z_POSITIVE.rotationDegrees(0F));
                matrices.rotate(Axis.X_POSITIVE.rotationDegrees(o));
            }
        }
    }
}
