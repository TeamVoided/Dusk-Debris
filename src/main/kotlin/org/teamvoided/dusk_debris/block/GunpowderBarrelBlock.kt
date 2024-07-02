package org.teamvoided.dusk_debris.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CrafterBlock
import net.minecraft.block.enums.JigsawOrientation
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.ItemInteractionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import net.minecraft.world.explosion.Explosion
import org.teamvoided.dusk_debris.entity.GunpowderBarrelEntity

class GunpowderBarrelBlock(val power: Int = 5, val knockbackMultiplier: Float = 1f, val color: Int = 0xffffff, settings: Settings) :
    Block(settings) {

    public override fun getCodec(): MapCodec<GunpowderBarrelBlock> {
        return CODEC
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        if (!oldState.isOf(state.block)) {
            if (world.isReceivingRedstonePower(pos)) {
                primeGunpowderBarrel(world, pos)
                world.removeBlock(pos, false)
            }
        }
    }

    override fun neighborUpdate(
        state: BlockState,
        world: World,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        notify: Boolean
    ) {
        if (world.isReceivingRedstonePower(pos)) {
            primeGunpowderBarrel(world, pos)
            world.removeBlock(pos, false)
        }
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): BlockState {
        if (!world.isClient() && !player.isCreative && state.get(UNSTABLE)) {
            primeGunpowderBarrel(world, pos)
        }

        return super.onBreak(world, pos, state, player)
    }

    override fun onDestroyedByExplosion(world: World, pos: BlockPos, explosion: Explosion) {
        if (!world.isClient) {
            val gunpowderBarrelEntity = GunpowderBarrelEntity(
                world,
                pos.x.toDouble() + 0.5,
                pos.y.toDouble(),
                pos.z.toDouble() + 0.5,
                explosion.causingEntity
            )
            gunpowderBarrelEntity.setProperties(
                this.power,
                this.knockbackMultiplier,
                this.defaultState,
                this.color
            )
            val fuse = gunpowderBarrelEntity.fuse
            gunpowderBarrelEntity.fuse = (world.random.nextInt(fuse / 5) + fuse / 10).toShort().toInt()
            world.spawnEntity(gunpowderBarrelEntity)
        }
    }

    override fun onInteract(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: PlayerEntity,
        hand: Hand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (!stack.isOf(Items.FLINT_AND_STEEL) && !stack.isOf(Items.FIRE_CHARGE)) {
            return super.onInteract(stack, state, world, pos, entity, hand, hitResult)
        } else {
            primeGunpowderBarrel(world, pos, entity)
            world.setBlockState(pos, Blocks.AIR.defaultState, 11)
            val item = stack.item
            if (stack.isOf(Items.FLINT_AND_STEEL)) {
                stack.damageEquipment(1, entity, LivingEntity.getHand(hand))
            } else {
                stack.consume(1, entity)
            }

            entity.incrementStat(Stats.USED.getOrCreateStat(item))
            return ItemInteractionResult.success(world.isClient)
        }
    }

    override fun onProjectileHit(world: World, state: BlockState, hit: BlockHitResult, projectile: ProjectileEntity) {
        if (!world.isClient) {
            val blockPos = hit.blockPos
            val entity = projectile.owner
            if (projectile.isOnFire && projectile.canModifyAt(world, blockPos)) {
                primeGunpowderBarrel(world, blockPos, if (entity is LivingEntity) entity else null)
                world.removeBlock(blockPos, false)
            }
        }
    }

    override fun shouldDropItemsOnExplosion(explosion: Explosion): Boolean {
        return false
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(UNSTABLE, ORIENTATION)
    }

    init {
        this.defaultState = defaultState
            .with(UNSTABLE, false)
            .with(ORIENTATION, JigsawOrientation.NORTH_UP)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val direction = ctx.playerLookDirection.opposite
        val var10000 = when (direction) {
            Direction.DOWN -> ctx.playerFacing.opposite
            Direction.UP -> ctx.playerFacing
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST -> Direction.UP
            else -> throw MatchException(null as String?, null as Throwable?)
        }
        return defaultState.with(
            ORIENTATION,
            JigsawOrientation.byDirections(direction, var10000)
        )
    }

    companion object {
        val CODEC: MapCodec<GunpowderBarrelBlock> = createCodec { settings: Settings ->
            GunpowderBarrelBlock(
                5,
                1f,
                0xffffff,
                settings
            )
        }

        val ORIENTATION: EnumProperty<JigsawOrientation> = Properties.ORIENTATION
        val UNSTABLE: BooleanProperty = Properties.UNSTABLE

        fun primeGunpowderBarrel(world: World, pos: BlockPos) {
            primeGunpowderBarrel(world, pos, null)
        }

        private fun primeGunpowderBarrel(world: World, pos: BlockPos, igniter: LivingEntity?) {
            if (!world.isClient) {
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
                    (world.getBlockState(pos).block as GunpowderBarrelBlock).knockbackMultiplier,
                    world.getBlockState(pos),
                    (world.getBlockState(pos).block as GunpowderBarrelBlock).color,
                )
                world.spawnEntity(gunpowderBarrelEntity)
                world.playSound(
                    null,
                    gunpowderBarrelEntity.x,
                    gunpowderBarrelEntity.y,
                    gunpowderBarrelEntity.z,
                    SoundEvents.ENTITY_TNT_PRIMED,
                    SoundCategory.BLOCKS,
                    1.0f,
                    1.0f
                )
                world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos)
            }
        }
    }
}