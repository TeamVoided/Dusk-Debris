package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.TallFlowerBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import org.teamvoided.dusk_debris.init.DuskParticles

class SpiderlilyBlock(settings: Properties) : TallFlowerBlock(settings) {
    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(FLOWERING, true)
                .setValue(HALF, DoubleBlockHalf.LOWER)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FLOWERING, HALF)
    }

    override fun entityInside(state: BlockState, world: Level, pos: BlockPos, entity: Entity) {
        val velocity = entity.deltaMovement.length()
        if (state.getValue(FLOWERING) && entity.deltaMovement.length() > 0.2) {
            flower(world, pos, state, false, velocity)
        }
    }

    override fun performBonemeal(world: ServerLevel, random: RandomSource, pos: BlockPos, state: BlockState) {
        if (!state.getValue(FLOWERING)) flower(world, pos, state, true)
        else super.performBonemeal(world, random, pos, state)
    }

    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (random.nextInt(2) == 0) {
            flower(world, pos, state, true)
        }
    }

    override fun isRandomlyTicking(state: BlockState): Boolean {
        return if (!state.getValue(FLOWERING)) true else super.isRandomlyTicking(state)
    }

    companion object {
        val FLOWERING: BooleanProperty = BooleanProperty.create("flowering")
        fun flower(world: Level, pos: BlockPos, state: BlockState, flower: Boolean, speed: Double = 0.0) {
            var upperPos = pos
            var lowerPos = pos
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
                upperPos = pos.above()
            } else {
                lowerPos = pos.below()
            }
            if (!flower) {
                explode(world, upperPos, speed)
            }
            world.setBlockAndUpdate(upperPos, state.setValue(HALF, DoubleBlockHalf.UPPER).setValue(FLOWERING, flower))
            world.setBlockAndUpdate(lowerPos, state.setValue(HALF, DoubleBlockHalf.LOWER).setValue(FLOWERING, flower))

        }

        fun explode(world: Level, pos: BlockPos, speed: Double) {
            val random = world.random
            repeat(10) {
                world.addParticle(
                    DuskParticles.SPIDERLILY,
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