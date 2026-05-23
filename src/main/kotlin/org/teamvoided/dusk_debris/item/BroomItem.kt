package org.teamvoided.dusk_debris.item

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BrushableBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BrushableBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class BroomItem(settings: Properties?) : Item(settings) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val playerEntity = context.player
        if (playerEntity != null && getHitResult(playerEntity).type == HitResult.Type.BLOCK) {
            playerEntity.startUsingItem(context.hand)
        }

        return InteractionResult.CONSUME
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.BRUSH
    override fun getUseDuration(stack: ItemStack, livingEntity: LivingEntity): Int = USE_DURATION

    override fun onUseTick(world: Level, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        if (remainingUseTicks >= 0 && user is Player) {
            val hitResult = this.getHitResult(user)
            if (hitResult is BlockHitResult) {
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    val ticks = this.getUseDuration(stack, user) - remainingUseTicks + 1
                    if (ticks % ANIMATION_DURATION == ANIMATION_INTERVAL) {
                        val blockPos: BlockPos = hitResult.blockPos
                        val blockState = world.getBlockState(blockPos)
                        val arm =
                            if (user.usedItemHand == InteractionHand.MAIN_HAND) user.getMainArm() else user.getMainArm().opposite
                        if (blockState.shouldSpawnTerrainParticles() && blockState.renderShape != RenderShape.INVISIBLE) {
                            this.spawnDustParticles(world, hitResult, blockState, user.getViewVector(0.0f), arm)
                        }

                        val block = blockState.block
                        val soundEvent: SoundEvent = if (block is BrushableBlock) {
                            block.brushSound
                        } else {
                            SoundEvents.BRUSH_GENERIC
                        }

                        world.playSound(user, blockPos, soundEvent, SoundSource.BLOCKS)
                        if (!world.isClientSide) {
                            val blockEntity = world.getBlockEntity(blockPos)
                            if (blockEntity is BrushableBlockEntity) {
                                if (blockEntity.brush(world.gameTime, user, hitResult.direction)) {
                                    val equipmentSlot =
                                        if (stack == user.getItemBySlot(EquipmentSlot.OFFHAND)) EquipmentSlot.OFFHAND else EquipmentSlot.MAINHAND
                                    stack.hurtAndBreak(1, user, equipmentSlot)
                                }
                            }
                        }
                    }

                    return
                }
            }
            user.releaseUsingItem()
        } else {
            user.releaseUsingItem()
        }
    }

    private fun getHitResult(player: Player): HitResult {
        return ProjectileUtil.getHitResultOnViewVector(
            player,
            { entity: Entity -> !entity.isSpectator && entity.isPickable }, player.blockInteractionRange()
        )
    }

    private fun spawnDustParticles(
        world: Level,
        blockHitResult: BlockHitResult,
        state: BlockState,
        pos: Vec3,
        arm: HumanoidArm
    ) {
        val velMult = 3.0
        val armDir = if (arm == HumanoidArm.RIGHT) 1 else -1
        val count = world.getRandom().nextInt(7, 12)
        val blockStateParticleEffect = BlockParticleOption(ParticleTypes.BLOCK, state)
        val direction = blockHitResult.direction
        val dustParticleDelta = DustParticleDelta.create(pos, direction)
        val hitPos = blockHitResult.location

        for (k in 0 until count) {
            world.addParticle(
                blockStateParticleEffect,
                hitPos.x - (if (direction == Direction.WEST) 1.0E-6 else 0.0),
                hitPos.y,
                hitPos.z - (if (direction == Direction.NORTH) 1.0E-6 else 0.0),
                dustParticleDelta.xd * armDir.toDouble() * velMult * world.getRandom().nextDouble(),
                0.0,
                dustParticleDelta.zd * armDir.toDouble() * velMult * world.getRandom().nextDouble()
            )
        }
    }

    private data class DustParticleDelta(val xd: Double, val yd: Double, val zd: Double) {
        companion object {
            private const val ALONG_SIDE_DELTA = 1.0
            private const val OUT_FROM_SIDE_DELTA = 0.1

            fun create(pos: Vec3, direction: Direction?): DustParticleDelta {
                val d = 0.0
                val var10000 = when (direction) {
                    Direction.DOWN, Direction.UP -> DustParticleDelta(pos.z(), 0.0, -pos.x())
                    Direction.NORTH -> DustParticleDelta(1.0, 0.0, -0.1)
                    Direction.SOUTH -> DustParticleDelta(-1.0, 0.0, 0.1)
                    Direction.WEST -> DustParticleDelta(-0.1, 0.0, -1.0)
                    Direction.EAST -> DustParticleDelta(0.1, 0.0, 1.0)
                    else -> throw MatchException(null as String?, null as Throwable?)
                }
                return var10000
            }
        }
    }

    companion object {
        const val ANIMATION_DURATION: Int = 10
        const val ANIMATION_INTERVAL: Int = 5
        private const val USE_DURATION = 200
    }
}