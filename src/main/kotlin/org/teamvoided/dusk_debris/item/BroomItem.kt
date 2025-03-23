package org.teamvoided.dusk_debris.item

import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BrushableBlock
import net.minecraft.block.entity.BrushableBlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Arm
import net.minecraft.util.Hand
import net.minecraft.util.UseAction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class BroomItem(settings: Settings?) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val playerEntity = context.player
        if (playerEntity != null && getHitResult(playerEntity).type == HitResult.Type.BLOCK) {
            playerEntity.setCurrentHand(context.hand)
        }

        return ActionResult.CONSUME
    }

    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BRUSH
    override fun getUseTicks(stack: ItemStack, livingEntity: LivingEntity): Int = USE_DURATION

    override fun usageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int) {
        if (remainingUseTicks >= 0 && user is PlayerEntity) {
            val hitResult = this.getHitResult(user)
            if (hitResult is BlockHitResult) {
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    val ticks = this.getUseTicks(stack, user) - remainingUseTicks + 1
                    if (ticks % ANIMATION_DURATION == ANIMATION_INTERVAL) {
                        val blockPos: BlockPos = hitResult.blockPos
                        val blockState = world.getBlockState(blockPos)
                        val arm =
                            if (user.getActiveHand() == Hand.MAIN_HAND) user.getMainArm() else user.getMainArm().opposite
                        if (blockState.spawnsDustParticles() && blockState.renderType != BlockRenderType.INVISIBLE) {
                            this.spawnDustParticles(world, hitResult, blockState, user.getRotationVec(0.0f), arm)
                        }

                        val block = blockState.block
                        val soundEvent: SoundEvent = if (block is BrushableBlock) {
                            block.brushingSound
                        } else {
                            SoundEvents.ITEM_BRUSH_BRUSHING_GENERIC
                        }

                        world.playSound(user, blockPos, soundEvent, SoundCategory.BLOCKS)
                        if (!world.isClient()) {
                            val blockEntity = world.getBlockEntity(blockPos)
                            if (blockEntity is BrushableBlockEntity) {
                                if (blockEntity.brush(world.time, user, hitResult.side)) {
                                    val equipmentSlot =
                                        if (stack == user.getEquippedStack(EquipmentSlot.OFFHAND)) EquipmentSlot.OFFHAND else EquipmentSlot.MAINHAND
                                    stack.damageEquipment(1, user, equipmentSlot)
                                }
                            }
                        }
                    }

                    return
                }
            }
            user.stopUsingItem()
        } else {
            user.stopUsingItem()
        }
    }

    private fun getHitResult(player: PlayerEntity): HitResult {
        return ProjectileUtil.getCollision(
            player,
            { entity: Entity -> !entity.isSpectator && entity.collides() }, player.blockInteractionRange
        )
    }

    private fun spawnDustParticles(
        world: World,
        blockHitResult: BlockHitResult,
        state: BlockState,
        pos: Vec3d,
        arm: Arm
    ) {
        val velMult = 3.0
        val armDir = if (arm == Arm.RIGHT) 1 else -1
        val count = world.getRandom().range(7, 12)
        val blockStateParticleEffect = BlockStateParticleEffect(ParticleTypes.BLOCK, state)
        val direction = blockHitResult.side
        val dustParticleDelta = DustParticleDelta.create(pos, direction)
        val hitPos = blockHitResult.pos

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

            fun create(pos: Vec3d, direction: Direction?): DustParticleDelta {
                val d = 0.0
                val var10000 = when (direction) {
                    Direction.DOWN, Direction.UP -> DustParticleDelta(pos.getZ(), 0.0, -pos.getX())
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