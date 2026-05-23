package org.teamvoided.dusk_debris.entity.ai.goal

import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.animal.horse.AbstractHorse
import net.minecraft.world.entity.animal.horse.SkeletonTrapGoal
import net.minecraft.world.entity.monster.WitherSkeleton
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders
import org.teamvoided.dusk_debris.entity.WitherSkeletonHorseEntity
import org.teamvoided.dusk_debris.init.DuskEntities

class WitherSkeletonHorseTrapTriggerGoal(witherSkeletonHorse: WitherSkeletonHorseEntity) : SkeletonTrapGoal(
    witherSkeletonHorse
) {


    override fun tick() {
        val serverWorld = horse.level() as ServerLevel
        val localDifficulty = serverWorld.getCurrentDifficultyAt(horse.blockPosition())
        horse.setTrap(false)
        horse.setTamed(true)
        horse.setAge(0)
        val lightningEntity = EntityType.LIGHTNING_BOLT.create(serverWorld)
        if (lightningEntity != null) {
            lightningEntity.moveTo(
                horse.x,
                horse.y, horse.z
            )
            lightningEntity.setVisualOnly(true)
            serverWorld.addFreshEntity(lightningEntity)
            val skeletonEntity = this.getWitherSkeleton(localDifficulty, this.horse)
            if (skeletonEntity != null) {
                skeletonEntity.startRiding(this.horse)
                serverWorld.addFreshEntityWithPassengers(skeletonEntity)
                for (i in 0..2) {
                    val abstractHorseEntity = this.getWitherHorse(localDifficulty)
                    if (abstractHorseEntity != null) {
                        val skeletonEntity2 = this.getWitherSkeleton(localDifficulty, abstractHorseEntity)
                        if (skeletonEntity2 != null) {
                            skeletonEntity2.startRiding(abstractHorseEntity)
                            abstractHorseEntity.push(
                                horse.getRandom().triangle(0.0, 1.1485), 0.0,
                                horse.getRandom().triangle(0.0, 1.1485)
                            )
                            serverWorld.addFreshEntityWithPassengers(abstractHorseEntity)
                        }
                    }
                }
            }
        }
    }

    fun getWitherHorse(localDifficulty: DifficultyInstance): AbstractHorse? {
        val skeletonHorseEntity = DuskEntities.WITHER_SKELETON_HORSE.create(horse.level())
        if (skeletonHorseEntity != null) {
            skeletonHorseEntity.finalizeSpawn(
                horse.level() as ServerLevel,
                localDifficulty,
                MobSpawnType.TRIGGERED,
                null as SpawnGroupData?
            )
            skeletonHorseEntity.setPos(
                horse.x,
                horse.y,
                horse.z
            )
            skeletonHorseEntity.invulnerableTime = 60
            skeletonHorseEntity.setPersistenceRequired()
            skeletonHorseEntity.setTamed(true)
            skeletonHorseEntity.setAge(0)
        }

        return skeletonHorseEntity
    }

    fun getWitherSkeleton(localDifficulty: DifficultyInstance, vehicle: AbstractHorse): WitherSkeleton? {
        val witherSkeletonEntity = EntityType.WITHER_SKELETON.create(vehicle.level())
        if (witherSkeletonEntity != null) {
            witherSkeletonEntity.finalizeSpawn(
                vehicle.level() as ServerLevel,
                localDifficulty,
                MobSpawnType.TRIGGERED,
                null as SpawnGroupData?
            )
            witherSkeletonEntity.setPos(vehicle.x, vehicle.y, vehicle.z)
            witherSkeletonEntity.invulnerableTime = 60
            witherSkeletonEntity.setPersistenceRequired()
            if (witherSkeletonEntity.getItemBySlot(EquipmentSlot.HEAD).isEmpty) {
                witherSkeletonEntity.setItemSlot(EquipmentSlot.HEAD, ItemStack(Items.IRON_HELMET))
            }
            this.enchantHelmetAndSword(witherSkeletonEntity, EquipmentSlot.MAINHAND, localDifficulty)
            this.enchantHelmetAndSword(witherSkeletonEntity, EquipmentSlot.HEAD, localDifficulty)
        }

        return witherSkeletonEntity
    }

    fun enchantHelmetAndSword(
        witherSkeleton: WitherSkeleton,
        equipmentSlot: EquipmentSlot,
        difficulty: DifficultyInstance
    ) {
        val itemStack = witherSkeleton.getItemBySlot(equipmentSlot)
        itemStack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        EnchantmentHelper.enchantItemFromProvider(
            itemStack,
            witherSkeleton.level().registryAccess(),
            VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT,
            difficulty,
            witherSkeleton.getRandom()
        )
        witherSkeleton.setItemSlot(equipmentSlot, itemStack)
    }
}