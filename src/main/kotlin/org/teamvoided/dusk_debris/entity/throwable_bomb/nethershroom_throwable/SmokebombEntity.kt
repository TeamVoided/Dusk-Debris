package org.teamvoided.dusk_debris.entity.throwable_bomb.nethershroom_throwable

import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.entity.throwable_bomb.NethershroomThrowableEntity
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.particle.color.NethershroomSporeParticleEffect

class SmokebombEntity : NethershroomThrowableEntity {
    constructor(entityType: EntityType<out SmokebombEntity>, world: Level) : super(entityType, world)

    constructor(world: Level, owner: LivingEntity?) : super(DuskEntities.SMOKEBOMB, owner, world)

    constructor(world: Level, x: Double, y: Double, z: Double) : super(DuskEntities.SMOKEBOMB, world, x, y, z)

    override fun getTrailingParticle(): ParticleOptions {
        val color = this.item.get(DataComponents.DYED_COLOR)
        println(color)
        if (color != null) {
            return NethershroomSporeParticleEffect(color.rgb)
        }
        return NethershroomSporeParticleEffect(0x7F7F7F)
    }

    override fun getDefaultItem(): Item {
        return DuskBlocks.SMOKEBOMB_BLOCK.asItem()
    }
}