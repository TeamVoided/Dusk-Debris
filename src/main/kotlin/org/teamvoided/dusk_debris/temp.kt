//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package net.minecraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.block.pattern.BlockPattern
import net.minecraft.block.pattern.BlockPatternBuilder
import net.minecraft.block.pattern.CachedBlockPosition
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.item.ItemPlacementContext
import net.minecraft.predicate.block.BlockStatePredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Property
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.WorldView
import java.util.function.Predicate

open class CarvedPumpkinBlock(settings: Settings?) : HorizontalFacingBlock(settings) {
    private var snowGolemDispenserPattern: BlockPattern? = null
        get() {
            if (field == null) {
                field = BlockPatternBuilder.start().aisle(*arrayOf(" ", "#", "#"))
                    .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
                    .build()
            }

            return field
        }
    private var snowGolemPattern: BlockPattern? = null
        get() {
            if (field == null) {
                field = BlockPatternBuilder.start().aisle(*arrayOf("^", "#", "#")).where(
                    '^', CachedBlockPosition.matchesBlockState(
                        IS_GOLEM_HEAD_PREDICATE
                    )
                ).where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK)))
                    .build()
            }

            return field
        }
    private var ironGolemDispenserPattern: BlockPattern? = null
        get() {
            if (field == null) {
                field = BlockPatternBuilder.start().aisle(*arrayOf("~ ~", "###", "~#~"))
                    .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
                    .where(
                        '~'
                    ) { block: CachedBlockPosition ->
                        block.blockState.isAir
                    }.build()
            }

            return field
        }
    private var ironGolemPattern: BlockPattern? = null
        get() {
            if (field == null) {
                field = BlockPatternBuilder.start().aisle(*arrayOf("~^~", "###", "~#~")).where(
                    '^', CachedBlockPosition.matchesBlockState(
                        IS_GOLEM_HEAD_PREDICATE
                    )
                ).where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
                    .where(
                        '~'
                    ) { block: CachedBlockPosition ->
                        block.blockState.isAir
                    }.build()
            }

            return field
        }

    public override fun getCodec(): MapCodec<out CarvedPumpkinBlock> {
        return CODEC
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (!oldState.isOf(state.block)) {
            this.trySpawnEntity(world, pos)
        }
    }

    fun canDispense(world: WorldView?, pos: BlockPos?): Boolean {
        return snowGolemDispenserPattern!!.searchAround(world, pos) != null || ironGolemDispenserPattern!!.searchAround(
            world,
            pos
        ) != null
    }

    private fun trySpawnEntity(world: World, pos: BlockPos) {
        val result = snowGolemPattern!!.searchAround(world, pos)
        if (result != null) {
            val snowGolemEntity = EntityType.SNOW_GOLEM.create(world)
            if (snowGolemEntity != null) {
                spawnGolem(world, result, snowGolemEntity, result.translate(0, 2, 0).blockPos)
            }
        } else {
            val result2 = ironGolemPattern!!.searchAround(world, pos)
            if (result2 != null) {
                val ironGolemEntity = EntityType.IRON_GOLEM.create(world)
                if (ironGolemEntity != null) {
                    ironGolemEntity.isPlayerCreated = true
                    spawnGolem(world, result2, ironGolemEntity, result2.translate(1, 2, 0).blockPos)
                }
            }
        }
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return defaultState.with(FACING, ctx.playerFacing.opposite) as BlockState
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(*arrayOf<Property<*>>(FACING))
    }

    init {
        this.defaultState = (stateManager.defaultState as BlockState).with(FACING, Direction.NORTH) as BlockState
    }

    companion object {
        val CODEC: MapCodec<CarvedPumpkinBlock> = createCodec { settings: Settings? ->
            CarvedPumpkinBlock(
                settings
            )
        }
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        private val IS_GOLEM_HEAD_PREDICATE =
            Predicate { state: BlockState? ->
                state != null && (state.isOf(
                    Blocks.CARVED_PUMPKIN
                ) || state.isOf(Blocks.JACK_O_LANTERN))
            }

        private fun spawnGolem(world: World, result: BlockPattern.Result, entity: Entity, pos: BlockPos) {
            replaceBlocks(world, result)
            entity.refreshPositionAndAngles(
                pos.x.toDouble() + 0.5,
                pos.y.toDouble() + 0.05,
                pos.z.toDouble() + 0.5,
                0.0f,
                0.0f
            )
            world.spawnEntity(entity)
            val var4: Iterator<*> = world.getNonSpectatingEntities(
                ServerPlayerEntity::class.java, entity.bounds.expand(5.0)
            ).iterator()

            while (var4.hasNext()) {
                val serverPlayerEntity = var4.next() as ServerPlayerEntity
                Criteria.SUMMONED_ENTITY.trigger(serverPlayerEntity, entity)
            }

            updateNeighbors(world, result)
        }

        fun replaceBlocks(world: World, result: BlockPattern.Result) {
            for (i in 0 until result.width) {
                for (j in 0 until result.height) {
                    val cachedBlockPosition = result.translate(i, j, 0)
                    world.setBlockState(cachedBlockPosition.blockPos, Blocks.AIR.defaultState, 2)
                    world.syncWorldEvent(
                        2001,
                        cachedBlockPosition.blockPos,
                        getRawIdFromState(cachedBlockPosition.blockState)
                    )
                }
            }
        }

        fun updateNeighbors(world: World, result: BlockPattern.Result) {
            for (i in 0 until result.width) {
                for (j in 0 until result.height) {
                    val cachedBlockPosition = result.translate(i, j, 0)
                    world.updateNeighbors(cachedBlockPosition.blockPos, Blocks.AIR)
                }
            }
        }
    }
}