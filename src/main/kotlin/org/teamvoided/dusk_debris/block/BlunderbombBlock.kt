package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.BatEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.*
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.BlunderbombEntity
import org.teamvoided.dusk_debris.world.explosion.SpecialExplosionBehavior

open class BlunderbombBlock(settings: Settings) : Block(settings), Waterloggable {
    public override fun getCodec(): MapCodec<BlunderbombBlock> {
        return CODEC
    }

    init {
        this.defaultState =
            (stateManager.defaultState).with(HANGING, false).with(WATERLOGGED, false)
    }

    open val explosionBehavior: ExplosionBehavior = SpecialExplosionBehavior(
        DuskBlockTags.BLUNDERBOMB_DESTROYS,
        DuskEntityTypeTags.BLUNDERBOMB_DOES_NOT_DAMAGE,
        1.1f,
        1f
    )

    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        if (!entity.bypassesSteppingEffects()) {
            this.tryBreakBomb(world, state, pos, entity, 100)
        }

        super.onSteppedOn(world, pos, state, entity)
    }

    override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        this.tryBreakBomb(world, state, pos, entity, 3)
        super.onLandedUpon(world, state, pos, entity, fallDistance)
    }

    override fun onProjectileHit(world: World, state: BlockState, hit: BlockHitResult, projectile: ProjectileEntity) {
        val blockPos = hit.blockPos
        if (!world.isClient && projectile.canModifyAt(world, blockPos) && projectile.canBreakBlocks(world)) {
            this.explode(world, blockPos, explosionBehavior)
            projectile.kill()
        }
    }


    override fun onDestroyedByExplosion(world: World, pos: BlockPos, explosion: Explosion) {
        if (!world.isClient) this.explode(world, pos, explosionBehaviorOnExploded)
        return super.onDestroyedByExplosion(world, pos, explosion)
    }

    private fun breaksBomb(world: World, entity: Entity): Boolean {
        return if (entity !is BatEntity) {
            if (entity !is LivingEntity) {
                false
            } else {
                entity is PlayerEntity || world.gameRules.getBooleanValue(GameRules.DO_MOB_GRIEFING)
            }
        } else {
            false
        }
    }

    private fun tryBreakBomb(world: World, state: BlockState, pos: BlockPos, entity: Entity, inverseChance: Int) {
        if (this.breaksBomb(world, entity)) {
            if (!world.isClient && world.random.nextInt(inverseChance) == 0 && state.isOf(this)) {
                this.explode(world, pos, explosionBehavior)
            }
        }
    }

    open fun explode(world: World, pos: BlockPos, explosionBehavior: ExplosionBehavior) {
        world.playSound(
            null,
            pos,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundCategory.BLOCKS,
            0.7f,
            0.9f + world.random.nextFloat() * 0.2f
        )
        world.breakBlock(pos, false)
        super.spawnBreakParticles(world, null, pos, this.defaultState)
        world.createExplosion(
            null,
            Explosion.createDamageSource(
                world,
                null
            ), explosionBehavior,
            pos.x + 0.5,
            pos.y + 0.5,
            pos.z + 0.5,
            DEFAULT_EXPLOSION_POWER,
            false,
            World.ExplosionSourceType.TNT,
            ParticleTypes.BUBBLE,
            ParticleTypes.BUBBLE,
            SoundEvents.ENTITY_GENERIC_EXPLODE
        )
    }

    override fun shouldDropItemsOnExplosion(explosion: Explosion): Boolean {
        return false
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        return if (attachedDirection(state).opposite == direction && !state.canPlaceAt(
                world,
                pos
            )
        ) Blocks.AIR.defaultState
        else super.getStateForNeighborUpdate(
            state,
            direction,
            neighborState,
            world,
            pos,
            neighborPos
        )
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        val var3 = ctx.placementDirections
        val var4 = var3.size

        for (var5 in 0 until var4) {
            val direction = var3[var5]
            if (direction.axis === Direction.Axis.Y) {
                val blockState =
                    defaultState.with(HANGING, direction == Direction.UP)
                if (blockState.canPlaceAt(ctx.world, ctx.blockPos)) {
                    return blockState.with(WATERLOGGED, fluidState.fluid === Fluids.WATER)
                }
            }
        }
        return null
    }

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val direction = attachedDirection(state).opposite
        return sideCoversSmallSquare(world, pos.offset(direction), direction.opposite)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return if (state.get(HANGING)) HANGING_SHAPE else STANDING_SHAPE
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(HANGING, WATERLOGGED)
    }


    companion object {
        val CODEC: MapCodec<BlunderbombBlock> = createCodec { settings: Settings ->
            BlunderbombBlock(
                settings
            )
        }
        private val STANDING_SHAPE: VoxelShape = createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0)
        private val HANGING_SHAPE: VoxelShape = createCuboidShape(4.0, 3.0, 4.0, 12.0, 11.0, 12.0)
        val HANGING: BooleanProperty = Properties.HANGING
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        const val DEFAULT_EXPLOSION_POWER = 2f

        protected fun attachedDirection(state: BlockState): Direction {
            return if (state.get(HANGING)) Direction.DOWN else Direction.UP
        }

        private val randomNumber = (Math.random() * Math.random()).toFloat()
        val explosionBehaviorOnExploded: SpecialExplosionBehavior = SpecialExplosionBehavior(
            DuskBlockTags.BLUNDERBOMB_DESTROYS,
            DuskEntityTypeTags.BLUNDERBOMB_DOES_NOT_DAMAGE,
            randomNumber,
            randomNumber / 3
        )
    }
}