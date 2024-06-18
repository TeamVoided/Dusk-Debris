package org.teamvoided.dusk_debris.data

import net.minecraft.registry.*
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.data.gen.SoundEvents
import org.teamvoided.dusk_debris.util.Bootstrapper

object DuskSoundEvents : Bootstrapper<SoundEvent>(RegistryKeys.SOUND_EVENT) {
    val EVENT_MOB_EFFECT_RAID_OMEN = register("event.mob_effect.raid_omen", ::createSoundEvent)
    fun createSoundEvent(bootstrapContext: BootstrapContext<SoundEvent>): SoundEvent {
        return SoundEvent.createVariableRangeEvent(Identifier.of("dusk_debris","event/entity/explode"))
    }
}