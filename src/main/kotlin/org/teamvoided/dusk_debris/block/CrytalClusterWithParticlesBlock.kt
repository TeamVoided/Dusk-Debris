package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AmethystClusterBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

class CrytalClusterWithParticlesBlock(height: Float, aabbOffset: Float, settings: Properties) :
    AmethystClusterBlock(height, aabbOffset, settings) {
    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        val velocity = getParticleDirections(state.getValue(FACING), random)
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
        super.animateTick(state, world, pos, random)
    }

    companion object {
        fun getParticleDirections(direction: Direction, random: RandomSource): Vec3 {
            val velMain = (random.nextDouble() * 0.4) + 0.2
            val velSec = (random.nextDouble() - random.nextDouble()) * 0.125
            return (when (direction) {
                Direction.UP -> Vec3(velSec, -velMain, velSec)
                Direction.DOWN -> Vec3(velSec, velMain, velSec)
                Direction.NORTH -> Vec3(velSec, velSec, velMain)
                Direction.SOUTH -> Vec3(velSec, velSec,-velMain)
                Direction.WEST -> Vec3(velMain, velSec, velSec)
                Direction.EAST -> Vec3(-velMain, velSec, velSec)
                else -> Vec3(velSec, velMain, velSec)
            })
        }
    }
}