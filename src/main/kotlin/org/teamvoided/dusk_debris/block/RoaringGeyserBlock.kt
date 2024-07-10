package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.LeavesBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.dusk_debris.init.DuskBlocks

class RoaringGeyserBlock(val blockAfterFinish: Block, settings: Settings) :
    Block(settings) {
    public override fun getCodec(): MapCodec<RoaringGeyserBlock> {
        return CODEC
    }

    init {
        this.defaultState =
            stateManager.defaultState
                .with(ACTIVE, false)
                .with(PERSISTENT, false)
    }
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(ACTIVE, PERSISTENT)
    }
    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val blockState = defaultState.with(PERSISTENT, true)
        return blockState
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (state.get(PERSISTENT)) {
            world.setBlockState(pos, state.with(ACTIVE, true))
            world.scheduleBlockTick(pos, state.block, random.nextInt(40))
            super.randomTick(state, world, pos, random)
        }
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (state.get(ACTIVE) && random.nextFloat() > 0.2) {
            geyser(pos, world)
            world.scheduleBlockTick(pos, state.block, random.nextInt(90) + 10)
        } else {
            if (state.get(PERSISTENT)) {
                world.setBlockState(pos, state.with(ACTIVE, false))
            } else {
                world.setBlockState(pos, blockAfterFinish.defaultState)
            }
        }
        super.scheduledTick(state, world, pos, random)
    }

    private fun geyser(pos: BlockPos, world: World) {
        val entitiesInRange = world.getOtherEntities(
            null, Box(
                pos.x - 0.5,
                pos.y + 0.5,
                pos.z - 0.5,
                pos.x + 1.5,
                pos.y + 1.5,
                pos.z + 1.5
            )
        )
//        { obj: Entity -> obj.isAlive }
        return entitiesInRange.forEach {
            val vec3d = it.velocity
            it.setVelocity(vec3d.x, vec3d.y + 1.5, vec3d.z)
        }
    }

//    private fun bounce(entity: Entity) {
//        val vec3d = entity.velocity
//        if (vec3d.y < 0.0) {
//            val d = if (entity is LivingEntity) 1.0 else 0.8
//            entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z)
//        }
//    }

    companion object {
        val CODEC: MapCodec<RoaringGeyserBlock> = createCodec { settings: Settings ->
            RoaringGeyserBlock(
                DuskBlocks.VOLCANIC_SAND,
                settings
            )
        }
        val PERSISTENT: BooleanProperty = Properties.PERSISTENT
        val ACTIVE: BooleanProperty = BooleanProperty.of("active")
    }
}