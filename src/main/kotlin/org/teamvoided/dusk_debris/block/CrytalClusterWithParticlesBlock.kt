package org.teamvoided.dusk_debris.block

import net.minecraft.block.AmethystClusterBlock
import net.minecraft.block.BlockState
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World

class CrytalClusterWithParticlesBlock(height: Float, aabbOffset: Float, settings: Settings) :
    AmethystClusterBlock(height, aabbOffset, settings) {
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        val velocity = getParticleDirections(state.get(FACING), random)
        world.addParticle(
            ParticleTypes.OMINOUS_SPAWNING,
            true,
            pos.x + random.nextDouble(),
            pos.y + random.nextDouble(),
            pos.z + random.nextDouble(),
            velocity.x,
            velocity.y,
            velocity.z
        )
        super.randomDisplayTick(state, world, pos, random)
    }

    companion object {
        fun getParticleDirections(direction: Direction, random: RandomGenerator): Vec3d {
            val velMain = (random.nextDouble() * 0.4) + 0.2
            val velSec = (random.nextDouble() - random.nextDouble()) * 0.125
            return (when (direction) {
                Direction.UP -> Vec3d(velSec, -velMain, velSec)
                Direction.DOWN -> Vec3d(velSec, velMain, velSec)
                Direction.NORTH -> Vec3d(velSec, velSec, velMain)
                Direction.SOUTH -> Vec3d(velSec, velSec,-velMain)
                Direction.WEST -> Vec3d(velMain, velSec, velSec)
                Direction.EAST -> Vec3d(-velMain, velSec, velSec)
                else -> Vec3d(velSec, velMain, velSec)
            })
        }
    }
}