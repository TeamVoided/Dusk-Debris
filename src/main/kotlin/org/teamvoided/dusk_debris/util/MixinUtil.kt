package org.teamvoided.dusk_debris.util

import net.minecraft.entity.Entity
import net.minecraft.entity.VariantProvider
import net.minecraft.entity.passive.SnifferEntity
import net.minecraft.registry.Holder
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.entity.helper.VelocityWind
import org.teamvoided.dusk_debris.entity.sniffer.SnifferVariant

var SnifferEntity.variant: Holder<SnifferVariant>
    get() = (this as VariantProvider<Holder<SnifferVariant>>).getVariant()
    set(input) {
        (this as VariantProvider<Holder<SnifferVariant>>).setVariant(input)
    }

var Entity.velocityWind: Vec3d
    get() = (this as VelocityWind).getWind()
    set(wind) = (this as VelocityWind).setWind(wind)