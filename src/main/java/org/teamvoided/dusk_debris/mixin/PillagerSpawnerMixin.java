package org.teamvoided.dusk_debris.mixin;


import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.gen.PillagerSpawner;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.dusk_debris.data.tags.DuskFluidTags;

@Debug(export = true)
@Mixin(PillagerSpawner.class)
public class PillagerSpawnerMixin {

    @Inject(method = "spawnPillager", at = @At("HEAD"), cancellable = true)
    private void orVindicator(ServerWorld world, BlockPos pos, RandomGenerator random, boolean captain, CallbackInfoReturnable<Boolean> cir) {
        if (captain) {//&& world.random.nextInt(5) == 0) {
            BlockState blockState = world.getBlockState(pos);
            if (!SpawnHelper.isClearForSpawn(world, pos, blockState, blockState.getFluidState(), EntityType.VINDICATOR)) {
                cir.setReturnValue(false);
            } else if (!PatrolEntity.canSpawn(EntityType.VINDICATOR, world, SpawnReason.PATROL, pos, random)) {
                cir.setReturnValue(false);
            } else {
                PatrolEntity patrolEntity = (PatrolEntity) EntityType.VINDICATOR.create(world);
                if (patrolEntity != null) {
//                    if (captain) {
                    patrolEntity.setPatrolLeader(true);
                    patrolEntity.setRandomPatrolTarget();

                    patrolEntity.setPosition((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
                    patrolEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, (EntityData) null);
                    world.spawnEntityAndPassengers(patrolEntity);
                    cir.setReturnValue(true);
                } else {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
