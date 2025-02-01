package org.teamvoided.dusk_debris.entity

import net.minecraft.advancement.criterion.Criteria
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvent
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import java.util.*

interface Pickupable {
    var placed: Boolean

    fun copyDataToStack(stack: ItemStack)

    fun copyDataFromNbt(nbt: NbtCompound)

    val pickupItem: ItemStack

    val pickupSound: SoundEvent?

    companion object {
        fun copyDataToStack(entity: MobEntity, stack: ItemStack) {
            stack.set(DataComponentTypes.CUSTOM_NAME, entity.customName)
            NbtComponent.set(DataComponentTypes.BUCKET_ENTITY_DATA, stack) { nbtCompound: NbtCompound ->
                if (entity.isAiDisabled) {
                    nbtCompound.putBoolean("NoAI", entity.isAiDisabled)
                }
                if (entity.isSilent) {
                    nbtCompound.putBoolean("Silent", entity.isSilent)
                }

                if (entity.hasNoGravity()) {
                    nbtCompound.putBoolean("NoGravity", entity.hasNoGravity())
                }

                if (entity.isGlowingLocal) {
                    nbtCompound.putBoolean("Glowing", entity.isGlowingLocal)
                }

                if (entity.isInvulnerable) {
                    nbtCompound.putBoolean("Invulnerable", entity.isInvulnerable)
                }
                nbtCompound.putFloat("Health", entity.health)
            }
        }

        fun copyDataFromNbt(entity: MobEntity, nbt: NbtCompound) {
            if (nbt.contains("NoAI")) {
                entity.isAiDisabled = nbt.getBoolean("NoAI")
            }

            if (nbt.contains("Silent")) {
                entity.isSilent = nbt.getBoolean("Silent")
            }

            if (nbt.contains("NoGravity")) {
                entity.setNoGravity(nbt.getBoolean("NoGravity"))
            }

            if (nbt.contains("Glowing")) {
                entity.isGlowing = nbt.getBoolean("Glowing")
            }

            if (nbt.contains("Invulnerable")) {
                entity.isInvulnerable = nbt.getBoolean("Invulnerable")
            }

            if (nbt.contains("Health", 99)) {
                entity.health = nbt.getFloat("Health")
            }
        }

        fun <T> tryPickup(
            player: PlayerEntity,
            hand: Hand?,
            entity: T
        ): Optional<ActionResult> where T : LivingEntity, T : Pickupable {
            val handStack = player.getStackInHand(hand)
            if (handStack.isEmpty && entity.isAlive) {
                entity.playSound((entity as Pickupable).pickupSound, 1f, 1f)
                val pickupItem = (entity as Pickupable).pickupItem
                (entity as Pickupable).copyDataToStack(pickupItem)
                player.setStackInHand(hand, pickupItem)
                val world = entity.world

                /*advancement*/
//                if (!world.isClient) {
//                    Criteria.FILLED_BUCKET.trigger(player as ServerPlayerEntity, pickupItem)
//                }

                entity.discard()
                return Optional.of(ActionResult.success(world.isClient))
            } else {
                return Optional.empty()
            }
        }
    }
}