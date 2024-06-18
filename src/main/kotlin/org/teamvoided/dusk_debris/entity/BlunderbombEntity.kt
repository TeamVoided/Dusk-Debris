package org.teamvoided.dusk_debris.entity

import net.minecraft.block.Block
import org.teamvoided.dusk_debris.block.BlunderbombBlock
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.world.explosion.SpecialExplosionBehavior

open class BlunderbombEntity : ThrownItemEntity {
    constructor(entityType: EntityType<out BlunderbombEntity>, world: World) : super(entityType, world)
    constructor(entityType: EntityType<out BlunderbombEntity>, owner: LivingEntity?, world: World) :
            super(entityType, owner, world)

    constructor(entityType: EntityType<out BlunderbombEntity>, x: Double, y: Double, z: Double, world: World) :
            super(entityType, x, y, z, world)

    constructor(world: World, owner: LivingEntity?) : super(DuskEntities.BLUNDERBOMB, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.BLUNDERBOMB, x, y, z, world)

    open val trailingParticle: ParticleEffect = ParticleTypes.SMOKE
    open val explosionBehavior: ExplosionBehavior = SpecialExplosionBehavior(
        DuskBlockTags.BLUNDERBOMB_DESTROYS,
        DuskEntityTypeTags.BLUNDERBOMB_DOES_NOT_DAMAGE,
        1.1f,
        1f
    )

    override fun handleStatus(status: Byte) {
        if (status.toInt() == 3) {
            val scatter = 0.08

            for (i in 0..7) {
                world.addParticle(
                    ItemStackParticleEffect(ParticleTypes.ITEM, this.stack),
                    this.x,
                    this.y,
                    this.z,
                    (random.nextFloat().toDouble() - 0.5) * scatter,
                    (random.nextFloat().toDouble() - 0.5) * scatter,
                    (random.nextFloat().toDouble() - 0.5) * scatter
                )
            }
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        entityHitResult.entity.damage(this.damageSources.thrown(this, this.owner), 0.0f)
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            this.discard()
            this.explode()
        }
    }

    override fun tick() {
        if (world.isClient) {
            world.addParticle(
                trailingParticle,
                this.x,
                this.y,
                this.z,
                0.0,
                0.0,
                0.0
            )
        }
        return super.tick()
    }

    open fun explode() {
        world.playSound(
            this,
            this.blockPos,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundCategory.BLOCKS,
            0.7f,
            0.9f + world.random.nextFloat() * 0.2f
        )
        world.syncWorldEvent(
            null,
            2001,
            this.blockPos,
            Block.getRawIdFromState(getDefaultBlock().defaultState)
        )
        world.createExplosion(
            this, Explosion.createDamageSource(
                this.world,
                this
            ), explosionBehavior,
            this.x,
            this.getBodyY(0.0625),
            this.z,
            DEFAULT_EXPLOSION_POWER,
            false,
            World.ExplosionSourceType.TNT,
            ParticleTypes.BUBBLE,
            ParticleTypes.BUBBLE,
            SoundEvents.AMBIENT_CAVE
        )
    }

    override fun getDefaultItem(): Item {
        return DuskItems.BLUNDERBOMB
    }

    private fun getDefaultBlock(): Block {
        return (defaultItem as BlockItem).block
    }


    companion object {
        const val DEFAULT_EXPLOSION_POWER = BlunderbombBlock.DEFAULT_EXPLOSION_POWER
    }
}