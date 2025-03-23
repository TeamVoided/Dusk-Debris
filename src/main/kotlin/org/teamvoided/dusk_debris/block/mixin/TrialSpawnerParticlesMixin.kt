package org.teamvoided.dusk_debris.block.mixin

import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TrialSpawnerLogic
import org.teamvoided.dusk_debris.particle.BetweenPointsParticleEffect
import org.teamvoided.dusk_debris.util.spawnParticles

object TrialSpawnerParticlesMixin {
    const val particleDistance = 2

    @JvmStatic
    fun trialSpawnerParticles(blockPos: BlockPos, logic: TrialSpawnerLogic, world: ServerWorld) {
        val currentMobs = logic.data.currentMobs
        if (currentMobs.isNotEmpty()) {
            val mob = world.getEntity(currentMobs.random())
            if (mob != null) {
                val random = world.random
                val pos = blockPos.ofCenter()
                val mobPos = mob.pos.add(0.0, mob.height / 2.0, 0.0)

                world.spawnParticles(
                    BetweenPointsParticleEffect(pos, logic.isOminous, particleDistance, 5),
                    mobPos.add(
                        (random.nextDouble() - 0.5),
                        (random.nextDouble() - 0.5),
                        (random.nextDouble() - 0.5)
                    ),
                    Vec3d.ZERO
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