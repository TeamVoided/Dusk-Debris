package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.particle.entity.EinsteinParticleEffect
import org.teamvoided.dusk_debris.util.addParticle

class EntityTestParticleBlock(settings: Properties) : Block(settings) {
    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        val entitiesAround = world.getEntities(
            null, AABB(
                pos.x - RANGE - 1,
                pos.y - RANGE - 1,
                pos.z - RANGE - 1,
                pos.x + RANGE,
                pos.y + RANGE,
                pos.z + RANGE,
            )
        ) { it is LivingEntity }
        val entity: Entity? = if (entitiesAround.isNotEmpty()) {
            entitiesAround[random.nextInt(entitiesAround.size)]
        } else {
            null
        }
        val particlePos = Vec3(
            (random.nextDouble() - 0.5) * 5,
            (random.nextDouble() - 0.5) * 5,
            (random.nextDouble() - 0.5) * 5
        ).add(pos.center)
        val particleVel = Vec3(
            (world.random.nextDouble() - 0.5) * 0.2,
            world.random.nextDouble() * 0.2 + 0.1,
            (world.random.nextDouble() - 0.5) * 0.2,
        )
        world.addParticle(
            EinsteinParticleEffect(entity),
            particlePos,
            particleVel
        )
        super.animateTick(state, world, pos, random)
    }

    companion object {
        const val RANGE = 6.0
    }
}