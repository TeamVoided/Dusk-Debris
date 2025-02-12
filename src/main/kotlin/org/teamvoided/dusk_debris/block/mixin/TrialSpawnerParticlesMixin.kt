package org.teamvoided.dusk_debris.block.mixin

import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TrialSpawnerLogic
import org.teamvoided.dusk_debris.util.spawnParticles

class TrialSpawnerParticlesMixin {
    companion object {
        const val particleDistance = 2

        fun trialSpawnerParticles(pos: BlockPos, logic: TrialSpawnerLogic, world: ServerWorld) {
            val currentMobs = logic.data.currentMobs
            if (currentMobs.isNotEmpty()) {
                val mob = world.getEntity(currentMobs.random())
                if (mob != null) {
                    val blockPos = pos.ofCenter()
                    val mobPos = mob.pos.add(0.0, mob.height / 2.0, 0.0)

                    val distance = (blockPos.distanceTo(mobPos) * particleDistance).toInt()
                    val angle = blockPos.subtract(mobPos).normalize().multiply(1.0 / particleDistance)
                    val particle = if (logic.isOminous) ParticleTypes.SOUL_FIRE_FLAME else ParticleTypes.FLAME
                    var particlePos = mobPos
                    val random = world.random
                    repeat(distance) {
                        world.spawnParticles(
                            particle,
                            particlePos.add(
                                (random.nextDouble() - 0.5),
                                (random.nextDouble() - 0.5),
                                (random.nextDouble() - 0.5)
                            ),
                            Vec3d.ZERO
                        )
                        particlePos = particlePos.add(angle)
                    }
                }
            }
        }
    }
}