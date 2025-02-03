package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.particle.EntityTestParticleEffect
import org.teamvoided.dusk_debris.particle.GoopFlyingParticleEffect
import org.teamvoided.dusk_debris.util.addParticle

class EntityTestParticleBlock(settings: Settings) : Block(settings) {
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
//        val entitiesAround = world.getOtherEntities(
//            null, Box(
//                pos.x - RANGE - 1,
//                pos.y - RANGE - 1,
//                pos.z - RANGE - 1,
//                pos.x + RANGE,
//                pos.y + RANGE,
//                pos.z + RANGE,
//            )
//        ) { it is LivingEntity }
//        val entity: Entity? = if (entitiesAround.isNotEmpty()) {
//            entitiesAround[random.nextInt(entitiesAround.size)]
//        } else {
//            null
//        }
        val particlePos = Vec3d(
            (random.nextDouble() - 0.5) * 5,
            (random.nextDouble() - 0.5) * 5,
            (random.nextDouble() - 0.5) * 5
        ).add(pos.ofCenter())
        val particleVel = Vec3d(
            1.0, 0.0, 0.0
//            (world.random.nextDouble() - world.random.nextDouble()) * 0.1,
//            world.random.nextDouble() * 0.2 + 0.1,
//            (world.random.nextDouble() - world.random.nextDouble()) * 0.1,
        )
        world.addParticle(
            GoopFlyingParticleEffect(100),
            particlePos,
            particleVel
        )
        super.randomDisplayTick(state, world, pos, random)
    }

    companion object {
        const val RANGE = 6.0
    }
}