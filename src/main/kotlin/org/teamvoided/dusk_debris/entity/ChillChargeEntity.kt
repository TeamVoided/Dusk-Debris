package org.teamvoided.dusk_debris.entity

import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import net.minecraft.world.level.SimpleExplosionDamageCalculator
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.*
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import java.util.*
import java.util.function.Function
import kotlin.math.max

class ChillChargeEntity : AbstractHurtingProjectile, ItemSupplier {
    constructor(entityType: EntityType<out ChillChargeEntity>, world: Level) : super(entityType, world) {
        this.accelerationPower = 0.0
    }

    constructor(type: EntityType<out ChillChargeEntity>, world: Level, entity: Entity, x: Double, y: Double, z: Double)
            : super(type, x, y, z, world) {
        this.owner = entity
        this.accelerationPower = 0.0
    }

    constructor(world: Level, d: Double, e: Double, f: Double, vec3d: Vec3)
            : super(DuskEntities.CHILL_CHARGE, d, e, f, vec3d, world)


    constructor(player: Player, world: Level, x: Double, y: Double, z: Double)
            : this(DuskEntities.CHILL_CHARGE, world, player, x, y, z)

    override fun makeBoundingBox(): AABB {
        val width = type.dimensions.width() / 2.0f
        val height = type.dimensions.height()
        val heightOffset = 0.15f
        return AABB(
            position().x - width.toDouble(), position().y - heightOffset, position().z - width.toDouble(),
            position().x + width.toDouble(), position().y - heightOffset + height.toDouble(), position().z + width.toDouble()
        )
    }

    override fun canCollideWith(other: Entity): Boolean =
        if (other is ChillChargeEntity) false else super.canCollideWith(other)

    override fun canHitEntity(entity: Entity): Boolean {
        return if (entity.type.`is`(DuskEntityTypeTags.CHILL_CHARGE_GOES_THROUGH)) false
        else super.canHitEntity(entity)
    }

    override fun onHitEntity(entityHitResult: EntityHitResult) {
        super.onHitEntity(entityHitResult)
        if (!level().isClientSide) {
            val owner = this.owner
            val var10000: LivingEntity? = owner as? LivingEntity
            val entity = entityHitResult.entity
            var10000?.setLastHurtMob(entity)
            val damageSource = this.damageSources().windCharge(this, var10000)
            if (entity.hurt(damageSource, 1.0f) && entity is LivingEntity) {
                EnchantmentHelper.doPostAttackEffects(level() as ServerLevel, entity, damageSource)
            }
            this.freeze(level(), defaultRange)
        }
    }

    override fun tick() {
        if ((!level().isClientSide && this.blockY > level().maxBuildHeight + 30) || isOnFire) {
            this.freeze(level(), defaultRange)
            this.discard()
        } else {
            super.tick()
            if (level() is ServerLevel && level().hasChunkAt(this.blockPosition())) {
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

    private fun freeze(world: Level, radius: Int) {
        if (!world.isClientSide) {
            val serverWorld = world as ServerLevel
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
        val entitiesNearby = world.getEntities(
            this, AABB(
                this.x - radius, this.y - radius, this.z - radius,
                this.x + radius, this.y + radius, this.z + radius
            )
        ) { obj: Entity -> obj.isAlive && !obj.type.`is`(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) }
        entitiesNearby.forEach {
            it.ticksFrozen = max(it.ticksFrozen, it.ticksRequiredToFreeze + random.nextIntBetweenInclusive(450, 500))
        }
        for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    val blockPos = blockPosition()
                        .relative(Direction.Axis.X, x)
                        .relative(Direction.Axis.Y, y)
                        .relative(Direction.Axis.Z, z)
                    val state = world.getBlockState(blockPos)
                    if (((state.`is`(Blocks.WATER) && state.getValue(BlockStateProperties.LEVEL) == 0) &&
                                (world.height < blockPos.y + 1 || world.getBlockState(blockPos.above())
                                    .`is`(BlockTags.AIR)))
                    ) {
                        world.setBlockAndUpdate(blockPos, Blocks.FROSTED_ICE.defaultBlockState())
                    } else if (state.`is`(Blocks.FROSTED_ICE)) {
                        world.setBlockAndUpdate(blockPos, Blocks.FROSTED_ICE.defaultBlockState())
                    } else if (/*state.isIn(DuskBlockTags.CHILL_CHARGE_AFFECTS) &&*/ state.hasProperty(
                            BlockStateProperties.LIT)) {
                        world.setBlockAndUpdate(blockPos, state.setValue(BlockStateProperties.LIT, false))
                    }
                }
            }
        }
    }

    override fun push(deltaX: Double, deltaY: Double, deltaZ: Double) = Unit
    override fun onHitBlock(blockHitResult: BlockHitResult) {
        super.onHitBlock(blockHitResult)
        if (!level().isClientSide) {
            this.freeze(level(), defaultRange)
            this.discard()
        }
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        if (!level().isClientSide) this.discard()
    }

    override fun shouldBurn(): Boolean = false
    override fun getItem(): ItemStack = DuskItems.CHILL_CHARGE.defaultInstance
    override fun getInertia(): Float = 1.0f
    override fun getLiquidInertia(): Float = this.getInertia()
    override fun getTrailParticle(): ParticleOptions? = null //this places the particle half a block above the entity
    override fun hurt(source: DamageSource, amount: Float): Boolean = false

    companion object {
        val defaultRange = 3
        val chillExplosionBehavior: ExplosionDamageCalculator =
            SimpleExplosionDamageCalculator(
                true, false, Optional.empty(),
                BuiltInRegistries.BLOCK.getTag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
            )
        const val explosionOffsetMult: Double = 0.25
    }
}