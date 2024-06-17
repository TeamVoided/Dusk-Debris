package org.teamvoided.dusk_debris.entity

import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_autumn.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.data.DuskBlockTags
import java.util.*

class BlunderbombEntity : ThrownItemEntity {
    constructor(entityType: EntityType<out BlunderbombEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity?) : super(DuskEntities.BLUNDERBOMB, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.BLUNDERBOMB, x, y, z, world)

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

    private fun explode() {
        val explosionPower = 2f
        world.createExplosion(
            this, Explosion.createDamageSource(
                this.world,
                this
            ), blockDestroyer,
            this.x,
            this.getBodyY(0.0625),
            this.z, explosionPower, false, World.ExplosionSourceType.TNT
        )
    }

    override fun getDefaultItem(): Item {
        return DuskItems.BLUNDERBOMB
    }

    companion object {
        private val blockDestroyer: ExplosionBehavior = object : ExplosionBehavior() {
            override fun canDestroyBlock(
                explosion: Explosion,
                world: BlockView,
                pos: BlockPos,
                state: BlockState,
                power: Float
            ): Boolean {
                return if (!state.isIn(DuskBlockTags.BLUNDERBOMB_DESTROYS)) false else super.canDestroyBlock(
                    explosion,
                    world,
                    pos,
                    state,
                    power
                )
            }

            override fun getBlastResistance(
                explosion: Explosion,
                world: BlockView,
                pos: BlockPos,
                blockState: BlockState,
                fluidState: FluidState
            ): Optional<Float> {
                return if (!blockState.isIn(DuskBlockTags.BLUNDERBOMB_DESTROYS)) Optional.empty() else super.getBlastResistance(
                    explosion,
                    world,
                    pos,
                    blockState,
                    fluidState
                )
            }
        }
    }
}