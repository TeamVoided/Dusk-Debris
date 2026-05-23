package org.teamvoided.dusk_debris.block.mixin

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.particle.BetweenPointsParticleEffect
import org.teamvoided.dusk_debris.util.spawnParticles

object TrialSpawnerParticlesMixin {
    const val particleDistance = 2

    @JvmStatic
    fun trialSpawnerParticles(blockPos: BlockPos, logic: TrialSpawner, world: ServerLevel) {
        val currentMobs = logic.data.currentMobs
        if (currentMobs.isNotEmpty()) {
            val mob = world.getEntity(currentMobs.random())
            if (mob != null) {
                val random = world.random
                val pos = blockPos.center
                val mobPos = mob.position().add(0.0, mob.bbHeight / 2.0, 0.0)

                world.spawnParticles(
                    BetweenPointsParticleEffect(pos, logic.isOminous, particleDistance, 5),
                    mobPos.add(
                        (random.nextDouble() - 0.5),
                        (random.nextDouble() - 0.5),
                        (random.nextDouble() - 0.5)
                    ),
                    Vec3.ZERO
                )

//                val distance = (pos.distanceTo(mobPos) * particleDistance).toInt()
//                val angle = pos.subtract(mobPos).normalize().multiply(1.0 / particleDistance)
//                val particle = if (logic.isOminous) ParticleTypes.SOUL_FIRE_FLAME else ParticleTypes.FLAME
//                var particlePos = mobPos
//                repeat(distance) {
//                    world.spawnParticles(
//                        particle,
//                        particlePos.add(
//                            (random.nextDouble() - 0.5),
//                            (random.nextDouble() - 0.5),
//                            (random.nextDouble() - 0.5)
//                        ),
//                        Vec3d.ZERO
//                    )
//                    particlePos = particlePos.add(angle)
//                }
            }
        }
    }
}