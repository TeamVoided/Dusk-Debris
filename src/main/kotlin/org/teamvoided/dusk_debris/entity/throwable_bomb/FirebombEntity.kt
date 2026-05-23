package org.teamvoided.dusk_debris.entity.throwable_bomb

import com.google.common.collect.Lists
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Explosion.BlockInteraction
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.AABB
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.particle.color.FlashParticleEffect
import org.teamvoided.dusk_debris.world.explosion.FirebombExplosionBehavior

class FirebombEntity : AbstractThrwowableBombEntity {
    constructor(entityType: EntityType<out FirebombEntity>, world: Level) : super(entityType, world)

    constructor(world: Level, owner: LivingEntity?) : super(DuskEntities.FIREBOMB, owner, world)

    constructor(world: Level, x: Double, y: Double, z: Double) : super(DuskEntities.FIREBOMB, x, y, z, world)
//    DustColorTransitionParticleEffect(FIRE, GREY, 1.0F)

    val firebombRadius = 4

    override fun explode() {
        val serverWorld = this.level() as ServerLevel
        serverWorld.sendParticles(
            DuskParticles.FIREBOMB,
            this.x, this.y, this.z,
            20,
            0.0, 0.0, 0.0,
            1.0
        )
        serverWorld.sendParticles(
            FlashParticleEffect(0xFF9000),
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
            0.6f + level().random.nextFloat() * 0.2f
        )
        modifyNearbyBlocks(level(), DuskBlockTags.FIREBOMB_DESTROYS, firebombRadius)
        burnEntities(level(), firebombRadius)
        super.explode()
    }

    fun burnEntities(world: Level, radius: Int) {
        val entitiesNearby = world.getEntities(
            this, AABB(
                this.x - radius,
                this.y - radius,
                this.z - radius,
                this.x + radius,
                this.y + radius,
                this.z + radius
            )
        ) { obj: Entity -> obj.isAlive && !obj.type.`is`(DuskEntityTypeTags.FIREBOMB_DOES_NOT_DAMAGE) }

        return entitiesNearby.forEach {
            it.hurt(this.damageSources().onFire(), 4f)
            it.remainingFireTicks += 200
        }
    }

    fun modifyNearbyBlocks(world: Level, condition: TagKey<Block>, firebombRadius: Int) {
        val posListDestroy: MutableList<BlockPos> = Lists.newArrayList<BlockPos>()
        var posListLight: MutableList<BlockPos> = Lists.newArrayList<BlockPos>()
        for (x in -firebombRadius..firebombRadius) {
            for (y in -firebombRadius..firebombRadius) {
                for (z in -firebombRadius..firebombRadius) {
                    val block = blockPosition()
                        .relative(Direction.Axis.X, x)
                        .relative(Direction.Axis.Y, y)
                        .relative(Direction.Axis.Z, z)
                    if (world.getBlockState(block).`is`(condition)) {
                        if (world.getBlockState(block).hasProperty(BlockStateProperties.LIT))
                            posListLight.add(block)
                        else
                            posListDestroy.add(block)
                    }
                }
            }
        }
        posListDestroy.forEach {
            world.getBlockState(it)
                .onExplosionHit(
                    world,
                    it,
                    Explosion(
                        world,
                        null,
                        it.x.toDouble(),
                        it.y.toDouble(),
                        it.z.toDouble(),
                        0f,
                        false,
                        BlockInteraction.DESTROY
                    )
                ) { itemStack: ItemStack, blockPos: BlockPos ->
                    if (world.isClientSide) return@onExplosionHit
                    val stacks = Block.getDrops(
                        world.getBlockState(blockPos),
                        world as ServerLevel,
                        blockPos,
                        world.getBlockEntity(blockPos),
                        null, // player
                        itemStack
                    )
                    stacks.forEach { stack ->
                        Block.popResource(world, it, stack)
                    }
                }
            world.destroyBlock(it, world.gameRules.getBoolean(GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY))
        }
        posListLight.forEach {
            world.setBlockAndUpdate(it, world.getBlockState(it).setValue(BlockStateProperties.LIT, true))
        }
    }

    override fun getDefaultItem() = DuskBlocks.FIREBOMB_BLOCK.asItem()

    override fun getTrailingParticle(): ParticleOptions = ParticleTypes.FLAME
    override fun getExplosionBehavior(): ExplosionDamageCalculator = FirebombExplosionBehavior(DuskBlockTags.FIREBOMB_DESTROYS)
}