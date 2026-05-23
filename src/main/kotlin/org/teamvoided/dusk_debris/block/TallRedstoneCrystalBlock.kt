package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RedstoneTorchBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.phys.BlockHitResult

class TallRedstoneCrystalBlock(settings: Properties) : TallDirectionalBlock(settings) {
    init {
        this.registerDefaultState(
            defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(LIT, false)
                .setValue(WATERLOGGED, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, HALF, LIT, WATERLOGGED)
    }

    override fun attack(state: BlockState, world: Level, pos: BlockPos, player: Player) {
        light(state, world, pos)
        super.attack(state, world, pos, player)
    }

    override fun stepOn(world: Level, pos: BlockPos, state: BlockState, entity: Entity) {
        if (!entity.isSteppingCarefully) {
            light(state, world, pos)
        }

        super.stepOn(world, pos, state, entity)
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (world.isClientSide) {
            spawnParticles(world, pos)
        } else {
            light(state, world, pos)
        }

        return if (stack.item is BlockItem && BlockPlaceContext(entity, hand, stack, hitResult).canPlace()
        ) ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
        else ItemInteractionResult.SUCCESS
    }

    override fun spawnAfterBreak(
        state: BlockState,
        world: ServerLevel,
        pos: BlockPos,
        stack: ItemStack,
        dropExperience: Boolean
    ) {
        super.spawnAfterBreak(state, world, pos, stack, dropExperience)
        if (dropExperience) {
            this.tryDropExperience(world, pos, stack, UniformInt.of(1, 5))
        }
    }

    override fun isRandomlyTicking(state: BlockState): Boolean = (state.getValue(LIT))
    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(LIT)) {
            dark(state, world, pos)
        }
    }

    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        if (state.getValue(LIT)) {
            spawnParticles(world, pos)
        }
    }

    companion object {
        val LIT: BooleanProperty = RedstoneTorchBlock.LIT

        private fun light(state: BlockState, world: Level, pos: BlockPos) {
            changeLitState(state, world, pos, true)
        }

        private fun dark(state: BlockState, world: Level, pos: BlockPos) {
            changeLitState(state, world, pos, false)
        }

        private fun changeLitState(state: BlockState, world: Level, pos: BlockPos, light: Boolean) {
            spawnParticles(world, pos)
            world.setBlock(
                pos,
                state
                    .setValue(HALF, state.getValue(HALF))
                    .setValue(LIT, light),
                3
            )
            world.setBlock(
                pos.relative(getDirectionTowardsOtherPart(state.getValue(HALF), state.getValue(FACING))),
                state
                    .setValue(HALF, getOppositeCrystalState(state))
                    .setValue(LIT, light),
                3
            )
        }

        private fun spawnParticles(world: Level, pos: BlockPos) {
            val randomGenerator = world.random
            world.addParticle(
                DustParticleOptions.REDSTONE,
                pos.x.toDouble() + randomGenerator.nextDouble(),
                pos.y.toDouble() + randomGenerator.nextDouble(),
                pos.z.toDouble() + randomGenerator.nextDouble(),
                0.0,
                0.0,
                0.0
            )
        }
    }
}
