package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

open class PaperBlock(settings: Properties) : Block(settings) {

    open val velocityThreshold = 7f
    override fun entityInside(state: BlockState, world: Level, pos: BlockPos, entity: Entity) {
        tryBreakPaper(world, pos, entity)
        super.entityInside(state, world, pos, entity)
    }

    override fun fallOn(world: Level, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        tryBreakPaper(world, pos, entity)
        super.fallOn(world, state, pos, entity, fallDistance)
    }

    override fun stepOn(world: Level, pos: BlockPos, state: BlockState, entity: Entity) {
        tryBreakPaper(world, pos, entity)
        super.stepOn(world, pos, state, entity)
    }

    private fun tryBreakPaper(world: Level, pos: BlockPos, entity: Entity) {
        if (!world.isClientSide && entity.deltaMovement.length() > velocityThreshold) {
            world.destroyBlock(pos, false)
        }
    }
    fun test(entity: Entity){
        entity.onPos
    }
}