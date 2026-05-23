package org.teamvoided.dusk_debris.mixin.directional_sculk;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.dusk_debris.particle.ShriekDirectionalParticleEffect;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow
    private ClientLevel level;

    @Inject(method = "levelEvent", at = @At("HEAD"), cancellable = true)
    private void changeShriekParticles(int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (eventId == 3007 && this.level.getBlockState(pos).hasProperty(BlockStateProperties.FACING)) {
            BlockState blockState = this.level.getBlockState(pos);
            Direction direction = blockState.getValue(BlockStateProperties.FACING);
            var center = pos.getCenter();
            for (int count = 0; count < 10; ++count) {
                this.level.addParticle(
                        new ShriekDirectionalParticleEffect(direction, count * 5),
                        false,
                        center.x, center.y, center.z,
                        0.0, 0.0, 0.0
                );
            }
            boolean silent = blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED);
            if (!silent) {
                level.playLocalSound(
                        center.x, center.y, center.z,
                        SoundEvents.SCULK_SHRIEKER_SHRIEK,
                        SoundSource.BLOCKS,
                        2F, 0.6F + level.random.nextFloat() * 0.4F,
                        false
                );
            }
            ci.cancel();
        }
    }
}