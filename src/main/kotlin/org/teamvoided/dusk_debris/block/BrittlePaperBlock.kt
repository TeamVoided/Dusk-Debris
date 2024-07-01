package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class BrittlePaperBlock(settings: Settings) : PaperBlock(settings) {
    override val velocityThreshold = 5.61f
}