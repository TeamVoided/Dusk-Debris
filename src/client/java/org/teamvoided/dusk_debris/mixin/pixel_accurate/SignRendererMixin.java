package org.teamvoided.dusk_debris.mixin.pixel_accurate;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignRenderer.class)
public abstract class SignRendererMixin implements BlockEntityRenderer<SignBlockEntity> {
    @Shadow
    void translateSign(PoseStack matrices, float rotation, BlockState state) {
    }

    @Shadow
    void renderSignText(BlockPos pos, SignText text, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int lineHeight, int lineWidth, boolean front) {
    }

    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/SignBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"), cancellable = true)
    public void render(SignBlockEntity signBlockEntity, float f, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int j, CallbackInfo ci) {
        BlockState blockState = signBlockEntity.getBlockState();
        if (blockState.getBlock() instanceof StandingSignBlock || blockState.getBlock() instanceof WallSignBlock) {
            SignBlock abstractSignBlock = (SignBlock) blockState.getBlock();
            this.render(signBlockEntity, matrices, vertexConsumers, light, blockState, abstractSignBlock);
            ci.cancel();
        }
    }

    @Unique
    void render(SignBlockEntity blockEntity, PoseStack matrices, MultiBufferSource vertexConsumers, int light, BlockState state, SignBlock block) {
        matrices.pushPose();
        this.translateSign(matrices, -block.getYRotationDegrees(state), state);
        if (!(state.getBlock() instanceof StandingSignBlock)) {
            matrices.translate(0f, 0.0625f, 0f);
        }
        this.renderSignText(blockEntity.getBlockPos(), blockEntity.getFrontText(), matrices, vertexConsumers, light, blockEntity.getTextLineHeight(), blockEntity.getMaxTextLineWidth(), true);
        this.renderSignText(blockEntity.getBlockPos(), blockEntity.getBackText(), matrices, vertexConsumers, light, blockEntity.getTextLineHeight(), blockEntity.getMaxTextLineWidth(), false);
        matrices.popPose();
    }


    @Shadow
    private void translateSignText(PoseStack matrices, boolean front, Vec3 translation) {
    }

    @Shadow
    Vec3 getTextOffset() {
        return null;
    }

    @Redirect(method = "renderSignText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/SignRenderer;translateSignText(Lcom/mojang/blaze3d/vertex/PoseStack;ZLnet/minecraft/world/phys/Vec3;)V"))
    void text(SignRenderer instance, PoseStack matrices, boolean front, Vec3 translation, @Local BlockPos pos) {
        var world = Minecraft.getInstance().level;
        assert world != null;
        var block = world.getBlockState(pos).getBlock();
        if (block instanceof StandingSignBlock || block instanceof WallSignBlock) {
            var translationAlt = new Vec3(0.0, 0.234375, 0.0626);
            this.translateSignText(matrices, front, translationAlt);
        } else {
            this.translateSignText(matrices, front, this.getTextOffset());
        }
    }
}