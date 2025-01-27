package org.teamvoided.dusk_debris.entity.throwable_bomb

import com.google.common.collect.Lists
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.Explosion.DestructionType
import net.minecraft.world.explosion.ExplosionBehavior
import org.teamvoided.dusk_debris.data.tags.DuskBlockTags
import org.teamvoided.dusk_debris.data.tags.DuskEntityTypeTags
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskEntities
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.init.DuskParticles
import org.teamvoided.dusk_debris.particle.FlashParticleEffect
import org.teamvoided.dusk_debris.world.explosion.FirebombExplosionBehavior

class FirebombEntity : AbstractThrwowableBombEntity {
    constructor(entityType: EntityType<out FirebombEntity>, world: World) : super(entityType, world)

    constructor(world: World, owner: LivingEntity?) : super(DuskEntities.FIREBOMB, owner, world)

    constructor(world: World, x: Double, y: Double, z: Double) : super(DuskEntities.FIREBOMB, x, y, z, world)
//    DustColorTransitionParticleEffect(FIRE, GREY, 1.0F)

    val firebombRadius = 4

    override fun explode() {
        val serverWorld = this.world as ServerWorld
        serverWorld.spawnParticles(
            DuskParticles.FIREBOMB,
            this.x, this.y, this.z,
            20,
            0.0, 0.0, 0.0,
            1.0
        )
        serverWorld.spawnParticles(
            FlashParticleEffect(0xFF9000),
            this.x, this.y, this.z,
            1,
            0.0, 0.0, 0.0,
            1.0
        )
        world.playSound(
            this,
            this.blockPos,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundCategory.BLOCKS,
            0.7f,
            0.6f + world.random.nextFloat() * 0.2f
        )
        modifyNearbyBlocks(world, DuskBlockTags.FIREBOMB_DESTROYS, firebombRadius)
        burnEntities(world, firebombRadius)
        super.explode()
    }

    fun burnEntities(world: World, radius: Int) {
        val entitiesNearby = world.getOtherEntities(
            this, Box(
                this.x - radius,
                this.y - radius,
                this.z - radius,
                this.x + radius,
                this.y + radius,
                this.z + radius
            )
        ) { obj: Entity -> obj.isAlive && !obj.type.isIn(DuskEntityTypeTags.FIREBOMB_DOES_NOT_DAMAGE) }

        return entitiesNearby.forEach {
            it.damage(this.damageSources.onFire(), 4f)
            it.fireTicks += 200
        }
    }

    fun modifyNearbyBlocks(world: World, condition: TagKey<Block>, firebombRadius: Int) {
        val posListDestroy: MutableList<BlockPos> = Lists.newArrayList<BlockPos>()
        var posListLight: MutableList<BlockPos> = Lists.newArrayList<BlockPos>()
        for (x in -firebombRadius..firebombRadius) {
            for (y in -firebombRadius..firebombRadius) {
                for (z in -firebombRadius..firebombRadius) {
                    val block = blockPos
                        .offset(Direction.Axis.X, x)
                        .offset(Direction.Axis.Y, y)
                        .offset(Direction.Axis.Z, z)
                    if (world.getBlockState(block).isIn(condition)) {
                        if (world.getBlockState(block).contains(Properties.LIT))
                            posListLight.add(block)
                        else
                            posListDestroy.add(block)
                    }
                }
            }
        }
        posListDestroy.forEach {
            world.getBlockState(it)
                .onExplosion(
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
                        DestructionType.DESTROY
                    )
                ) { itemStack: ItemStack, blockPos: BlockPos ->
                    if (world.isClient) return@onExplosion
                    val stacks = Block.getDroppedStacks(
                        world.getBlockState(blockPos),
                        world as ServerWorld,
                        blockPos,
                        world.getBlockEntity(blockPos),
                        null, // player
                        itemStack
                    )
                    stacks.forEach { stack ->
                        Block.dropStack(world, it, stack)
                    }
                }
            world.breakBlock(it, world.gameRules.getBooleanValue(GameRules.BLOCK_EXPLOSION_DROP_DECAY))
        }
        posListLight.forEach {
            world.setBlockState(it, world.getBlockState(it).with(Properties.LIT, true))
        }
    }

    override fun getDefaultItem() = DuskBlocks.FIREBOMB_BLOCK.asItem()

    override fun getTrailingParticle(): ParticleEffect = ParticleTypes.FLAME
    override fun getExplosionBehavior(): ExplosionBehavior = FirebombExplosionBehavior(DuskBlockTags.FIREBOMB_DESTROYS)
}