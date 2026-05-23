package org.teamvoided.dusk_debris.block.throwable_bomb

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ambient.Bat
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.*
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

abstract class AbstractThrwowableBombBlock(settings: Properties) : HorizontalDirectionalBlock(settings),
    SimpleWaterloggedBlock {
    public override fun codec(): MapCodec<BlunderbombBlock> {
        return CODEC
    }

    init {
        this.registerDefaultState(
            (stateDefinition.any())
                .setValue(FACING, Direction.NORTH)
                .setValue(HANGING, false)
                .setValue(WATERLOGGED, false)
        )
    }

    open val explosionBehavior: ExplosionDamageCalculator = ExplosionDamageCalculator()
    open val explosionBehaviorOnExploded: ExplosionDamageCalculator = ExplosionDamageCalculator()

    override fun stepOn(world: Level, pos: BlockPos, state: BlockState, entity: Entity) {
        if (!entity.isSteppingCarefully) {
            this.tryBreakBomb(world, state, pos, entity, 257)
        }

        super.stepOn(world, pos, state, entity)
    }

    override fun fallOn(world: Level, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        this.tryBreakBomb(world, state, pos, entity, 4)
        super.fallOn(world, state, pos, entity, fallDistance)
    }

    override fun onProjectileHit(world: Level, state: BlockState, hit: BlockHitResult, projectile: Projectile) {
        val blockPos = hit.blockPos
        if (!world.isClientSide && projectile.mayInteract(world, blockPos) && projectile.mayBreak(world)) {
            this.explode(world, blockPos, explosionBehavior)
            projectile.kill()
        }
    }


    override fun wasExploded(world: Level, pos: BlockPos, explosion: Explosion) {
        if (!world.isClientSide) this.explode(world, pos, explosionBehaviorOnExploded)
        return super.wasExploded(world, pos, explosion)
    }

    private fun breaksBomb(world: Level, entity: Entity): Boolean {
        return if (entity !is Bat) {
            if (entity !is LivingEntity) {
                false
            } else {
                entity is Player || world.gameRules.getBoolean(GameRules.RULE_MOBGRIEFING)
            }
        } else {
            false
        }
    }

    private fun tryBreakBomb(world: Level, state: BlockState, pos: BlockPos, entity: Entity, inverseChance: Int) {
        if (this.breaksBomb(world, entity)) {
            if (!world.isClientSide && world.random.nextInt(inverseChance) == 0 && state.`is`(this)) {
                this.explode(world, pos, explosionBehavior)
            }
        }
    }

    open fun explode(world: Level, pos: BlockPos, explosionBehavior: ExplosionDamageCalculator) {
        println("this should not occur, please check that you override the explosion function")
    }

    override fun dropFromExplosion(explosion: Explosion): Boolean {
        return false
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world))
        }
        return if (attachedDirection(state).opposite == direction && !state.canSurvive(
                world,
                pos
            )
        ) Blocks.AIR.defaultBlockState()
        else super.updateShape(
            state,
            direction,
            neighborState,
            world,
            pos,
            neighborPos
        )
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
        val fluidState = ctx.level.getFluidState(ctx.clickedPos)
        val var3 = ctx.nearestLookingDirections
        val var4 = var3.size

        for (var5 in 0 until var4) {
            val direction = var3[var5]
            if (direction.axis === Direction.Axis.Y) {
                val blockState =
                    defaultBlockState()
                        .setValue(HANGING, direction == Direction.UP)
                        .setValue(FACING, ctx.horizontalDirection.opposite)
                if (blockState.canSurvive(ctx.level, ctx.clickedPos)) {
                    return blockState.setValue(WATERLOGGED, fluidState.type === Fluids.WATER)
                }
            }
        }
        return null
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(LanternBlock.WATERLOGGED)) Fluids.WATER.getSource(false)
        else super.getFluidState(state)
    }

    override fun canSurvive(state: BlockState, world: LevelReader, pos: BlockPos): Boolean {
        val direction = attachedDirection(state).opposite
        return canSupportCenter(world, pos.relative(direction), direction.opposite)
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return if (state.getValue(HANGING)) HANGING_SHAPE else STANDING_SHAPE
    }

    override fun getOcclusionShape(state: BlockState, world: BlockGetter, pos: BlockPos): VoxelShape {
        return Shapes.empty()
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, HANGING, WATERLOGGED)
    }


    companion object {
        val CODEC: MapCodec<BlunderbombBlock> = simpleCodec { settings: Properties ->
            BlunderbombBlock(
                settings
            )
        }
        private val STANDING_SHAPE: VoxelShape = box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0)
        private val HANGING_SHAPE: VoxelShape = box(5.0, 4.0, 5.0, 11.0, 10.0, 11.0)
        val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
        val HANGING: BooleanProperty = BlockStateProperties.HANGING
        val WATERLOGGED: BooleanProperty = BlockStateProperties.WATERLOGGED
        const val DEFAULT_EXPLOSION_POWER = 3f

        protected fun attachedDirection(state: BlockState): Direction {
            return if (state.getValue(HANGING)) Direction.DOWN else Direction.UP
        }
    }
}