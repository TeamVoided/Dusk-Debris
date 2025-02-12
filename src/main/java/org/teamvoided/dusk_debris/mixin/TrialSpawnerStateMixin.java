package org.teamvoided.dusk_debris.mixin;

import net.minecraft.block.TrialSpawnerData;
import net.minecraft.block.enums.TrialSpawnerState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TrialSpawnerLogic;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.block.mixin.TrialSpawnerParticlesMixin;

import java.util.Set;
import java.util.UUID;

@Mixin(TrialSpawnerState.class)
public abstract class TrialSpawnerStateMixin {
    @Shadow @Final private boolean spawnCapable;
    @Inject(method = "tick", at = @At("TAIL"))
    public void tickEntityConnection(BlockPos pos, TrialSpawnerLogic logic, ServerWorld world, CallbackInfoReturnable<TrialSpawnerState> cir) {
        if (world.random.nextInt(100) == 0 && this.spawnCapable)
            TrialSpawnerParticlesMixin.Companion.trialSpawnerParticles(pos, logic, world);
    }
}
