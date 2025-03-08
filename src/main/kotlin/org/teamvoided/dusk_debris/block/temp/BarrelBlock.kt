package org.teamvoided.dusk_debris.block.temp

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BarrelBlockEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.mob.PiglinBrain
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.stat.Stats
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.state.property.Property
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World

class BarrelBlock(settings: Settings) : BlockWithEntity(settings) {
    public override fun getCodec(): MapCodec<BarrelBlock> {
        return CODEC
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        if (world.isClient) {
            return ActionResult.SUCCESS
        } else {
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is BarrelBlockEntity) {
                entity.openHandledScreen(blockEntity)
                entity.incrementStat(Stats.OPEN_BARREL)
                PiglinBrain.onGuardedBlockInteracted(entity, true)
            }

            return ActionResult.CONSUME
        }
    }

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        ItemScatterer.scatterInventory(state, newState, world, pos)
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        val blockEntity = world.getBlockEntity(pos)!!
        if (blockEntity is BarrelBlockEntity) {
            blockEntity.tick()
        }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return BarrelBlockEntity(pos, state)
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun hasComparatorOutput(state: BlockState): Boolean {
        return true
    }

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos))
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING) as Direction)) as BlockState
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(FACING) as Direction))
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING, OPEN)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(FACING, ctx.playerLookDirection.opposite)
    }

    init {
        this.defaultState = stateManager.defaultState
            .with(FACING, Direction.NORTH)
            .with(OPEN, false)
    }

    companion object {
        val CODEC: MapCodec<BarrelBlock> = createCodec(::BarrelBlock)
        val FACING: DirectionProperty = Properties.FACING
        val OPEN: BooleanProperty = Properties.OPEN
    }
}
