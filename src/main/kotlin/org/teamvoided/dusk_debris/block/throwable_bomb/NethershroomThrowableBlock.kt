package org.teamvoided.dusk_debris.block.throwable_bomb

import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.registry.Holder
import net.minecraft.util.math.BlockPos
import net.minecraft.world.*
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.entity.throwable_bomb.BlunderbombEntity
import org.teamvoided.dusk_debris.entity.throwable_bomb.NethershroomThrowableEntity
import org.teamvoided.dusk_debris.world.explosion.SpecialExplosionBehavior

open class NethershroomThrowableBlock(
    val entity: EntityType<out NethershroomThrowableEntity>,
    val particle: ParticleEffect,
    val statusEffect: Holder<StatusEffect>?,
    val hasDoubleEffect: Boolean,
    settings: Settings
) : AbstractThrwowableBombBlock(settings) {
    override fun explode(world: World, pos: BlockPos, explosionBehavior: ExplosionBehavior) {
        world.breakBlock(pos, false)
        println(this.asItem().toString() + " From Block")
        val nethershroomThrowableEntity = NethershroomThrowableEntity(
            world,
            pos.x.toDouble() + 0.5,
            pos.y.toDouble() + Math.random() * 0.8,
            pos.z.toDouble() + 0.5,
            this.asItem(),
            entity,
            particle,
            statusEffect,
            hasDoubleEffect
        )
        world.spawnEntity(nethershroomThrowableEntity)
    }
}