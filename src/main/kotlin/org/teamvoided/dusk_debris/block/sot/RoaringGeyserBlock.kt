package org.teamvoided.dusk_debris.block.sot

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.util.spawnParticles

class RoaringGeyserBlock(settings: Properties) :
    Block(settings) {
    public override fun codec(): MapCodec<RoaringGeyserBlock> {
        return CODEC
    }

    init {
        this.registerDefaultState(
            stateDefinition.any()
                .setValue(ACTIVE, false)
                .setValue(PERSISTENT, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(ACTIVE, PERSISTENT)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        if (ctx.level.getBlockState(ctx.clickedPos.below()).`is`(DuskBlockTags.GEYSER_PERSISTANT)) {
            ctx.level.scheduleTick(ctx.clickedPos, this, 20)
            return defaultBlockState().setValue(ACTIVE, true).setValue(PERSISTENT, true)
        }
        return defaultBlockState()
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (world.getBlockState(pos.below()).`is`(DuskBlockTags.GEYSER_PERSISTANT)) {
            world.scheduleTick(pos, this, 20)
            return state.setValue(PERSISTENT, true).setValue(ACTIVE, true)
        }
        return state.setValue(PERSISTENT, false)
    }

    override fun randomTick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (!state.getValue(PERSISTENT) && random.nextInt(0, 8) == 0) {
            world.setBlockAndUpdate(pos, state.setValue(ACTIVE, true))
            world.scheduleTick(pos, state.block, random.nextInt(100))
            super.randomTick(state, world, pos, random)
        }
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(PERSISTENT) || (state.getValue(ACTIVE) && random.nextInt(5) != 0)) {
            geyser(pos, world)
            world.scheduleTick(pos, state.block, random.nextInt(130) + 10)
        } else {
            world.setBlockAndUpdate(pos, state.setValue(ACTIVE, false))
        }
        super.tick(state, world, pos, random)
    }

    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        if (state.getValue(ACTIVE)) {
            repeat(random.nextInt(5, 13)) {
                world.addParticle(
                    DuskParticles.GEYSER,
                    true,
                    pos.x + 0.5,
                    pos.y + 1.0,
                    pos.z + 0.5,
                    (random.nextDouble() - random.nextDouble()) * 0.1,
                    random.nextDouble() * 0.75,
                    (random.nextDouble() - random.nextDouble()) * 0.1
                )
            }
        }
        super.animateTick(state, world, pos, random)
    }

    private fun geyser(pos: BlockPos, world: ServerLevel) {
        val random = world.random
        repeat(random.nextInt(10, 23)) {
            world.spawnParticles(
                DuskParticles.GEYSER,
                pos.above().bottomCenter,
                Vec3(
                    (random.nextDouble() - random.nextDouble()) * 0.15,
                    random.nextDouble() + 0.6,
                    (random.nextDouble() - random.nextDouble()) * 0.15
                )
            )
        }
        val entitiesInRange = world.getEntities(
            null, AABB(
                pos.x - 0.5,
                pos.y + 1.0,
                pos.z - 0.5,
                pos.x + 1.5,
                pos.y + 2.5,
                pos.z + 1.5
            )
        ) { obj: Entity -> !obj.type.`is`(DuskEntityTypeTags.GEYSERS_DONT_PROPEL) }
        return entitiesInRange.forEach {
            val vec3d = it.deltaMovement
            it.setDeltaMovement(vec3d.x, vec3d.y + 1.75, vec3d.z)
            it.hurtMarked = true
        }
    }

    companion object {
        val CODEC: MapCodec<RoaringGeyserBlock> = simpleCodec { settings: Properties ->
            RoaringGeyserBlock(
                settings
            )
        }
        val PERSISTENT: BooleanProperty = BlockStateProperties.PERSISTENT
        val ACTIVE: BooleanProperty = DuskProperties.ACTIVE
    }
}