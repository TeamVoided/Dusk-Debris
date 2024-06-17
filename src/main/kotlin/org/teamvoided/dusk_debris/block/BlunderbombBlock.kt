//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package net.minecraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.entity.passive.BatEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
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
import org.teamvoided.dusk_debris.entity.GunpowderBarrelEntity
import java.util.*

class BlunderbombBlock(settings: Settings) : Block(settings), Waterloggable {
    public override fun getCodec(): MapCodec<BlunderbombBlock> {
        return CODEC
    }

    init {
        this.defaultState =
            (stateManager.defaultState).with(HANGING, false).with(WATERLOGGED, false)
    }

    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        if (!entity.bypassesSteppingEffects()) {
            this.tryBreakBomb(world, state, pos, entity, 100)
        }

        super.onSteppedOn(world, pos, state, entity)
    }

    override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        if (entity !is ZombieEntity) {
            this.tryBreakBomb(world, state, pos, entity, 3)
        }

        super.onLandedUpon(world, state, pos, entity, fallDistance)
    }

    override fun onProjectileHit(world: World, state: BlockState, hit: BlockHitResult, projectile: ProjectileEntity) {
        val blockPos = hit.blockPos
        if (!world.isClient && projectile.canModifyAt(world, blockPos) && projectile.canBreakBlocks(world)) {
            this.explodeBomb(world, blockPos, defaultExplosionPower)
        }
    }


    override fun onDestroyedByExplosion(world: World, pos: BlockPos, explosion: Explosion) {
        val randomChance = world.getRandom().nextInt(10)
        if (randomChance > 3) this.explodeBomb(
            world,
            pos,
            defaultExplosionPower / randomChance
        )
        return super.onDestroyedByExplosion(world, pos, explosion)
    }

    private fun tryBreakBomb(world: World, state: BlockState, pos: BlockPos, entity: Entity, inverseChance: Int) {
        if (this.breaksBomb(world, entity)) {
            if (!world.isClient && world.random.nextInt(inverseChance) == 0 && state.isOf(this)) {
                this.explodeBomb(world, pos, defaultExplosionPower)
            }
        }
    }

    private fun explodeBomb(world: World, pos: BlockPos, explosionPower: Float) {
        world.playSound(
            null,
            pos,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundCategory.BLOCKS,
            0.7f,
            0.9f + world.random.nextFloat() * 0.2f
        )
        world.breakBlock(pos, false)
        world.createExplosion(
            null,
            Explosion.createDamageSource(
                world,
                null
            ), blockDestroyer,
            pos.x + 0.5, pos.y + 0.5, pos.z + 0.5,
            explosionPower, false, World.ExplosionSourceType.TNT
        )
    }

    override fun shouldDropItemsOnExplosion(explosion: Explosion): Boolean {
        return false
    }


    override fun afterBreak(
        world: World,
        player: PlayerEntity,
        pos: BlockPos,
        state: BlockState,
        blockEntity: BlockEntity?,
        stack: ItemStack
    ) {
        super.afterBreak(world, player, pos, state, blockEntity, stack)
        this.explodeBomb(world, pos, defaultExplosionPower)
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
        private val defaultExplosionPower = 2f

        protected fun attachedDirection(state: BlockState): Direction {
            return if (state.get(HANGING)) Direction.DOWN else Direction.UP
        }

        private val blockDestroyer: ExplosionBehavior = object : ExplosionBehavior() {
            override fun canDestroyBlock(
                explosion: Explosion,
                world: BlockView,
                pos: BlockPos,
                state: BlockState,
                power: Float
            ): Boolean {
                return if (!state.isIn(DuskBlockTags.BLUNDERBOMB_DESTROYS)) false else super.canDestroyBlock(
                    explosion,
                    world,
                    pos,
                    state,
                    power
                )
            }

            override fun getBlastResistance(
                explosion: Explosion,
                world: BlockView,
                pos: BlockPos,
                blockState: BlockState,
                fluidState: FluidState
            ): Optional<Float> {
                return if (!blockState.isIn(DuskBlockTags.BLUNDERBOMB_DESTROYS)) Optional.empty() else super.getBlastResistance(
                    explosion,
                    world,
                    pos,
                    blockState,
                    fluidState
                )
            }
        }
    }
}