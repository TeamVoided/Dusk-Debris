package org.teamvoided.dusk_debris.entity.throwable_bomb

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import org.teamvoided.dusk_debris.block.throwable_bomb.AbstractThrwowableBombBlock
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.particle.color.FlashParticleEffect
import org.teamvoided.dusk_debris.world.explosion.SpecialExplosionBehavior
import kotlin.math.cos
import kotlin.math.sin

open class BlunderbombEntity : AbstractThrwowableBombEntity {
    constructor(entityType: EntityType<out BlunderbombEntity>, world: Level) : super(entityType, world)
    constructor(entityType: EntityType<out BlunderbombEntity>, owner: LivingEntity?, world: Level) :
            super(entityType, owner, world)

    constructor(entityType: EntityType<out BlunderbombEntity>, x: Double, y: Double, z: Double, world: Level) :
            super(entityType, x, y, z, world)

    constructor(world: Level, owner: LivingEntity?) : super(DuskEntities.BLUNDERBOMB, owner, world)

    constructor(world: Level, x: Double, y: Double, z: Double) : super(DuskEntities.BLUNDERBOMB, x, y, z, world)

    constructor(world: Level, x: Double, y: Double, z: Double, customExplosionBehavior: ExplosionDamageCalculator) :
            this(DuskEntities.BLUNDERBOMB, world) {
        val randVelocity = world.random.nextDouble() * 6.3
        this.setPos(x, y, z)
        this.setDeltaMovement(-sin(randVelocity) * 0.02, 0.04, -cos(randVelocity) * 0.02)
        this.xo = x
        this.yo = y
        this.zo = z
    }

    override fun explode() {
        val serverWorld = this.level() as ServerLevel
        serverWorld.sendParticles(
            DuskParticles.BLUNDERBOMB,
            this.x, this.y, this.z,
            20,
            0.0, 0.0, 0.0,
            1.0
        )
        serverWorld.sendParticles(
            FlashParticleEffect(0x603300),
            this.x, this.y, this.z,
            1,
            0.0, 0.0, 0.0,
            1.0
        )
        level().playSound(
            this,
            this.blockPosition(),
            SoundEvents.GLASS_BREAK,
            SoundSource.BLOCKS,
            0.7f,
            0.9f + level().random.nextFloat() * 0.2f
        )
        level().explode(
            this, Explosion.getDefaultDamageSource(
                this.level(),
                this
            ), getExplosionBehavior(),
            this.x,
            this.getY(0.0625),
            this.z,
            DEFAULT_EXPLOSION_POWER,
            false,
            Level.ExplosionInteraction.TNT,
            DuskParticles.BLUNDERBOMB,
            ParticleTypes.FLASH,
            SoundEvents.RESPAWN_ANCHOR_DEPLETE
        )
        super.explode()
    }

    override fun getDefaultItem(): Item {
        return DuskBlocks.BLUNDERBOMB_BLOCK.asItem()
    }

    override fun getHitDamage(): Float = 5f
    override fun getExplosionBehavior(): ExplosionDamageCalculator = SpecialExplosionBehavior(
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