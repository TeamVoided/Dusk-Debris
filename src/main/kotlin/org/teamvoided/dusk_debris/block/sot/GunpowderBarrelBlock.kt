package org.teamvoided.dusk_debris.block.sot

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.FrontAndTop
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.BlockHitResult
import org.teamvoided.dusk_debris.data.tags.DuskItemTags
import org.teamvoided.dusk_debris.entity.GunpowderBarrelEntity

class GunpowderBarrelBlock(val power: Int = 5, val range: Int = 4, val color: Int = 0xffffff, settings: Properties) :
    Block(settings) {

    public override fun codec(): MapCodec<GunpowderBarrelBlock> {
        return CODEC
    }

    override fun onPlace(state: BlockState, world: Level, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (!oldState.`is`(state.block)) {
            if (world.hasNeighborSignal(pos)) {
                primeGunpowderBarrel(world, pos)
                world.removeBlock(pos, false)
            }
        }
    }

    override fun neighborChanged(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        notify: Boolean
    ) {
        if (world.hasNeighborSignal(pos)) {
            primeGunpowderBarrel(world, pos)
            world.removeBlock(pos, false)
        }
    }

    override fun playerWillDestroy(world: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        if (!world.isClientSide && !player.isCreative && state.getValue(UNSTABLE)) {
            primeGunpowderBarrel(world, pos)
        }

        return super.playerWillDestroy(world, pos, state, player)
    }

    override fun wasExploded(world: Level, pos: BlockPos, explosion: Explosion) {
        if (!world.isClientSide) {
            val gunpowderBarrelEntity = GunpowderBarrelEntity(
                world,
                pos.x.toDouble() + 0.5,
                pos.y.toDouble(),
                pos.z.toDouble() + 0.5,
                explosion.indirectSourceEntity
            )
            gunpowderBarrelEntity.setProperties(
                this.power,
                this.range,
                this.defaultBlockState(),
                this.color
            )
            val fuse = gunpowderBarrelEntity.fuse
            gunpowderBarrelEntity.fuse = (world.random.nextInt(fuse / 5) + fuse / 10).toShort().toInt()
            world.addFreshEntity(gunpowderBarrelEntity)
        }
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (!stack.`is`(DuskItemTags.IGNITES_GUNPOWDER)) {
            return super.useItemOn(stack, state, world, pos, entity, hand, hitResult)
        } else {
            primeGunpowderBarrel(world, pos, entity)
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 11)
            val item = stack.item
            if (stack.isDamageableItem) {
                stack.hurtAndBreak(1, entity, LivingEntity.getSlotForHand(hand))
            } else {
                stack.consume(1, entity)
            }

            entity.awardStat(Stats.ITEM_USED.get(item))
            return ItemInteractionResult.sidedSuccess(world.isClientSide)
        }
    }

    override fun onProjectileHit(world: Level, state: BlockState, hit: BlockHitResult, projectile: Projectile) {
        if (!world.isClientSide) {
            val blockPos = hit.blockPos
            val entity = projectile.owner
            if (projectile.isOnFire && projectile.mayInteract(world, blockPos)) {
                primeGunpowderBarrel(world, blockPos, if (entity is LivingEntity) entity else null)
                world.removeBlock(blockPos, false)
            }
        }
    }

    override fun dropFromExplosion(explosion: Explosion): Boolean {
        return false
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(UNSTABLE, ORIENTATION)
    }

    init {
        this.registerDefaultState(
            defaultBlockState()
                .setValue(UNSTABLE, false)
                .setValue(ORIENTATION, FrontAndTop.NORTH_UP)
        )
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        val direction = ctx.nearestLookingDirection.opposite
        val var10000 = when (direction) {
            Direction.DOWN -> ctx.horizontalDirection.opposite
            Direction.UP -> ctx.horizontalDirection
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST -> Direction.UP
            else -> throw MatchException(null as String?, null as Throwable?)
        }
        return defaultBlockState().setValue(
            ORIENTATION,
            FrontAndTop.fromFrontAndTop(direction, var10000)
        )
    }

    companion object {
        val CODEC: MapCodec<GunpowderBarrelBlock> = simpleCodec { settings: Properties ->
            GunpowderBarrelBlock(
                5,
                1,
                0xffffff,
                settings
            )
        }

        val ORIENTATION: EnumProperty<FrontAndTop> = BlockStateProperties.ORIENTATION
        val UNSTABLE: BooleanProperty = BlockStateProperties.UNSTABLE

        fun primeGunpowderBarrel(world: Level, pos: BlockPos) {
            primeGunpowderBarrel(world, pos, null)
        }

        private fun primeGunpowderBarrel(world: Level, pos: BlockPos, igniter: LivingEntity?) {
            if (!world.isClientSide) {
                val gunpowderBarrelEntity =
                    GunpowderBarrelEntity(
                        world,
                        pos.x.toDouble() + 0.5,
                        pos.y.toDouble(),
                        pos.z.toDouble() + 0.5,
                        igniter
                    )
                gunpowderBarrelEntity.setProperties(
                    (world.getBlockState(pos).block as GunpowderBarrelBlock).power,
                    (world.getBlockState(pos).block as GunpowderBarrelBlock).range,
                    world.getBlockState(pos),
                    (world.getBlockState(pos).block as GunpowderBarrelBlock).color,
                )
                world.addFreshEntity(gunpowderBarrelEntity)
                world.playSound(
                    null,
                    gunpowderBarrelEntity.x,
                    gunpowderBarrelEntity.y,
                    gunpowderBarrelEntity.z,
                    SoundEvents.TNT_PRIMED,
                    SoundSource.BLOCKS,
                    1.0f,
                    1.0f
                )
                world.gameEvent(igniter, GameEvent.PRIME_FUSE, pos)
            }
        }
    }
}