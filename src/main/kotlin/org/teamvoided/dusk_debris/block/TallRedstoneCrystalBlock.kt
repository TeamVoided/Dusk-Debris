package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.RedstoneTorchBlock
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.particle.DustParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.Hand
import net.minecraft.util.ItemInteractionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.int_provider.UniformIntProvider
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World

class TallRedstoneCrystalBlock(settings: Settings) : TallDirectionalBlock(settings) {
    init {
        this.defaultState = defaultState
            .with(FACING, Direction.UP)
            .with(HALF, DoubleBlockHalf.LOWER)
            .with(LIT, false)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING, HALF, LIT, WATERLOGGED)
    }

    override fun onBlockBreakStart(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity) {
        light(state, world, pos)
        super.onBlockBreakStart(state, world, pos, player)
    }

    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        if (!entity.bypassesSteppingEffects()) {
            light(state, world, pos)
        }

        super.onSteppedOn(world, pos, state, entity)
    }

    override fun onInteract(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hand: Hand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (world.isClient) {
            spawnParticles(world, pos)
        } else {
            light(state, world, pos)
        }

        return if (stack.item is BlockItem && ItemPlacementContext(entity, hand, stack, hitResult).canPlace()
        ) ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
        else ItemInteractionResult.SUCCESS
    }

    override fun onStacksDropped(
        state: BlockState,
        world: ServerWorld,
        pos: BlockPos,
        stack: ItemStack,
        dropExperience: Boolean
    ) {
        super.onStacksDropped(state, world, pos, stack, dropExperience)
        if (dropExperience) {
            this.dropConditionalExperience(world, pos, stack, UniformIntProvider.create(1, 5))
        }
    }

    override fun getRandomTicks(state: BlockState): Boolean = (state.get(LIT))
    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (state.get(LIT)) {
            dark(state, world, pos)
        }
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        if (state.get(LIT)) {
            spawnParticles(world, pos)
        }
    }

    companion object {
        val LIT: BooleanProperty = RedstoneTorchBlock.LIT

        private fun light(state: BlockState, world: World, pos: BlockPos) {
            changeLitState(state, world, pos, true)
        }

        private fun dark(state: BlockState, world: World, pos: BlockPos) {
            changeLitState(state, world, pos, false)
        }

        private fun changeLitState(state: BlockState, world: World, pos: BlockPos, light: Boolean) {
            spawnParticles(world, pos)
            world.setBlockState(
                pos,
                state
                    .with(HALF, state.get(HALF))
                    .with(LIT, light),
                3
            )
            world.setBlockState(
                pos.offset(getDirectionTowardsOtherPart(state.get(HALF), state.get(FACING))),
                state
                    .with(HALF, getOppositeCrystalState(state))
                    .with(LIT, light),
                3
            )
        }

        private fun spawnParticles(world: World, pos: BlockPos) {
            val randomGenerator = world.random
            world.addParticle(
                DustParticleEffect.DEFAULT,
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
