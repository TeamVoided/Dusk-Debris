package org.teamvoided.dusk_debris.entity.throwable_bomb.nethershroom_throwable

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.registry.Holder
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.throwable_bomb.NethershroomThrowableEntity
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.util.purpleNethershroomSmoke

class BlindbombEntity : NethershroomThrowableEntity {
    constructor(entityType: EntityType<out BlindbombEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity?) : super(DuskEntities.BLINDBOMB, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.BLINDBOMB, world, x, y, z)

    override var statusEffect: Holder<StatusEffect>? = StatusEffects.BLINDNESS


    override fun getDefaultItem(): Item {
        return DuskBlocks.POCKETPOISON_BLOCK.asItem()
    }
    override fun getTrailingParticle() = purpleNethershroomSmoke

}