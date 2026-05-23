package org.teamvoided.dusk_debris.util

import net.minecraft.core.Holder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.VariantHolder
import net.minecraft.world.entity.animal.sniffer.Sniffer
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.entity.helper.DuskClawStuff
import org.teamvoided.dusk_debris.entity.helper.DuskSpellStuff
import org.teamvoided.dusk_debris.entity.helper.DuskVelocityWind
import org.teamvoided.dusk_debris.entity.helper.SpellController
import org.teamvoided.dusk_debris.entity.variant.SnifferVariant

var Sniffer.variant: Holder<SnifferVariant>
    get() = (this as VariantHolder<Holder<SnifferVariant>>).getVariant()
    set(input) {
        (this as VariantHolder<Holder<SnifferVariant>>).setVariant(input)
    }

var Entity.velocityWind: Vec3
    get() = (this as DuskVelocityWind).getWind()
    set(wind) = (this as DuskVelocityWind).setWind(wind)

var Entity.hangingDirection: Vec3
    get() = (this as DuskClawStuff).getHangingDirection()
    set(direction) = (this as DuskClawStuff).setHangingDirection(direction)

var Entity.isHanging: Boolean
    get() = (this as DuskClawStuff).getHanging()
    set(lie) = (this as DuskClawStuff).setHanging(lie)

val LivingEntity.spellController: SpellController
    get() = (this as DuskSpellStuff).getSpellController()