package org.teamvoided.dusk_debris.mixin.pixel_accurate;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.block.sign.AbstractSignBlock;
import net.minecraft.block.sign.SignBlock;
import net.minecraft.block.sign.WallSignBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.dusk_debris.util.SignFunctions;

@Mixin(SignBlockEntityRenderer.class)
public abstract class SignBlockEntityRendererMixin implements BlockEntityRenderer<SignBlockEntity> {
    @Shadow
    void setupMatrices(MatrixStack matrices, float rotation, BlockState state) {
    }

    @Shadow
    void renderText(BlockPos pos, SignText text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int lineHeight, int lineWidth, boolean front) {
    }

    @Inject(method = "render(Lnet/minecraft/block/entity/SignBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At("HEAD"), cancellable = true)
    public void render(SignBlockEntity signBlockEntity, float f, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int j, CallbackInfo ci) {
        BlockState blockState = signBlockEntity.getCachedState();
        if (blockState.getBlock() instanceof SignBlock || blockState.getBlock() instanceof WallSignBlock) {
            AbstractSignBlock abstractSignBlock = (AbstractSignBlock) blockState.getBlock();
            this.render(signBlockEntity, matrices, vertexConsumers, light, blockState, abstractSignBlock);
            ci.cancel();
        }
    }

    @Unique
    void render(SignBlockEntity blockEntity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, BlockState state, AbstractSignBlock block) {
        matrices.push();
        this.setupMatrices(matrices, -block.getRotationDegrees(state), state);
        if (!(state.getBlock() instanceof SignBlock)) {
            matrices.translate(0f, 0.0625f, 0f);
        }
        this.renderText(blockEntity.getPos(), blockEntity.getFrontText(), matrices, vertexConsumers, light, blockEntity.getTextLineHeight(), blockEntity.getMaxTextLineWidth(), true);
        this.renderText(blockEntity.getPos(), blockEntity.getBackText(), matrices, vertexConsumers, light, blockEntity.getTextLineHeight(), blockEntity.getMaxTextLineWidth(), false);
        matrices.pop();
    }


    @Shadow
    private void setTextAngles(MatrixStack matrices, boolean front, Vec3d translation) {
    }

    @Shadow
    Vec3d getTextOffset() {
        return null;
    }

    @Redirect(method = "renderText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/SignBlockEntityRenderer;setTextAngles(Lnet/minecraft/client/util/math/MatrixStack;ZLnet/minecraft/util/math/Vec3d;)V"))
    void text(SignBlockEntityRenderer instance, MatrixStack matrices, boolean front, Vec3d translation, @Local BlockPos pos) {
        var world = MinecraftClient.getInstance().world;
        assert world != null;
        var block = world.getBlockState(pos).getBlock();
        if (block instanceof SignBlock || block instanceof WallSignBlock) {
            var translationAlt = new Vec3d(0.0, 0.234375, 0.0626);
            this.setTextAngles(matrices, front, translationAlt);
        } else {
            this.setTextAngles(matrices, front, this.getTextOffset());
        }
    }
}