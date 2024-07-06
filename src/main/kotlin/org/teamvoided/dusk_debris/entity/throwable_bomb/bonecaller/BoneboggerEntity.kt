package org.teamvoided.dusk_debris.entity.throwable_bomb.bonecaller

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.throwable_bomb.BonecallerEntity
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.particle.BonecallerParticleEffect
import java.awt.Color

open class BoneboggerEntity : BonecallerEntity {

    constructor(entityType: EntityType<out BoneboggerEntity>, world: World) : super(entityType, world)

    constructor(world: World) : super(DuskEntities.BONEBOGGER, world)
    constructor(owner: LivingEntity?, world: World) :
            super(DuskEntities.BONEBOGGER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: World) :
            super(DuskEntities.BONEBOGGER, x, y, z, world)

    override fun getCalledEntity() = EntityType.BOGGED
    override fun getDefaultItem(): Item {
        return DuskItems.BONEBOGGER_ITEM
    }

    override fun bandanaColors(): Int {
        val randomRange = 100
        val one = Math.random() * randomRange
        val two = Math.random() * randomRange - one
        val r: Int
        val b: Int
        val g: Int
        if (two + one > 75 && two > 0) {
            r = 25 + one.toInt()
            g = 25
            b = 25 + two.toInt()
        } else {
            r = if (two > 0) 25 + one.toInt() else 25 + one.toInt()
            g = 100
            b = 0
        }
        return Color(r, g, b).rgb
    }

    override fun getTrailingParticle(): ParticleEffect = BonecallerParticleEffect(0xEFC90B, 0x935D26)
}