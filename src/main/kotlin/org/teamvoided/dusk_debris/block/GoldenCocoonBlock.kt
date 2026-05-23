package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties

class GoldenCocoonBlock(settings: Properties) : Block(settings) {
    public override fun codec(): MapCodec<out GoldenCocoonBlock> = CODEC

    init {
        this.registerDefaultState(stateDefinition.any().setValue(DuskProperties.COCOON, false))
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean =
        canSupportCenter(world, pos.above(), Direction.DOWN)

    override fun isRandomlyTicking(state: BlockState): Boolean = !state.getValue(DuskProperties.COCOON)

    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (random.nextInt(CHANCE) == 0) world.setBlock(pos, state.setValue(DuskProperties.COCOON, true), 2)
        super.randomTick(state, world, pos, random)
    }

    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        if (state.getValue(DuskProperties.COCOON)) {
            val direction = Direction.getRandom(random)
            if (direction != Direction.UP) {
                val directionPos = pos.relative(direction)
                val directionWorldState = world.getBlockState(directionPos)
                if (!directionWorldState.isFaceSturdy(world, directionPos, direction.opposite)) {
                    val x = if (direction.stepX == 0) random.nextDouble() else 0.5 + direction.stepX * 0.6
                    val y = if (direction.stepY == 0) random.nextDouble() else 0.5 + direction.stepY * 0.6
                    val z = if (direction.stepZ == 0) random.nextDouble() else 0.5 + direction.stepZ * 0.6
                    world.addParticle(ParticleTypes.DRIPPING_HONEY, pos.x + x, pos.y + y, pos.z + z, 0.0, 0.0, 0.0)
                }
            }
        }
    }

    override fun playerWillDestroy(world: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        if (state.getValue(DuskProperties.COCOON)) {
            addAbsorbtion(player)
            return state.setValue(DuskProperties.COCOON, false)
        }
        return super.playerWillDestroy(world, pos, state, player)
    }

    fun addAbsorbtion(entity: LivingEntity? = null) {
        if (entity != null) entity.absorptionAmount += 2f * (1 + 1)
        //world.setBlockState(pos, state.with(DuskProperties.COCOON, false), 2)
    }

    companion object {
        val CODEC: MapCodec<GoldenCocoonBlock> = simpleCodec(::GoldenCocoonBlock)
        const val CHANCE = 128
    }
}