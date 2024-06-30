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
        if (entity.velocity.length() > velocityThreshold)
            println("onEntityCollision")
        super.onEntityCollision(state, world, pos, entity)
    }

    override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        if (entity.velocity.length() > velocityThreshold) {
            println("onLandedUpon")
            if (!world.isClient) world.breakBlock(pos, false)
        }
        super.onLandedUpon(world, state, pos, entity, fallDistance)
    }
}