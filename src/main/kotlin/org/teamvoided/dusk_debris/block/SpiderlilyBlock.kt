package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.TallFlowerBlock
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.dusks_and_dungeons.init.DnDParticles

class SpiderlilyBlock(settings: Settings) : TallFlowerBlock(settings) {
    init {
        this.defaultState = stateManager.defaultState
            .with(FLOWERING, true)
            .with(HALF, DoubleBlockHalf.LOWER)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FLOWERING, HALF)
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        val velocity = entity.velocity.length()
        if (state.get(FLOWERING) && entity.velocity.length() > 0.2) {
            flower(world, pos, state, false, velocity)
        }
    }

    override fun fertilize(world: ServerWorld, random: RandomGenerator, pos: BlockPos, state: BlockState) {
        if (!state.get(FLOWERING)) flower(world, pos, state, true)
        else super.fertilize(world, random, pos, state)
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (random.nextInt(2) == 0) {
            flower(world, pos, state, true)
        }
    }

    override fun getRandomTicks(state: BlockState): Boolean {
        return if (!state.get(FLOWERING)) true else super.getRandomTicks(state)
    }

    companion object {
        val FLOWERING: BooleanProperty = BooleanProperty.of("flowering")
        fun flower(world: World, pos: BlockPos, state: BlockState, flower: Boolean, speed: Double = 0.0) {
            var upperPos = pos
            var lowerPos = pos
            if (state.get(HALF) == DoubleBlockHalf.LOWER) {
                upperPos = pos.up()
            } else {
                lowerPos = pos.down()
            }
            if (!flower) {
                explode(world, upperPos, speed)
            }
            world.setBlockState(upperPos, state.with(HALF, DoubleBlockHalf.UPPER).with(FLOWERING, flower))
            world.setBlockState(lowerPos, state.with(HALF, DoubleBlockHalf.LOWER).with(FLOWERING, flower))

        }

        fun explode(world: World, pos: BlockPos, speed: Double) {
            val random = world.random
            repeat(10) {
                world.addParticle(
                    DnDParticles.SPIDERLILY,
                    true,
                    pos.x + random.nextDouble(),
                    pos.y + random.nextDouble(),
                    pos.z + random.nextDouble(),
                    (random.nextDouble() - random.nextDouble()) * speed,
                    (random.nextDouble() - random.nextDouble()) * speed,
                    (random.nextDouble() - random.nextDouble()) * speed
                )
            }
        }
    }
}