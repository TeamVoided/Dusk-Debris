package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

open class PaperBlock(settings: Settings) : Block(settings) {

    open val velocityThreshold = 7f
    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        tryBreakPaper(world, pos, entity)
        super.onEntityCollision(state, world, pos, entity)
    }

    override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        tryBreakPaper(world, pos, entity)
        super.onLandedUpon(world, state, pos, entity, fallDistance)
    }

    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        tryBreakPaper(world, pos, entity)
        super.onSteppedOn(world, pos, state, entity)
    }

    private fun tryBreakPaper(world: World, pos: BlockPos, entity: Entity) {
        if (!world.isClient && entity.velocity.length() > velocityThreshold) {
            world.breakBlock(pos, false)
        }
    }
    fun test(entity: Entity){
        entity.steppingPosition
    }
}