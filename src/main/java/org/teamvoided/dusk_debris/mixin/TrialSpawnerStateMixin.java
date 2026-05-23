package org.teamvoided.dusk_debris.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.block.mixin.TrialSpawnerParticlesMixin;

@Mixin(TrialSpawnerState.class)
public abstract class TrialSpawnerStateMixin {
    @Shadow @Final private boolean isCapableOfSpawning;
    @Inject(method = "tickAndGetNext", at = @At("RETURN"))
    public void tickEntityConnection(BlockPos pos, TrialSpawner logic, ServerLevel world, CallbackInfoReturnable<TrialSpawnerState> cir) {
        if (world.random.nextInt(100) == 0 && this.isCapableOfSpawning)
            TrialSpawnerParticlesMixin.trialSpawnerParticles(pos, logic, world);
    }
}
