package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.util.addParticle
import org.teamvoided.dusk_debris.util.createCuboidShape
import org.teamvoided.dusk_debris.util.spawnParticles

class SoulVesselBlock(settings: Settings) : PillarBlock(settings), Waterloggable {

    init {
        defaultState = stateManager.defaultState
            .with(Properties.WATERLOGGED, false)
            .with(AXIS, Direction.Axis.Y)
    }

    override fun getCodec(): MapCodec<SoulVesselBlock> {
        return CODEC
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        return super.getPlacementState(ctx)
            ?.with(Properties.WATERLOGGED, fluidState.fluid == Fluids.WATER)
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        return super.canPlaceAt(state, world, pos)
    }

    override fun onProjectileHit(world: World, state: BlockState, hit: BlockHitResult, projectile: ProjectileEntity) {
        val blockPos = hit.blockPos
        if (!world.isClient &&
            projectile.canModifyAt(world, blockPos) &&
            projectile.canBreakBlocks(world) &&
            projectile.velocity.length() > 0.75f
        ) {
            val random = world.random
            repeat(20) {
                (world as ServerWorld).spawnParticles(
                    DuskParticles.DRAINED_SOUL,
                    blockPos.ofCenter(),
                    Vec3d(
                        (random.nextDouble() - 0.5),
                        (random.nextDouble() - 0.5),
                        (random.nextDouble() - 0.5)
                    ).normalize().multiply(0.3)
                )
            }
            world.breakBlock(blockPos, true, projectile)
        }
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return VoxelShapes.union(
            ORB,
            createCuboidShape(4.0, 0.0, 12.0, 16.0, state.get(AXIS))
        )
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.get(Properties.WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        val particlePosOffset = Vec3d(
            (random.nextDouble() - 0.5) * 0.6,
            (random.nextDouble() - 0.5) * 0.6,
            (random.nextDouble() - 0.5) * 0.6
        )
        val particleVelocity = particlePosOffset.multiply(-0.2)
        world.addParticle(
            DuskParticles.DRAINED_SOUL,
            pos.ofCenter().add(particlePosOffset),
            particleVelocity
        )
        super.randomDisplayTick(state, world, pos, random)
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.get(Properties.WATERLOGGED)) Fluids.WATER.getStill(false) else super.getFluidState(state)
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return false
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(Properties.WATERLOGGED)
    }

    companion object {
        val CODEC: MapCodec<SoulVesselBlock> = createCodec { settings: Settings ->
            SoulVesselBlock(
                settings
            )
        }
        val ORB = createCuboidShape(2.0, 2.0, 14.0, 14.0)
    }
}