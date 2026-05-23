package org.teamvoided.dusk_debris.entity

import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import java.util.*

interface Pickupable {
    var placed: Boolean

    fun copyDataToStack(stack: ItemStack)

    fun copyDataFromNbt(nbt: CompoundTag)

    val pickupItem: ItemStack

    val pickupSound: SoundEvent?

    companion object {
        fun copyDataToStack(entity: Mob, stack: ItemStack) {
            stack.set(DataComponents.CUSTOM_NAME, entity.customName)
            CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack) { nbtCompound: CompoundTag ->
                if (entity.isNoAi) {
                    nbtCompound.putBoolean("NoAI", entity.isNoAi)
                }
                if (entity.isSilent) {
                    nbtCompound.putBoolean("Silent", entity.isSilent)
                }

                if (entity.isNoGravity) {
                    nbtCompound.putBoolean("NoGravity", entity.isNoGravity)
                }

                if (entity.hasGlowingTag()) {
                    nbtCompound.putBoolean("Glowing", entity.hasGlowingTag())
                }

                if (entity.isInvulnerable) {
                    nbtCompound.putBoolean("Invulnerable", entity.isInvulnerable)
                }
                nbtCompound.putFloat("Health", entity.health)
            }
        }

        fun copyDataFromNbt(entity: Mob, nbt: CompoundTag) {
            if (nbt.contains("NoAI")) {
                entity.setNoAi(nbt.getBoolean("NoAI"))
            }

            if (nbt.contains("Silent")) {
                entity.isSilent = nbt.getBoolean("Silent")
            }

            if (nbt.contains("NoGravity")) {
                entity.setNoGravity(nbt.getBoolean("NoGravity"))
            }

            if (nbt.contains("Glowing")) {
                entity.setGlowingTag(nbt.getBoolean("Glowing"))
            }

            if (nbt.contains("Invulnerable")) {
                entity.isInvulnerable = nbt.getBoolean("Invulnerable")
            }

            if (nbt.contains("Health", 99)) {
                entity.health = nbt.getFloat("Health")
            }
        }

        fun <T> tryPickup(
            player: Player,
            hand: InteractionHand,
            entity: T
        ): Optional<InteractionResult> where T : LivingEntity, T : Pickupable {
            val handStack = player.getItemInHand(hand)
            if (handStack.isEmpty && entity.isAlive) {
                entity.playSound((entity as Pickupable).pickupSound, 1f, 1f)
                val pickupItem = (entity as Pickupable).pickupItem
                (entity as Pickupable).copyDataToStack(pickupItem)
                player.setItemInHand(hand, pickupItem)
                val world = entity.level()

                /*advancement*/
//                if (!world.isClient) {
//                    Criteria.FILLED_BUCKET.trigger(player as ServerPlayerEntity, pickupItem)
//                }

                entity.discard()
                return Optional.of(InteractionResult.sidedSuccess(world.isClientSide))
            } else {
                return Optional.empty()
            }
        }
    }
}