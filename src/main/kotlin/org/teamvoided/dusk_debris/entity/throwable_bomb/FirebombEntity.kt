package org.teamvoided.dusk_debris.entity.throwable_bomb

import com.google.common.collect.Lists
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.block.GunpowderBlock
import org.teamvoided.dusk_debris.data.DuskBlockTags
import org.teamvoided.dusk_debris.data.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.world.explosion.FirebombExplosionBehavior

class FirebombEntity : AbstractThrwowableBombEntity {
    constructor(entityType: EntityType<out FirebombEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity?) : super(DuskEntities.FIREBOMB, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.FIREBOMB, x, y, z, world)


    override val trailingParticle = ParticleTypes.FLAME
    override var explosionBehavior: ExplosionBehavior = FirebombExplosionBehavior(
        DuskBlockTags.FIREBOMB_DESTROYS
    )
//    DustColorTransitionParticleEffect(FIRE, GREY, 1.0F)

    override fun explode() {
        val serverWorld = this.world as ServerWorld
        serverWorld.spawnParticles(
            DuskParticles.FIREBOMB,
            this.x,
            this.y,
            this.z,
            20,
            0.0,
            0.0,
            0.0,
            1.0
        )
        serverWorld.spawnParticles(
            ParticleTypes.FLASH,
            this.x,
            this.y,
            this.z,
            1,
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
//        world.createExplosion(
//            this, Explosion.createDamageSource(
//                this.world,
//                this
//            ), explosionBehavior,
//            this.x,
//            this.getBodyY(0.0625),
//            this.z,
//            BlunderbombEntity.DEFAULT_EXPLOSION_POWER,
//            false,
//            World.ExplosionSourceType.TNT,
//            DuskParticles.FIREBOMB,
//            ParticleTypes.FLASH,
//            SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE
//        )
        val firebombRadius = 5
        modifyNearbyBlocks(world, DuskBlockTags.FIREBOMB_DESTROYS, firebombRadius)
        burnEntities(world, firebombRadius)
        super.explode()
    }

    private fun burnEntities(world: World, firebombRadius: Int) {
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

        return entitiesNearby.forEach {
            it.damage(this.damageSources.onFire(), 3f)
            it.fireTicks += 200
        }
    }

    fun modifyNearbyBlocks(world: World, condition: TagKey<Block>, firebombRadius: Int) {
        val posList: MutableList<BlockPos> = Lists.newArrayList<BlockPos>()
        var posListGunpowder: MutableList<BlockPos> = Lists.newArrayList<BlockPos>()
        for (x in -firebombRadius..firebombRadius) {
            for (y in -firebombRadius..firebombRadius) {
                for (z in -firebombRadius..firebombRadius) {
                    val block = blockPos
                        .offset(Direction.Axis.X, x)
                        .offset(Direction.Axis.Y, y)
                        .offset(Direction.Axis.Z, z)
                    if (world.getBlockState(block).isIn(condition))
                        posList.add(block)
                    else if (world.getBlockState(block).isOf(DuskBlocks.GUNPOWDER))
                        posListGunpowder.add(block)
                }
            }
        }
        posList.forEach {
            world.breakBlock(it, world.gameRules.getBooleanValue(GameRules.BLOCK_EXPLOSION_DROP_DECAY))
        }
        posListGunpowder.forEach {
            world.setBlockState(it, world.getBlockState(it).with(GunpowderBlock.IGNITED, true))
        }
    }

    override fun getDefaultItem(): Item {
        return DuskItems.FIREBOMB_ITEM
    }
}