package org.teamvoided.dusk_debris.entity.throwable_bomb.nethershroom_throwable

import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.entity.throwable_bomb.NethershroomThrowableEntity
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.util.purpleNethershroomSmoke

class BlindbombEntity : NethershroomThrowableEntity {
    constructor(entityType: EntityType<out BlindbombEntity>, world: Level) : super(entityType, world)

    constructor(world: Level, owner: LivingEntity?) : super(DuskEntities.BLINDBOMB, owner, world)

    constructor(world: Level, x: Double, y: Double, z: Double) : super(DuskEntities.BLINDBOMB, world, x, y, z)

    override var statusEffect: Holder<MobEffect>? = MobEffects.BLINDNESS


    override fun getDefaultItem(): Item {
        return DuskBlocks.POCKETPOISON_BLOCK.asItem()
    }
    override fun getTrailingParticle() = purpleNethershroomSmoke

}