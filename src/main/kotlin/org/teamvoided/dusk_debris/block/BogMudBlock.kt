/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package net.minecraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.entity.Entity
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.EntityTypeTags
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.*
import java.util.*

class BogMudBlock
    (settings: Settings) : Block(settings), FluidDrainable {
    public override fun getCodec(): MapCodec<BogMudBlock> {
        return CODEC
    }

    override fun isSideInvisible(state: BlockState, stateFrom: BlockState, direction: Direction): Boolean {
        if (stateFrom.isOf(this)) {
            return true
        }
        return super.isSideInvisible(state, stateFrom, direction)
    }

    override fun getCullingShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape {
        return VoxelShapes.empty()
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        if (entity !is LivingEntity || entity.getBlockStateAtPos().isOf(this)) {
            entity.setMovementMultiplier(
                state,
                Vec3d(
                    HORIZONTAL_SPEED_MULTIPLIER,
                    VERTICAL_SPEED_MULTIPLIER,
                    HORIZONTAL_SPEED_MULTIPLIER
                )
            )
            if (world.isClient) {
                val randomGenerator = world.getRandom()
                val bl = entity.lastRenderX != entity.x || entity.lastRenderZ != entity.z
                if (bl && randomGenerator.nextBoolean()) {
                    world.addParticle(
                        ParticleTypes.SNOWFLAKE,
                        entity.x,
                        (pos.y + 1).toDouble(),
                        entity.z,
                        (MathHelper.nextBetween(
                            randomGenerator,
                            -1.0f,
                            1.0f
                        ) * HORIZONTAL_PARTICLE_MOMENTUM).toDouble(),
                        0.05,
                        (MathHelper.nextBetween(randomGenerator, -1.0f, 1.0f) * HORIZONTAL_PARTICLE_MOMENTUM).toDouble()
                    )
                }
            }
        }
        if (!world.isClient) {
            if (entity.isOnFire &&
                (world.gameRules.getBooleanValue(GameRules.DO_MOB_GRIEFING) || entity is PlayerEntity) &&
                entity.canModifyAt(world, pos)
            ) {
                world.breakBlock(pos, false)
            }
            entity.isOnFire = false
        }
    }

    override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        if (fallDistance.toDouble() < MIN_FALL_DIST_FOR_SOUND || entity !is LivingEntity) {
            return
        }
        val fallSounds = entity.fallSounds
        val soundEvent =
            if (fallDistance.toDouble() < MIN_FALL_DIST_FOR_BIG_SOUND) fallSounds.small() else fallSounds.big()
        entity.playSound(soundEvent, 1.0f, 1.0f)
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        if (context is EntityShapeContext) {
            if (context.entity != null) {
                val entity: Entity = context.entity!!
                if (entity.fallDistance > NUM_BLOCKS_TO_FALL_INTO_BLOCK) {
                    return FALLING_SHAPE
                }
                val bl = entity is FallingBlockEntity
                if (bl || canWalkOnBogMud(entity) && context.isAbove(
                        VoxelShapes.fullCube(),
                        pos,
                        false
                    ) && !context.isDescending()
                ) {
                    return super.getCollisionShape(state, world, pos, context)
                }
            }
        }
        return VoxelShapes.empty()
    }

    override fun getCameraCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return VoxelShapes.empty()
    }

    override fun tryDrainFluid(player: PlayerEntity?, world: WorldAccess, pos: BlockPos, state: BlockState): ItemStack {
        world.setBlockState(pos, Blocks.AIR.defaultState, NOTIFY_ALL or REDRAW_ON_MAIN_THREAD)
        if (!world.isClient) {
            world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, getRawIdFromState(state))
        }
        return ItemStack(Items.POWDER_SNOW_BUCKET)
    }

    override fun getBucketFillSound(): Optional<SoundEvent> {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL)
    }

    override fun canPathfindThrough(state: BlockState, navigationType: NavigationType): Boolean {
        return true
    }

    companion object {
        val CODEC: MapCodec<BogMudBlock> = createCodec { settings: Settings ->
            BogMudBlock(
                settings
            )
        }
        private const val HORIZONTAL_PARTICLE_MOMENTUM = 0.083333336f
        private const val HORIZONTAL_SPEED_MULTIPLIER = 0.2
        private const val VERTICAL_SPEED_MULTIPLIER = 2.5
        private const val NUM_BLOCKS_TO_FALL_INTO_BLOCK = 2.5f
        private val SHAPE: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0)
        private val FALLING_SHAPE: VoxelShape = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.9, 1.0)
        private const val MIN_FALL_DIST_FOR_SOUND = 4.0
        private const val MIN_FALL_DIST_FOR_BIG_SOUND = 7.0

        fun canWalkOnBogMud(entity: Entity): Boolean {
            if (entity.type.isIn(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
                return true
            }
//            if (entity is LivingEntity) {
//                return entity.getEquippedStack(EquipmentSlot.FEET).isOf(Items.LEATHER_BOOTS)
//            }
            return false
        }
    }
}