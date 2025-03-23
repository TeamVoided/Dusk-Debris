package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.fluid.Fluids
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import org.teamvoided.dusks_and_dungeons.data.tags.DnDEntityTypeTags

class WaterFernBlock(settings: Settings) : AbstractPlantBlock(settings) {
    override fun getCodec(): MapCodec<out AbstractPlantBlock> = CODEC

    override fun getCollisionShape(
        state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext
    ): VoxelShape {
        val entity = (context as EntityShapeContext).entity
        return if (entity != null && (entity.type.isIn(DnDEntityTypeTags.NO_COLLIDE_WATER_FERN) || entity is ProjectileEntity)) VoxelShapes.empty()
        else super.getCollisionShape(state, world, pos, context)
    }

    override fun canPlantOnTop(floor: BlockState, world: BlockView, pos: BlockPos): Boolean {
        val fluidState = world.getFluidState(pos)
        val fluidState2 = world.getFluidState(pos.up())
        return (fluidState.isIn(FluidTags.WATER) || floor.block is IceBlock) && fluidState2.fluid == Fluids.EMPTY
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext):
            VoxelShape = SHAPE

    companion object {
        val CODEC: MapCodec<WaterFernBlock> = createCodec(::WaterFernBlock)
        val SHAPE: VoxelShape = createCuboidShape(1.0, -2.0, 1.0, 15.0, 2.0, 15.0)
    }
}