package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class PaperBlock(settings: Settings) : Block(settings) {

    val velocityThreshold = 1f
    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
//        if (entity.velocity)
        println(1)
        super.onEntityCollision(state, world, pos, entity)
    }
}