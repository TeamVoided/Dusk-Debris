package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.util.addParticle
import org.teamvoided.dusk_debris.util.createCuboidShape
import org.teamvoided.dusk_debris.util.spawnParticles

class SoulVesselBlock(settings: Properties) : RotatedPillarBlock(settings), SimpleWaterloggedBlock {

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(AXIS, Direction.Axis.Y)
        )
    }

    override fun codec(): MapCodec<SoulVesselBlock> {
        return CODEC
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val fluidState = ctx.level.getFluidState(ctx.clickedPos)
        return super.getStateForPlacement(ctx)
            ?.setValue(BlockStateProperties.WATERLOGGED, fluidState.type == Fluids.WATER)
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        return super.canSurvive(state, world, pos)
    }

    override fun onProjectileHit(world: Level, state: BlockState, hit: BlockHitResult, projectile: Projectile) {
        val blockPos = hit.blockPos
        if (!world.isClientSide &&
            projectile.mayInteract(world, blockPos) &&
            projectile.mayBreak(world) &&
            projectile.deltaMovement.length() > 0.75f
        ) {
            val random = world.random
            repeat(20) {
                (world as ServerLevel).spawnParticles(
                    DuskParticles.DRAINED_SOUL,
                    blockPos.center,
                    Vec3(
                        (random.nextDouble() - 0.5),
                        (random.nextDouble() - 0.5),
                        (random.nextDouble() - 0.5)
                    ).normalize().scale(0.3)
                )
            }
            world.destroyBlock(blockPos, true, projectile)
        }
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return Shapes.or(
            ORB,
            createCuboidShape(4.0, 0.0, 12.0, 16.0, state.getValue(AXIS))
        )
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        val particlePosOffset = Vec3(
            (random.nextDouble() - 0.5) * 0.6,
            (random.nextDouble() - 0.5) * 0.6,
            (random.nextDouble() - 0.5) * 0.6
        )
        val particleVelocity = particlePosOffset.scale(-0.2)
        world.addParticle(
            DuskParticles.DRAINED_SOUL,
            pos.center.add(particlePosOffset),
            particleVelocity
        )
        super.animateTick(state, world, pos, random)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(BlockStateProperties.WATERLOGGED)) Fluids.WATER.getSource(false) else super.getFluidState(state)
    }

    override fun isPathfindable(state: BlockState, navigationType: PathComputationType): Boolean {
        return false
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(BlockStateProperties.WATERLOGGED)
    }

    companion object {
        val CODEC: MapCodec<SoulVesselBlock> = simpleCodec { settings: Properties ->
            SoulVesselBlock(
                settings
            )
        }
        val ORB = createCuboidShape(2.0, 2.0, 14.0, 14.0)
    }
}