package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskParticles

class RoaringGeyserBlock(settings: Settings) :
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
        if (ctx.world.getBlockState(ctx.blockPos.down()).isIn(DuskBlockTags.GEYSER_PERSISTANT)) {
            ctx.world.scheduleBlockTick(ctx.blockPos, this, 20)
            return defaultState.with(ACTIVE, true).with(PERSISTENT, true)
        }
        return defaultState
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (!state.get(PERSISTENT) && random.range(0, 6) == 0) {
            world.setBlockState(pos, state.with(ACTIVE, true))
            world.scheduleBlockTick(pos, state.block, random.nextInt(40))
            super.randomTick(state, world, pos, random)
        }
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: RandomGenerator) {
        if (state.get(PERSISTENT) || (state.get(ACTIVE) && random.range(0, 5) != 0)) {
            geyser(pos, world)
            world.scheduleBlockTick(pos, state.block, random.nextInt(90) + 10)
        } else {
            world.setBlockState(pos, state.with(ACTIVE, false))
        }
        super.scheduledTick(state, world, pos, random)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        if (state.get(ACTIVE)) {
            for (i in 0..random.range(3, 10))
                world.addParticle(
                    DuskParticles.GEYSER,
                    true,
                    pos.x + 0.5,
                    pos.y + 1.0,
                    pos.z + 0.5,
                    0.0,
                    0.0,
                    0.0
                )
        }
        super.randomDisplayTick(state, world, pos, random)
    }

    private fun geyser(pos: BlockPos, world: World) {
        val entitiesInRange = world.getOtherEntities(
            null, Box(
                pos.x - 0.5,
                pos.y + 1.0,
                pos.z - 0.5,
                pos.x + 1.5,
                pos.y + 2.5,
                pos.z + 1.5
            )
        ) { obj: Entity -> !obj.type.isIn(DuskEntityTypeTags.GEYSERS_DONT_PROPEL) }
        return entitiesInRange.forEach {
            println(it.type)
            val vec3d = it.velocity
            it.setVelocity(vec3d.x, vec3d.y + 1.75, vec3d.z)
            it.velocityModified = true
        }
    }

    companion object {
        val CODEC: MapCodec<RoaringGeyserBlock> = createCodec { settings: Settings ->
            RoaringGeyserBlock(
                settings
            )
        }
        val PERSISTENT: BooleanProperty = Properties.PERSISTENT
        val ACTIVE: BooleanProperty = BooleanProperty.of("active")
    }
}