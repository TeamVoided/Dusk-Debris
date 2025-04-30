package org.teamvoided.dusk_debris.util

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.VariantProvider
import net.minecraft.entity.passive.SnifferEntity
import net.minecraft.registry.Holder
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.entity.helper.DuskClawStuff
import org.teamvoided.dusk_debris.entity.helper.DuskSpellStuff
import org.teamvoided.dusk_debris.entity.helper.DuskVelocityWind
import org.teamvoided.dusk_debris.entity.helper.SpellController
import org.teamvoided.dusk_debris.entity.variant.SnifferVariant

var SnifferEntity.variant: Holder<SnifferVariant>
    get() = (this as VariantProvider<Holder<SnifferVariant>>).getVariant()
    set(input) {
        (this as VariantProvider<Holder<SnifferVariant>>).setVariant(input)
    }

var Entity.velocityWind: Vec3d
    get() = (this as DuskVelocityWind).getWind()
    set(wind) = (this as DuskVelocityWind).setWind(wind)

var Entity.hangingDirection: Vec3d
    get() = (this as DuskClawStuff).getHangingDirection()
    set(direction) = (this as DuskClawStuff).setHangingDirection(direction)

var Entity.isHanging: Boolean
    get() = (this as DuskClawStuff).getHanging()
    set(lie) = (this as DuskClawStuff).setHanging(lie)

val LivingEntity.spellController: SpellController
    get() = (this as DuskSpellStuff).getSpellController()