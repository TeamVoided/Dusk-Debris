package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import net.minecraft.world.WorldView
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties

class GoldenCocoonBlock(settings: Settings) : Block(settings) {
    public override fun getCodec(): MapCodec<out GoldenCocoonBlock> = CODEC

    init {
        this.defaultState = stateManager.defaultState.with(DuskProperties.COCOON, false)
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean =
        sideCoversSmallSquare(world, pos.up(), Direction.DOWN)

    override fun getRandomTicks(state: BlockState): Boolean = !state.get(DuskProperties.COCOON)

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (random.nextInt(CHANCE) == 0) world.setBlockState(pos, state.with(DuskProperties.COCOON, true), 2)
        super.randomTick(state, world, pos, random)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        if (state.get(DuskProperties.COCOON)) {
            val direction = Direction.random(random)
            if (direction != Direction.UP) {
                val directionPos = pos.offset(direction)
                val directionWorldState = world.getBlockState(directionPos)
                if (!directionWorldState.isSideSolidFullSquare(world, directionPos, direction.opposite)) {
                    val x = if (direction.offsetX == 0) random.nextDouble() else 0.5 + direction.offsetX * 0.6
                    val y = if (direction.offsetY == 0) random.nextDouble() else 0.5 + direction.offsetY * 0.6
                    val z = if (direction.offsetZ == 0) random.nextDouble() else 0.5 + direction.offsetZ * 0.6
                    world.addParticle(ParticleTypes.DRIPPING_HONEY, pos.x + x, pos.y + y, pos.z + z, 0.0, 0.0, 0.0)
                }
            }
        }
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        if (state.get(DuskProperties.COCOON)) {
            addAbsorbtion(player)
            return state.with(DuskProperties.COCOON, false)
        }
        return super.onBreak(world, pos, state, player)
    }

    fun addAbsorbtion(entity: LivingEntity? = null) {
        if (entity != null) entity.absorptionAmount += 2f * (1 + 1)
        //world.setBlockState(pos, state.with(DuskProperties.COCOON, false), 2)
    }

    companion object {
        val CODEC: MapCodec<GoldenCocoonBlock> = createCodec(::GoldenCocoonBlock)
        const val CHANCE = 128
    }
}