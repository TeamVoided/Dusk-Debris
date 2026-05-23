package org.teamvoided.dusk_debris.item

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.Tool
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.util.normalizeHorizontal
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.pow

class MaceBlazeItem(settings: Properties) : Item(settings) {
    override fun canAttackBlock(state: BlockState, world: Level, pos: BlockPos, miner: Player): Boolean =
        !miner.isCreative

    override fun getEnchantmentValue(): Int = 15

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        if (attacker is ServerPlayer) {
            if (shouldSmashAttack(attacker)) {
                val serverWorld = attacker.level() as ServerLevel
                if (attacker.isIgnoringFallDamageFromCurrentImpulse && attacker.currentImpulseImpactPos != null) {
                    if (attacker.currentImpulseImpactPos!!.y > attacker.position().y) {
                        attacker.currentImpulseImpactPos = attacker.position()
                    }
                } else {
                    attacker.currentImpulseImpactPos = attacker.position()
                }

                attacker.setIgnoreFallDamageFromCurrentImpulse(true)
                val attackerVel = attacker.deltaMovement.normalizeHorizontal(0.1)
                attacker.setDeltaMovement(attacker.deltaMovement.with(Direction.Axis.Y, 0.01))
                attacker.connection.send(ClientboundSetEntityMotionPacket(attacker))
                if (target.onGround()) {
                    attacker.setSpawnExtraParticlesOnFall(true)
                    val soundEvent =
                        if (attacker.walkDist > MIN_HEAVY_SMASH_ATTACK_SPEED) SoundEvents.MACE_SMASH_GROUND_HEAVY
                        else SoundEvents.MACE_SMASH_GROUND
                    serverWorld.playSound(
                        null as Player?,
                        attacker.getX(),
                        attacker.getY(),
                        attacker.getZ(),
                        soundEvent,
                        attacker.soundSource,
                        1f,
                        0.5f
                    )
                } else {
                    serverWorld.playSound(
                        null as Player?,
                        attacker.getX(),
                        attacker.getY(),
                        attacker.getZ(),
                        SoundEvents.MACE_SMASH_AIR,
                        attacker.soundSource,
                        1f,
                        0.5f
                    )
                }

                knockbackNearbyEntities(serverWorld, attacker, target)
            }
        }

        return true
    }

    override fun postHurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND)
        if (shouldSmashAttack(attacker)) {
            attacker.resetFallDistance()
        }
    }

    override fun isValidRepairItem(stack: ItemStack, ingredient: ItemStack): Boolean = ingredient.`is`(Items.BLAZE_ROD)


    override fun getAttackDamageBonus(target: Entity, damage: Float, damageSource: DamageSource): Float {
        val source = damageSource.directEntity
        if (source is LivingEntity) {
            if (!shouldSmashAttack(source)) {
                return 0f
            } else {
                val lowLimit = 7f
                val speed: Float = source.walkDist
                val speedReturn = if (speed <= lowLimit) {
                    speed
                } else {
                    (speed + lowLimit) / 2f
                }

                val var10: Level = source.level()
                if (var10 is ServerLevel) {
                    return speedReturn + EnchantmentHelper.modifyFallBasedDamage(
                        var10,
                        source.weaponItem,
                        target,
                        damageSource,
                        0f
                    ) * speed
                } else {
                    return speedReturn
                }
            }
        } else {
            return 0f
        }
    }

    companion object {
        private const val ATTACK_DAMAGE_MODIFIER = 5.0
        private const val ATTACK_SPEED_MODIFIER = -3.4
        const val MIN_SMASH_ATTACK_SPEED: Float = 5f
        private const val MIN_HEAVY_SMASH_ATTACK_SPEED = 8f
        const val SMASH_ATTACK_RANGE: Double = 3.5
        private const val SMASH_ATTACK_KNOCKBACK_MULTIPLIER = 0.7f

        fun createAttributes(): ItemAttributeModifiers {
            return ItemAttributeModifiers.builder().add(
                Attributes.ATTACK_DAMAGE,
                AttributeModifier(
                    BASE_ATTACK_DAMAGE_ID,
                    ATTACK_DAMAGE_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            ).add(
                Attributes.ATTACK_SPEED,
                AttributeModifier(
                    BASE_ATTACK_SPEED_ID,
                    ATTACK_SPEED_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            ).build()
        }

        fun createToolComponent(): Tool {
            return Tool(listOf(), 1f, 2)
        }

        private fun knockbackNearbyEntities(world: Level, attacker: Player, target: Entity) {
            world.levelEvent(2013, target.onPos, 750)
            world.getEntitiesOfClass(
                LivingEntity::class.java,
                target.boundingBox.inflate(SMASH_ATTACK_RANGE),
                getKnockbackNearbyPredicate(attacker, target)
            ).forEach(
                Consumer { opponent: LivingEntity ->
                    val vec3d = opponent.position().subtract(target.position())
                    val magnitude = getKnockbackMagnitude(attacker, opponent, vec3d)
                    val vec3d2 = vec3d.normalize().scale(magnitude)
                    if (magnitude > 0.0) {
                        opponent.push(vec3d2.x, 0.7, vec3d2.z)
                        if (opponent is ServerPlayer) {
                            opponent.connection.send(ClientboundSetEntityMotionPacket(opponent))
                        }
                    }
                })
        }

        private fun getKnockbackNearbyPredicate(attacker: Player, target: Entity): Predicate<LivingEntity> {
            return Predicate { entity: LivingEntity ->
                var bl5: Boolean = false

                val bl: Boolean = !entity.isSpectator
                val bl2: Boolean = entity !== attacker && entity !== target
                val bl3: Boolean = !attacker.isAlliedTo(entity)
                if (entity is TamableAnimal) {
                    if (entity.isTame && attacker.uuid == entity.ownerUUID) {
                        bl5 = true
                    }
                }

                val bl4: Boolean = !bl5
                if (entity is ArmorStand) {
                    if (entity.isMarker) {
                        bl5 = false
                    }
                } else {
                    bl5 = true

                }
                val bl6 = target.distanceToSqr(entity) <= SMASH_ATTACK_RANGE.pow(2.0)
                bl && bl2 && bl3 && bl4 && bl5 && bl6
            }
        }

        private fun getKnockbackMagnitude(
            player: Player,
            opponent: LivingEntity,
            opponentToTarget: Vec3
        ): Double {
            return (3.5 - opponentToTarget.length()) *
                    0.7 *
                    (if (player.walkDist > MIN_HEAVY_SMASH_ATTACK_SPEED) 2 else 1).toDouble() *
                    (1.0 - opponent.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))
        }

        fun shouldSmashAttack(entity: LivingEntity): Boolean {
            return entity.walkDist > MIN_SMASH_ATTACK_SPEED && !entity.isFallFlying
        }
    }
}