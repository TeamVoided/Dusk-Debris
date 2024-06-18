package org.teamvoided.dusk_debris.entity

import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.particle.DustColorTransitionParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.block.BlunderbombBlock
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.world.explosion.FirebombExplosionBehavior
import org.teamvoided.dusk_debris.world.explosion.SpecialExplosionBehavior

class FirebombEntity : BlunderbombEntity {
    constructor(entityType: EntityType<out FirebombEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity?) : super(DuskEntities.FIREBOMB, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.FIREBOMB, x, y, z, world)



    override val trailingParticle = DustColorTransitionParticleEffect(FIRE, GREY, 1.0F)
    override val explosionBehavior = FirebombExplosionBehavior(
        DuskBlockTags.FIREBOMB_DESTROYS
    )

    override fun explode() {
        val firebombRadius = 3.0
        val entitiesNearby = world.getOtherEntities(
            this, Box(
                this.x - firebombRadius,
                this.y - firebombRadius,
                this.z - firebombRadius,
                this.x + firebombRadius,
                this.y + firebombRadius,
                this.z + firebombRadius
            )
        ) { obj: Entity -> obj.isAlive && !obj.type.isIn(DuskEntityTypeTags.FIREBOMB_DOES_NOT_DAMAGE) }
        entitiesNearby.forEach {
            it.damage(this.damageSources.onFire(), 4f)
            it.fireTicks += 100
        }
        super.explode()
    }

    override fun getDefaultItem(): Item {
        return DuskItems.FIREBOMB
    }

    companion object {
        val FIRE = Vec3d.unpackRgb(0xffffff).toVector3f()
        val GREY = Vec3d.unpackRgb(0xff5500).toVector3f()
    }
}