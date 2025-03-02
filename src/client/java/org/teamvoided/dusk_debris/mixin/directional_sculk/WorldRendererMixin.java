package org.teamvoided.dusk_debris.mixin.directional_sculk;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.teamvoided.dusk_debris.particle.ShriekDirectionalParticleEffect;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static net.minecraft.Bootstrap.println;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    @Nullable
    private ClientWorld world;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    private void changeShriekParticles(int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (world != null && eventId == 3007 && this.world.getBlockState(pos).contains(Properties.FACING)) {
            BlockState blockState = this.world.getBlockState(pos);
            Direction direction = blockState.get(Properties.FACING);
            var center = pos.ofCenter();
            for (int count = 0; count < 10; ++count) {
                this.world.addParticle(
                        new ShriekDirectionalParticleEffect(direction, count * 5),
                        false,
                        center.x,
                        center.y,
                        center.z,
                        0.0,
                        0.0,
                        0.0
                );
            }
            boolean bl2 = blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED);
            if (!bl2) {
                world.playSound(
                        center.x,
                        center.y,
                        center.z,
                        SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK,
                        SoundCategory.BLOCKS,
                        2.0F,
                        0.6F + world.random.nextFloat() * 0.4F,
                        false
                );
            }
            ci.cancel();
        }
    }
}