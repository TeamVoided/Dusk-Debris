package org.teamvoided.dusk_debris.mixin;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(PatrolSpawner.class)
public class PatrolSpawnerMixin {

    @Inject(method = "spawnPatrolMember", at = @At("HEAD"), cancellable = true)
    private void orVindicator(ServerLevel world, BlockPos pos, RandomSource random, boolean captain, CallbackInfoReturnable<Boolean> cir) {
        if (captain) {//&& world.random.nextInt(5) == 0) {
            BlockState blockState = world.getBlockState(pos);
            if (!NaturalSpawner.isValidEmptySpawnBlock(world, pos, blockState, blockState.getFluidState(), EntityType.VINDICATOR)) {
                cir.setReturnValue(false);
            } else if (!PatrollingMonster.checkPatrollingMonsterSpawnRules(EntityType.VINDICATOR, world, MobSpawnType.PATROL, pos, random)) {
                cir.setReturnValue(false);
            } else {
                PatrollingMonster patrolEntity = (PatrollingMonster) EntityType.VINDICATOR.create(world);
                if (patrolEntity != null) {
//                    if (captain) {
                    patrolEntity.setPatrolLeader(true);
                    patrolEntity.findPatrolTarget();

                    patrolEntity.setPos((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
                    patrolEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.PATROL, (SpawnGroupData) null);
                    world.addFreshEntityWithPassengers(patrolEntity);
                    cir.setReturnValue(true);
                } else {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
