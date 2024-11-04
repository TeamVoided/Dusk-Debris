package org.teamvoided.dusk_debris.entity.throwable_bomb.nethershroom_throwable

import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleEffect
import net.minecraft.world.World
import org.teamvoided.dusk_debris.entity.throwable_bomb.NethershroomThrowableEntity
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.particle.NethershroomSporeParticleEffect

class SmokebombEntity : NethershroomThrowableEntity {
    constructor(entityType: EntityType<out SmokebombEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity?) : super(DuskEntities.SMOKEBOMB, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.SMOKEBOMB, world, x, y, z)

    override fun getTrailingParticle(): ParticleEffect {
        val color = this.stack.get(DataComponentTypes.DYED_COLOR)
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