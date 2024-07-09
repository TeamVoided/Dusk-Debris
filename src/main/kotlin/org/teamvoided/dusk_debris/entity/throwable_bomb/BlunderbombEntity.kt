package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.block.throwable_bomb.AbstractThrwowableBombBlock
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.world.explosion.SpecialExplosionBehavior
import kotlin.math.cos
import kotlin.math.sin

open class BlunderbombEntity : AbstractThrwowableBombEntity {
    constructor(entityType: EntityType<out BlunderbombEntity>, world: World) : super(entityType, world)
    constructor(entityType: EntityType<out BlunderbombEntity>, owner: LivingEntity?, world: World) :
            super(entityType, owner, world)

    constructor(entityType: EntityType<out BlunderbombEntity>, x: Double, y: Double, z: Double, world: World) :
            super(entityType, x, y, z, world)

    constructor(world: World, owner: LivingEntity?) : super(DuskEntities.BLUNDERBOMB, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.BLUNDERBOMB, x, y, z, world)

    constructor(world: World, x: Double, y: Double, z: Double, customExplosionBehavior: ExplosionBehavior) :
            this(DuskEntities.BLUNDERBOMB, world) {
        val randVelocity = world.random.nextDouble() * 6.3
        this.setPosition(x, y, z)
        this.setVelocity(-sin(randVelocity) * 0.02, 0.04, -cos(randVelocity) * 0.02)
        this.prevX = x
        this.prevY = y
        this.prevZ = z
    }

    override fun explode() {
        val serverWorld = this.world as ServerWorld
        serverWorld.spawnParticles(
            DuskParticles.BLUNDERBOMB,
            this.x,
            this.y,
            this.z,
            20,
            0.0,
            0.0,
            0.0,
            1.0
        )
        world.playSound(
            this,
            this.blockPos,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundCategory.BLOCKS,
            0.7f,
            0.9f + world.random.nextFloat() * 0.2f
        )
        world.createExplosion(
            this, Explosion.createDamageSource(
                this.world,
                this
            ), getExplosionBehavior(),
            this.x,
            this.getBodyY(0.0625),
            this.z,
            DEFAULT_EXPLOSION_POWER,
            false,
            World.ExplosionSourceType.TNT,
            DuskParticles.BLUNDERBOMB,
            ParticleTypes.FLASH,
            SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE
        )
        super.explode()
    }

    override fun getDefaultItem(): Item {
        return DuskItems.BLUNDERBOMB_ITEM
    }

    override fun getHitDamage(): Float = 5f
    override fun getExplosionBehavior(): ExplosionBehavior = SpecialExplosionBehavior(
        DuskBlockTags.BLUNDERBOMB_DESTROYS,
        DuskEntityTypeTags.BLUNDERBOMB_DOES_NOT_DAMAGE,
        7f,
        1.1f,
        12f
    )


    companion object {
        const val DEFAULT_EXPLOSION_POWER = AbstractThrwowableBombBlock.DEFAULT_EXPLOSION_POWER
    }
}