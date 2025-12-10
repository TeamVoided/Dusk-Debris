package org.teamvoided.dusk_debris.init

import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.id

object DuskSoundEvents {
//    val EVENT_MOB_EFFECT_RAID_OMEN = register("event.mob_effect.raid_omen")
//    val ENTITY_GENERIC_EXPLODE = registerHolder("entity.generic.explode")

    val BLOCK_ORGAN_NOTE = register("block.exhaust.organ_note")
    val BLOCK_EXHAUST_ANTICIPATION = register("block.exhaust.anticipation")
    val BLOCK_EXHAUST_ATTACK = register("block.exhaust.attack")

    val BLOCK_NETHERSHROOM_EXPLODE = register("block.nethershroom.explode")
    val BLOCK_NETHERSHROOM_SQUISHED = register("block.nethershroom.squished")
    val BLOCK_NETHERSHROOM_BLOCK_SQUISHED = register("block.nethershroom_block.squished")
    val BLOCK_GUNPOWDER_BARREL_EXPLODE = registerHolder("block.gunpowder_barrel.explode")

    val BLOCK_CELESTAL_BELL_USE = register("block.celestal_bell.use")

    fun init() {
    }

    private fun register(id: String): SoundEvent = register(id(id))
    private fun register(id: Identifier): SoundEvent = register(id, id)
    private fun register(id: Identifier, soundId: Identifier): SoundEvent =
        Registry.register(Registries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId))
    @Suppress("SameParameterValue")
    private fun registerHolder(id: String): Holder.Reference<SoundEvent> = registerHolder(id(id))
    private fun registerHolder(id: Identifier): Holder.Reference<SoundEvent> = registerHolder(id, id)
    private fun registerHolder(id: Identifier, soundId: Identifier): Holder.Reference<SoundEvent> =
        Registry.registerHolder(Registries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId))

    private fun registerHolder(id: Identifier, soundId: Identifier, range: Float): Holder<SoundEvent> =
        Registry.registerHolder(Registries.SOUND_EVENT, id, SoundEvent.createFixedRangeEvent(soundId, range))
}