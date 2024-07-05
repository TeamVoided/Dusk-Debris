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

open class BonechillerEntity : BonecallerEntity {

    constructor(entityType: EntityType<out BonechillerEntity>, world: World) : super(entityType, world)

    constructor(world: World) : super(DuskEntities.BONECHILLER, world)
    constructor(owner: LivingEntity?, world: World) :
            super(DuskEntities.BONECHILLER, owner, world) {
        this.owner = owner
    }

    constructor(x: Double, y: Double, z: Double, world: World) :
            super(DuskEntities.BONECHILLER, x, y, z, world)


    override fun getCalledEntity() = EntityType.STRAY
    override fun getDefaultItem(): Item {
        return DuskItems.BONECHILLER_ITEM
    }

    override fun getTrailingParticle(): ParticleEffect = BonecallerParticleEffect(0xE6ECED, 0xA2B6C4)
}