package org.teamvoided.dusk_debris.block

import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.random.RandomGenerator
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.World
import org.teamvoided.dusks_and_dungeons.block.entity.HauntedGravestoneBlockEntity
import org.teamvoided.dusks_and_dungeons.init.DnDBlockEntities
import org.teamvoided.dusks_and_dungeons.mixin.BlockWithEntityAccessor

open class HauntedGravestoneBlock(shape: VoxelShape, centerShape: VoxelShape, settings: Settings) :
    GravestoneBlock(shape, centerShape, settings), BlockEntityProvider {

    init {
        defaultState = stateManager.defaultState
            .with(Properties.WATERLOGGED, false)
            .with(CENTERED, true)
            .with(FACING, Direction.NORTH)
            .with(IS_ACTIVE, false)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return HauntedGravestoneBlockEntity(pos, state)
    }

    override fun onSyncedBlockEvent(state: BlockState?, world: World, pos: BlockPos?, type: Int, data: Int): Boolean {
        super.onSyncedBlockEvent(state, world, pos, type, data)
        val blockEntity: BlockEntity? = world.getBlockEntity(pos)
        return if (blockEntity == null) false else blockEntity.onSyncedBlockEvent(type, data)
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return BlockWithEntityAccessor.checkType(
            type,
            DnDBlockEntities.HAUNTED_GRAVESTONE_BLOCK,
            HauntedGravestoneBlockEntity::serverTick
        )
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(IS_ACTIVE)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        if (state.get(IS_ACTIVE) && random.nextInt(25) == 0) {
            world.playSound(
                pos.x + 0.5,
                pos.y + 0.5,
                pos.z + 0.5,
                SoundEvents.ENTITY_VEX_AMBIENT,
                SoundCategory.BLOCKS,
                0.5f + random.nextFloat(),
                random.nextFloat() * 0.3f,
                false
            )
            world.addParticle(
                ParticleTypes.SOUL,
                pos.x + random.nextDouble(),
                pos.y + random.nextDouble(),
                pos.z + random.nextDouble(),
                MathHelper.nextDouble(random, -0.01, 0.01),
                MathHelper.nextDouble(random, 0.0, 0.2),
                MathHelper.nextDouble(random, -0.01, 0.01)
            )
        }
        super.randomDisplayTick(state, world, pos, random)
    }

    companion object {
        val IS_ACTIVE: BooleanProperty = BooleanProperty.of("is_active")
    }
}