package org.teamvoided.dusk_debris.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.dusk_debris.init.DuskItems;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {

    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext transformationMode, HumanoidArm arm, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
        if (stack.is(DuskItems.INSTANCE.getBROOM()) && entity instanceof Player player) {
            float g = (float) (player.getUseItemRemainingTicks() % 10);
            float h = g - 0 + 1F;
            float i = 1F - h / 10F;
            float o = -15F + 75F * Mth.cos(i * 2F * 3.1415927F);

            matrices.mulPose(Axis.XP.rotationDegrees(90F));
            matrices.translate(1.0, -.7, -.4);

            boolean isRightArm = arm == HumanoidArm.RIGHT;
            if (!isRightArm) {
                matrices.translate(0.1, 0.83, 0.35);
                matrices.mulPose(Axis.XP.rotationDegrees(-80F));
                matrices.mulPose(Axis.YP.rotationDegrees(-90F));
                matrices.mulPose(Axis.XP.rotationDegrees(o));
                matrices.translate(-0.3, 0.22, 0.35);
            }
            else {
                matrices.translate(-0.25, 0.22, 0.35);
                matrices.mulPose(Axis.XP.rotationDegrees(-80F));
                matrices.mulPose(Axis.YP.rotationDegrees(90F));
                matrices.mulPose(Axis.ZP.rotationDegrees(0F));
                matrices.mulPose(Axis.XP.rotationDegrees(o));
            }
        }
    }
}
