package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ItemEnchantmentsComponent
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.provider.EnchantmentProviders
import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.goal.SkeletonHorseTrapTriggerGoal
import net.minecraft.entity.mob.WitherSkeletonEntity
import net.minecraft.entity.passive.AbstractHorseEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.LocalDifficulty
import org.teamvoided.dusk_debris.entity.WitherSkeletonHorseEntity
import org.teamvoided.dusk_debris.init.DuskEntities

class WitherSkeletonHorseTrapTriggerGoal(witherSkeletonHorse: WitherSkeletonHorseEntity) : SkeletonHorseTrapTriggerGoal(
    witherSkeletonHorse
) {


    override fun tick() {
        val serverWorld = skeletonHorse.world as ServerWorld
        val localDifficulty = serverWorld.getLocalDifficulty(skeletonHorse.blockPos)
        skeletonHorse.isTrapped = false
        skeletonHorse.isTame = true
        skeletonHorse.breedingAge = 0
        val lightningEntity = EntityType.LIGHTNING_BOLT.create(serverWorld)
        if (lightningEntity != null) {
            lightningEntity.refreshPositionAfterTeleport(
                skeletonHorse.x,
                skeletonHorse.y, skeletonHorse.z
            )
            lightningEntity.setCosmetic(true)
            serverWorld.spawnEntity(lightningEntity)
            val skeletonEntity = this.getWitherSkeleton(localDifficulty, this.skeletonHorse)
            if (skeletonEntity != null) {
                skeletonEntity.startRiding(this.skeletonHorse)
                serverWorld.spawnEntityAndPassengers(skeletonEntity)
                for (i in 0..2) {
                    val abstractHorseEntity = this.getWitherHorse(localDifficulty)
                    if (abstractHorseEntity != null) {
                        val skeletonEntity2 = this.getWitherSkeleton(localDifficulty, abstractHorseEntity)
                        if (skeletonEntity2 != null) {
                            skeletonEntity2.startRiding(abstractHorseEntity)
                            abstractHorseEntity.addVelocity(
                                skeletonHorse.getRandom().nextTriangular(0.0, 1.1485), 0.0,
                                skeletonHorse.getRandom().nextTriangular(0.0, 1.1485)
                            )
                            serverWorld.spawnEntityAndPassengers(abstractHorseEntity)
                        }
                    }
                }
            }
        }
    }

    fun getWitherHorse(localDifficulty: LocalDifficulty): AbstractHorseEntity? {
        val skeletonHorseEntity = DuskEntities.WITHER_SKELETON_HORSE.create(skeletonHorse.world)
        if (skeletonHorseEntity != null) {
            skeletonHorseEntity.initialize(
                skeletonHorse.world as ServerWorld,
                localDifficulty,
                SpawnReason.TRIGGERED,
                null as EntityData?
            )
            skeletonHorseEntity.setPosition(
                skeletonHorse.x,
                skeletonHorse.y,
                skeletonHorse.z
            )
            skeletonHorseEntity.timeUntilRegen = 60
            skeletonHorseEntity.setPersistent()
            skeletonHorseEntity.isTame = true
            skeletonHorseEntity.breedingAge = 0
        }

        return skeletonHorseEntity
    }

    fun getWitherSkeleton(localDifficulty: LocalDifficulty, vehicle: AbstractHorseEntity): WitherSkeletonEntity? {
        val witherSkeletonEntity = EntityType.WITHER_SKELETON.create(vehicle.world)
        if (witherSkeletonEntity != null) {
            witherSkeletonEntity.initialize(
                vehicle.world as ServerWorld,
                localDifficulty,
                SpawnReason.TRIGGERED,
                null as EntityData?
            )
            witherSkeletonEntity.setPosition(vehicle.x, vehicle.y, vehicle.z)
            witherSkeletonEntity.timeUntilRegen = 60
            witherSkeletonEntity.setPersistent()
            if (witherSkeletonEntity.getEquippedStack(EquipmentSlot.HEAD).isEmpty) {
                witherSkeletonEntity.equipStack(EquipmentSlot.HEAD, ItemStack(Items.IRON_HELMET))
            }
            this.enchantHelmetAndSword(witherSkeletonEntity, EquipmentSlot.MAINHAND, localDifficulty)
            this.enchantHelmetAndSword(witherSkeletonEntity, EquipmentSlot.HEAD, localDifficulty)
        }

        return witherSkeletonEntity
    }

    fun enchantHelmetAndSword(
        witherSkeleton: WitherSkeletonEntity,
        equipmentSlot: EquipmentSlot,
        difficulty: LocalDifficulty
    ) {
        val itemStack = witherSkeleton.getEquippedStack(equipmentSlot)
        itemStack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT)
        EnchantmentHelper.enchantFromProvider(
            itemStack,
            witherSkeleton.world.registryManager,
            EnchantmentProviders.MOB_SPAWN_EQUIPMENT,
            difficulty,
            witherSkeleton.getRandom()
        )
        witherSkeleton.equipStack(equipmentSlot, itemStack)
    }
}