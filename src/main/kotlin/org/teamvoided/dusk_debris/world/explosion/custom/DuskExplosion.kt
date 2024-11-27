package org.teamvoided.dusk_debris.world.explosion.custom

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.particle.ParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import org.teamvoided.dusk_debris.util.getSquaredDistanceToCenter
import org.teamvoided.dusk_debris.util.box
import org.teamvoided.dusk_debris.util.spawnParticles

class DuskExplosion(
    world: ServerWorld,
    radius: Double,
    damage: Float,
    source: DamageSource,
    particle: ParticleEffect? = null
) {
    init {
        damageEntities(world, radius, damage, source)
        val origin = source.position
        if (origin != null && particle != null) {
            particles(world, radius, origin, particle)
        }
    }

//    fun destroyBlocks(world: ServerWorld, radius: Double, source: DamageSource) {
//        val originVec = source.position
//        if (originVec != null) {
//            val origin =
//                BlockPos(
//                    originVec.x.roundToInt(),
//                    originVec.y.roundToInt(),
//                    originVec.z.roundToInt()
//                )
//            val posListDestroy: MutableList<BlockPos> = Lists.newArrayList<BlockPos>()
//            val radiusInt = radius.toInt()
//            for (x in -radiusInt..radiusInt) {
//                for (y in -radiusInt..radiusInt) {
//                    for (z in -radiusInt..radiusInt) {
//                        val block = origin.add(x, y, z)
//                        posListDestroy.add(block)
//                    }
//                }
//            }
//            posListDestroy.sortBy { it.ofCenter().squaredDistanceTo(originVec) }
//            posListDestroy.forEach {
//                if (it .getSquaredDistanceToCenter(originVec) > radius){
//                    world.breakBlock(it, world.gameRules.getBooleanValue(GameRules.BLOCK_EXPLOSION_DROP_DECAY))
//                }
//            }
//        }
//    }
//
//    fun modifyNearbyBlocks(world: World, condition: TagKey<Block>, firebombRadius: Int) {
//        val posListDestroy: MutableList<BlockPos> = Lists.newArrayList<BlockPos>()
//        var posListLight: MutableList<BlockPos> = Lists.newArrayList<BlockPos>()
//        for (x in -firebombRadius..firebombRadius) {
//            for (y in -firebombRadius..firebombRadius) {
//                for (z in -firebombRadius..firebombRadius) {
//                    val block = blockPos
//                        .offset(Direction.Axis.X, x)
//                        .offset(Direction.Axis.Y, y)
//                        .offset(Direction.Axis.Z, z)
//                    if (world.getBlockState(block).isIn(condition)) {
//                        if (world.getBlockState(block).contains(Properties.LIT))
//                            posListLight.add(block)
//                        else
//                            posListDestroy.add(block)
//                    }
//                }
//            }
//        }
//        posListDestroy.forEach {
//            world.getBlockState(it)
//                .onExplosion(
//                    world,
//                    it,
//                    Explosion(
//                        world,
//                        null,
//                        it.x.toDouble(),
//                        it.y.toDouble(),
//                        it.z.toDouble(),
//                        0f,
//                        false,
//                        Explosion.DestructionType.DESTROY
//                    )
//                ) { itemStack: ItemStack, blockPos: BlockPos ->
//                    if (world.isClient) return@onExplosion
//                    val stacks = Block.getDroppedStacks(
//                        world.getBlockState(blockPos),
//                        world as ServerWorld,
//                        blockPos,
//                        world.getBlockEntity(blockPos),
//                        null, // player
//                        itemStack
//                    )
//                    stacks.forEach { stack ->
//                        Block.dropStack(world, it, stack)
//                    }
//                }
//            world.breakBlock(it, world.gameRules.getBooleanValue(GameRules.BLOCK_EXPLOSION_DROP_DECAY))
//        }
//        posListLight.forEach {
//            world.setBlockState(it, world.getBlockState(it).with(Properties.LIT, true))
//        }
//    }

    fun damageEntities(world: ServerWorld, radius: Double, damage: Float, source: DamageSource) {
        val origin = source.position
        if (origin != null) {
            val entitiesInRange =
                world.getOtherEntities(null, box(radius).offset(origin))
                {
//                !it.type.isIn(EntityTypeTags.EXPL)
                    it.isAlive
                    it.squaredDistanceTo(origin) <= radius * radius
                }
            entitiesInRange.sortBy { it.pos.add(0.0, it.height / 2.0, 0.0).squaredDistanceTo(origin) }
            entitiesInRange.forEach {
                val position = it.pos.add(0.0, it.height / 2.0, 0.0)
                val distance = position.getSquaredDistanceToCenter(origin)
                if (position.getSquaredDistanceToCenter(origin) < radius) {
                    it.damage(source, damageDistance(damage, distance, radius))
                }
            }
        }
    }

    fun damageDistance(damage: Float, distance: Double, radius: Double): Float {
        val square = (1 - (distance / radius))
        return ((square * square) * damage).toFloat()
    }

    fun particles(world: ServerWorld, radius: Double, origin: Vec3d, particle: ParticleEffect) {
        world.spawnParticles(
            particle,
            origin,
            Vec3d(
                world.random.nextDouble() - 0.5,
                world.random.nextDouble() - 0.5,
                world.random.nextDouble() - 0.5
            ).normalize().multiply(radius)
        )
    }
}