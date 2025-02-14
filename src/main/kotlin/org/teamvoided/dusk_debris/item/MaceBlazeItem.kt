package org.teamvoided.dusk_debris.item

import net.minecraft.block.BlockState
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.ToolComponent
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.EquipmentSlotGroup
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.dusk_debris.util.normalizeHorizontal
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.math.pow

class MaceBlazeItem(settings: Settings) : Item(settings) {
    override fun canMine(state: BlockState, world: World, pos: BlockPos, miner: PlayerEntity): Boolean =
        !miner.isCreative

    override fun getEnchantability(): Int = 15

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        if (attacker is ServerPlayerEntity) {
            if (shouldSmashAttack(attacker)) {
                val serverWorld = attacker.getWorld() as ServerWorld
                if (attacker.shouldIgnoreFallDamageFromCurrentExplosion() && attacker.currentExplosionImpactPos != null) {
                    if (attacker.currentExplosionImpactPos!!.y > attacker.getPos().y) {
                        attacker.currentExplosionImpactPos = attacker.getPos()
                    }
                } else {
                    attacker.currentExplosionImpactPos = attacker.getPos()
                }

                attacker.setIgnoreFallDamageFromCurrentExplosion(true)
                val attackerVel = attacker.getVelocity().normalizeHorizontal(0.1)
                attacker.setVelocity(attacker.getVelocity().withAxis(Direction.Axis.Y, 0.01))
                attacker.networkHandler.send(EntityVelocityUpdateS2CPacket(attacker))
                if (target.isOnGround) {
                    attacker.setSpawnExtraParticlesOnFall(true)
                    val soundEvent =
                        if (attacker.horizontalSpeed > MIN_HEAVY_SMASH_ATTACK_SPEED) SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY
                        else SoundEvents.ITEM_MACE_SMASH_GROUND
                    serverWorld.playSound(
                        null as PlayerEntity?,
                        attacker.getX(),
                        attacker.getY(),
                        attacker.getZ(),
                        soundEvent,
                        attacker.getSoundCategory(),
                        1f,
                        0.5f
                    )
                } else {
                    serverWorld.playSound(
                        null as PlayerEntity?,
                        attacker.getX(),
                        attacker.getY(),
                        attacker.getZ(),
                        SoundEvents.ITEM_MACE_SMASH_AIR,
                        attacker.getSoundCategory(),
                        1f,
                        0.5f
                    )
                }

                knockbackNearbyEntities(serverWorld, attacker, target)
            }
        }

        return true
    }

    override fun postDamageEntity(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        stack.damageEquipment(1, attacker, EquipmentSlot.MAINHAND)
        if (shouldSmashAttack(attacker)) {
            attacker.resetFallDistance()
        }
    }

    override fun canRepair(stack: ItemStack, ingredient: ItemStack): Boolean = ingredient.isOf(Items.BLAZE_ROD)


    override fun getAttackDamage(target: Entity, damage: Float, damageSource: DamageSource): Float {
        val source = damageSource.source
        if (source is LivingEntity) {
            if (!shouldSmashAttack(source)) {
                return 0f
            } else {
                val lowLimit = 7f
                val speed: Float = source.horizontalSpeed
                val speedReturn = if (speed <= lowLimit) {
                    speed
                } else {
                    (speed + lowLimit) / 2f
                }

                val var10: World = source.getWorld()
                if (var10 is ServerWorld) {
                    return speedReturn + EnchantmentHelper.getFallBasedDamage(
                        var10,
                        source.weaponStack,
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

        fun createAttributes(): AttributeModifiersComponent {
            return AttributeModifiersComponent.builder().add(
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                EntityAttributeModifier(
                    BASE_ATTACK_DAMAGE,
                    ATTACK_DAMAGE_MODIFIER,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            ).add(
                EntityAttributes.GENERIC_ATTACK_SPEED,
                EntityAttributeModifier(
                    BASE_ATTACK_SPEED,
                    ATTACK_SPEED_MODIFIER,
                    EntityAttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            ).build()
        }

        fun createToolComponent(): ToolComponent {
            return ToolComponent(listOf(), 1f, 2)
        }

        private fun knockbackNearbyEntities(world: World, attacker: PlayerEntity, target: Entity) {
            world.syncWorldEvent(2013, target.steppingPosition, 750)
            world.getEntitiesByClass(
                LivingEntity::class.java,
                target.bounds.expand(SMASH_ATTACK_RANGE),
                getKnockbackNearbyPredicate(attacker, target)
            ).forEach(
                Consumer { opponent: LivingEntity ->
                    val vec3d = opponent.pos.subtract(target.pos)
                    val magnitude = getKnockbackMagnitude(attacker, opponent, vec3d)
                    val vec3d2 = vec3d.normalize().multiply(magnitude)
                    if (magnitude > 0.0) {
                        opponent.addVelocity(vec3d2.x, 0.7, vec3d2.z)
                        if (opponent is ServerPlayerEntity) {
                            opponent.networkHandler.send(EntityVelocityUpdateS2CPacket(opponent))
                        }
                    }
                })
        }

        private fun getKnockbackNearbyPredicate(attacker: PlayerEntity, target: Entity): Predicate<LivingEntity> {
            return Predicate { entity: LivingEntity ->
                var bl5: Boolean = false

                val bl: Boolean = !entity.isSpectator
                val bl2: Boolean = entity !== attacker && entity !== target
                val bl3: Boolean = !attacker.isTeammate(entity)
                if (entity is TameableEntity) {
                    if (entity.isTamed && attacker.uuid == entity.ownerUuid) {
                        bl5 = true
                    }
                }

                val bl4: Boolean = !bl5
                if (entity is ArmorStandEntity) {
                    if (entity.isMarker) {
                        bl5 = false
                    }
                } else {
                    bl5 = true

                }
                val bl6 = target.squaredDistanceTo(entity) <= SMASH_ATTACK_RANGE.pow(2.0)
                bl && bl2 && bl3 && bl4 && bl5 && bl6
            }
        }

        private fun getKnockbackMagnitude(
            player: PlayerEntity,
            opponent: LivingEntity,
            opponentToTarget: Vec3d
        ): Double {
            return (3.5 - opponentToTarget.length()) *
                    0.7 *
                    (if (player.horizontalSpeed > MIN_HEAVY_SMASH_ATTACK_SPEED) 2 else 1).toDouble() *
                    (1.0 - opponent.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))
        }

        fun shouldSmashAttack(entity: LivingEntity): Boolean {
            return entity.horizontalSpeed > MIN_SMASH_ATTACK_SPEED && !entity.isFallFlying
        }
    }
}