package org.teamvoided.dusk_debris.entity

import net.minecraft.block.Blocks
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.FlyingItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ExplosiveProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleEffect
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.EntityTypeTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.explosion.ExplosionBehavior
import net.minecraft.world.explosion.SimpleExplosionBehavior
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.data.tags.DuskItemTags
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems

import java.util.*
import java.util.function.Function
import kotlin.math.max

class ChillChargeEntity : ExplosiveProjectileEntity, FlyingItemEntity {
    constructor(entityType: EntityType<out ChillChargeEntity>, world: World) : super(entityType, world) {
        this.accelerationPower = 0.0
    }

    constructor(type: EntityType<out ChillChargeEntity>, world: World, entity: Entity, x: Double, y: Double, z: Double)
            : super(type, x, y, z, world) {
        this.owner = entity
        this.accelerationPower = 0.0
    }

    constructor(world: World, d: Double, e: Double, f: Double, vec3d: Vec3d)
            : super(DuskEntities.CHILL_CHARGE, d, e, f, vec3d, world)


    constructor(player: PlayerEntity, world: World, x: Double, y: Double, z: Double)
            : this(DuskEntities.CHILL_CHARGE, world, player, x, y, z)

    override fun calculateBoundingBox(): Box {
        val width = type.dimensions.width() / 2.0f
        val height = type.dimensions.height()
        val heightOffset = 0.15f
        return Box(
            pos.x - width.toDouble(), pos.y - heightOffset, pos.z - width.toDouble(),
            pos.x + width.toDouble(), pos.y - heightOffset + height.toDouble(), pos.z + width.toDouble()
        )
    }

    override fun collidesWith(other: Entity): Boolean =
        if (other is ChillChargeEntity) false else super.collidesWith(other)

    override fun canHit(entity: Entity): Boolean {
        return if (entity.type.isIn(DuskEntityTypeTags.CHILL_CHARGE_GOES_THROUGH)) false
        else super.canHit(entity)
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        if (!world.isClient) {
            val owner = this.owner
            val var10000: LivingEntity? = owner as? LivingEntity
            val entity = entityHitResult.entity
            var10000?.onAttacking(entity)
            val damageSource = this.damageSources.windCharge(this, var10000)
            if (entity.damage(damageSource, 1.0f) && entity is LivingEntity) {
                EnchantmentHelper.onEntityDamaged(world as ServerWorld, entity, damageSource)
            }
            this.freeze(world, defaultRange)
        }
    }

    override fun tick() {
        if ((!world.isClient && this.blockY > world.topY + 30) || isOnFire) {
            this.freeze(world, defaultRange)
            this.discard()
        } else {
            super.tick()
            if (world is ServerWorld && world.isChunkLoaded(this.blockPos)) {
                // TODO replace with voidlib
//                (world as ServerWorld).spawnParticles(
//                    DuskParticles.SNOWFLAKE, pos, Vec3d(
//                        (random.nextDouble() * 2.0 - 1.0) * 0.05,
//                        (random.nextDouble() * 2.0 - 1.0) * 0.05,
//                        (random.nextDouble() * 2.0 - 1.0) * 0.05
//                    )
//                )
            }
        }
    }

    private fun freeze(world: World, radius: Int) {
        if (!world.isClient) {
            val serverWorld = world as ServerWorld
            // TODO replace with voidlib
//            repeat(90) {
//                serverWorld.spawnParticles(
//                    DuskParticles.SNOWFLAKE, pos, Vec3d(
//                        (random.nextDouble() * 2.0 - 1.0),
//                        (random.nextDouble() * 2.0 - 1.0),
//                        (random.nextDouble() * 2.0 - 1.0)
//                    ).normalize().multiply(random.nextDouble() * 0.5)
//                )
//            }
        }
        val entitiesNearby = world.getOtherEntities(
            this, Box(
                this.x - radius, this.y - radius, this.z - radius,
                this.x + radius, this.y + radius, this.z + radius
            )
        ) { obj: Entity -> obj.isAlive && !obj.type.isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) }
        entitiesNearby.forEach {
            it.frozenTicks = max(it.frozenTicks, it.maxFreezeTicks + random.rangeInclusive(450, 500))
        }
        for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    val blockPos = blockPos
                        .offset(Direction.Axis.X, x)
                        .offset(Direction.Axis.Y, y)
                        .offset(Direction.Axis.Z, z)
                    val state = world.getBlockState(blockPos)
                    if (((state.isOf(Blocks.WATER) && state.get(Properties.LEVEL_15) == 0) &&
                                (world.height < blockPos.y + 1 || world.getBlockState(blockPos.up())
                                    .isIn(BlockTags.AIR)))
                    ) {
                        world.setBlockState(blockPos, Blocks.FROSTED_ICE.defaultState)
                    } else if (state.isOf(Blocks.FROSTED_ICE)) {
                        world.setBlockState(blockPos, Blocks.FROSTED_ICE.defaultState)
                    } else if (/*state.isIn(DuskBlockTags.CHILL_CHARGE_AFFECTS) &&*/ state.contains(Properties.LIT)) {
                        world.setBlockState(blockPos, state.with(Properties.LIT, false))
                    }
                }
            }
        }
    }

    override fun addVelocity(deltaX: Double, deltaY: Double, deltaZ: Double) = Unit
    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        if (!world.isClient) {
            this.freeze(world, defaultRange)
            this.discard()
        }
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) this.discard()
    }

    override fun isBurning(): Boolean = false
    override fun getStack(): ItemStack = DuskItems.CHILL_CHARGE.defaultStack
    override fun getDrag(): Float = 1.0f
    override fun drag(): Float = this.drag
    override fun getParticleType(): ParticleEffect? = null //this places the particle half a block above the entity
    override fun damage(source: DamageSource, amount: Float): Boolean = false

    companion object {
        val defaultRange = 3
        val chillExplosionBehavior: ExplosionBehavior =
            SimpleExplosionBehavior(
                true, false, Optional.empty(),
                Registries.BLOCK.getTag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
            )
        const val explosionOffsetMult: Double = 0.25
    }
}