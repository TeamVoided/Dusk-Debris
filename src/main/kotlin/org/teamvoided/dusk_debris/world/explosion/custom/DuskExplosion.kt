package org.teamvoided.dusk_debris.world.explosion.custom

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.phys.Vec3
import org.teamvoided.dusk_debris.util.box
import org.teamvoided.dusk_debris.util.spawnParticles

class DuskExplosion {
    constructor(
        world: ServerLevel,
        radius: Double,
        damage: Float,
        source: DamageSource,
        originPos: Vec3,
        particle: ParticleOptions
    ) {
        damageEntities(world, originPos, radius, damage, source)
        particles(world, radius, originPos, particle)
    }

    constructor(
        world: ServerLevel,
        radius: Double,
        damage: Float,
        source: DamageSource,
        particle: ParticleOptions? = null
    ) {
        val origin = source.sourcePosition
        if (origin != null) {
            damageEntities(world, origin, radius, damage, source)
            if (particle != null) {
                particles(world, radius, origin, particle)
            }
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

    fun damageEntities(world: ServerLevel, origin: Vec3, radius: Double, damage: Float, source: DamageSource) {
        val entitiesInRange =
            world.getEntities(null, box(radius).move(origin))
            {
//                !it.type.isIn(EntityTypeTags.EXPL)
                it.isAlive
                it.distanceToSqr(origin) <= radius * radius
            }
        entitiesInRange.sortBy { it.position().add(0.0, it.bbHeight / 2.0, 0.0).distanceToSqr(origin) }
        entitiesInRange.forEach {
            val position = it.position().add(0.0, it.bbHeight / 2.0, 0.0)
            val distance = position.subtract(origin).length()
            it.hurt(source, damageDistance(damage, distance, radius))
        }
    }

    fun damageDistance(damage: Float, distance: Double, radius: Double): Float {
        val square = (1 - (distance / radius))
        return ((square * square) * damage).toFloat()
    }

    fun particles(world: ServerLevel, radius: Double, origin: Vec3, particle: ParticleOptions) {
        world.spawnParticles(
            particle,
            origin,
            Vec3(
                world.random.nextDouble() - 0.5,
                world.random.nextDouble() - 0.5,
                world.random.nextDouble() - 0.5
            ).normalize().scale(radius)
        )
    }
}