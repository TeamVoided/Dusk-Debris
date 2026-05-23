package org.teamvoided.dusk_debris.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.shapes.VoxelShape
import org.teamvoided.dusk_debris.init.DuskBlockEntities
import org.teamvoided.dusk_debris.mixin.BaseEntityBlockAccessor
import org.teamvoided.dusks_and_dungeons.block.entity.HauntedGravestoneBlockEntity

open class HauntedGravestoneBlock(shape: VoxelShape, centerShape: VoxelShape, settings: Properties) :
    GravestoneBlock(shape, centerShape, settings), EntityBlock {

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(CENTERED, true)
                .setValue(FACING, Direction.NORTH)
                .setValue(IS_ACTIVE, false)
        )
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return HauntedGravestoneBlockEntity(pos, state)
    }

    override fun triggerEvent(state: BlockState?, world: Level, pos: BlockPos?, type: Int, data: Int): Boolean {
        super.triggerEvent(state, world, pos, type, data)
        val blockEntity: BlockEntity? = world.getBlockEntity(pos)
        return if (blockEntity == null) false else blockEntity.triggerEvent(type, data)
    }

    override fun <T : BlockEntity> getTicker(
        world: Level,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return BaseEntityBlockAccessor.CreateTickerHelper(
            type,
            DuskBlockEntities.HAUNTED_GRAVESTONE_BLOCK,
            HauntedGravestoneBlockEntity::serverTick
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(IS_ACTIVE)
    }

    override fun animateTick(state: BlockState, world: Level, pos: BlockPos, random: RandomSource) {
        if (state.getValue(IS_ACTIVE) && random.nextInt(25) == 0) {
            world.playLocalSound(
                pos.x + 0.5,
                pos.y + 0.5,
                pos.z + 0.5,
                SoundEvents.VEX_AMBIENT,
                SoundSource.BLOCKS,
                0.5f + random.nextFloat(),
                random.nextFloat() * 0.3f,
                false
            )
            world.addParticle(
                ParticleTypes.SOUL,
                pos.x + random.nextDouble(),
                pos.y + random.nextDouble(),
                pos.z + random.nextDouble(),
                Mth.nextDouble(random, -0.01, 0.01),
                Mth.nextDouble(random, 0.0, 0.2),
                Mth.nextDouble(random, -0.01, 0.01)
            )
        }
        super.animateTick(state, world, pos, random)
    }

    companion object {
        val IS_ACTIVE: BooleanProperty = BooleanProperty.create("is_active")
    }
}