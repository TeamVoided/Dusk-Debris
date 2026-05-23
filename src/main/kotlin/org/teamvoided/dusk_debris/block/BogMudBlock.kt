/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package net.minecraft.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.EntityTypeTags
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.FallingBlockEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BucketPickup
import net.minecraft.world.level.block.LevelEvent
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.EntityCollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.*

class BogMudBlock
    (settings: Properties) : Block(settings), BucketPickup {
    public override fun codec(): MapCodec<BogMudBlock> {
        return CODEC
    }

    override fun skipRendering(state: BlockState, stateFrom: BlockState, direction: Direction): Boolean {
        if (stateFrom.`is`(this)) {
            return true
        }
        return super.skipRendering(state, stateFrom, direction)
    }

    override fun getOcclusionShape(state: BlockState, world: BlockGetter, pos: BlockPos): VoxelShape {
        return Shapes.empty()
    }

    override fun entityInside(state: BlockState, world: Level, pos: BlockPos, entity: Entity) {
        if (entity !is LivingEntity || entity.inBlockState.`is`(this)) {
            entity.makeStuckInBlock(
                state,
                Vec3(
                    HORIZONTAL_SPEED_MULTIPLIER,
                    VERTICAL_SPEED_MULTIPLIER,
                    HORIZONTAL_SPEED_MULTIPLIER
                )
            )
            if (world.isClientSide) {
                val randomGenerator = world.getRandom()
                val bl = entity.xOld != entity.x || entity.zOld != entity.z
                if (bl && randomGenerator.nextBoolean()) {
                    world.addParticle(
                        ParticleTypes.SNOWFLAKE,
                        entity.x,
                        (pos.y + 1).toDouble(),
                        entity.z,
                        (Mth.randomBetween(
                            randomGenerator,
                            -1.0f,
                            1.0f
                        ) * HORIZONTAL_PARTICLE_MOMENTUM).toDouble(),
                        0.05,
                        (Mth.randomBetween(randomGenerator, -1.0f, 1.0f) * HORIZONTAL_PARTICLE_MOMENTUM).toDouble()
                    )
                }
            }
        }
        if (!world.isClientSide) {
            if (entity.isOnFire &&
                (world.gameRules.getBoolean(GameRules.RULE_MOBGRIEFING) || entity is Player) &&
                entity.mayInteract(world, pos)
            ) {
                world.destroyBlock(pos, false)
            }
            entity.setSharedFlagOnFire(false)
        }
    }

    override fun fallOn(world: Level, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
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
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape {
        if (context is EntityCollisionContext) {
            if (context.entity != null) {
                val entity: Entity = context.entity!!
                if (entity.fallDistance > NUM_BLOCKS_TO_FALL_INTO_BLOCK) {
                    return FALLING_SHAPE
                }
                val bl = entity is FallingBlockEntity
                if (bl || canWalkOnBogMud(entity) && context.isAbove(
                        Shapes.block(),
                        pos,
                        false
                    ) && !context.isDescending()
                ) {
                    return super.getCollisionShape(state, world, pos, context)
                }
            }
        }
        return Shapes.empty()
    }

    override fun getVisualShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape {
        return Shapes.empty()
    }

    override fun pickupBlock(player: Player?, world: LevelAccessor, pos: BlockPos, state: BlockState): ItemStack {
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), UPDATE_ALL or UPDATE_IMMEDIATE)
        if (!world.isClientSide) {
            world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, getId(state))
        }
        return ItemStack(Items.POWDER_SNOW_BUCKET)
    }

    override fun getPickupSound(): Optional<SoundEvent> {
        return Optional.of(SoundEvents.BUCKET_FILL)
    }

    override fun isPathfindable(state: BlockState, navigationType: PathComputationType): Boolean {
        return true
    }

    companion object {
        val CODEC: MapCodec<BogMudBlock> = simpleCodec(::BogMudBlock)
        private const val HORIZONTAL_PARTICLE_MOMENTUM = 0.083333336f
        private const val HORIZONTAL_SPEED_MULTIPLIER = 0.2
        private const val VERTICAL_SPEED_MULTIPLIER = 2.5
        private const val NUM_BLOCKS_TO_FALL_INTO_BLOCK = 2.5f
        private val SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0)
        private val FALLING_SHAPE: VoxelShape = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.9, 1.0)
        private const val MIN_FALL_DIST_FOR_SOUND = 4.0
        private const val MIN_FALL_DIST_FOR_BIG_SOUND = 7.0

        fun canWalkOnBogMud(entity: Entity): Boolean {
            if (entity.type.`is`(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
                return true
            }
//            if (entity is LivingEntity) {
//                return entity.getEquippedStack(EquipmentSlot.FEET).isOf(Items.LEATHER_BOOTS)
//            }
            return false
        }
    }
}