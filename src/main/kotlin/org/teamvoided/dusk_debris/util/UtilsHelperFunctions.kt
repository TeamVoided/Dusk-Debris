package org.teamvoided.dusk_debris.util

import net.minecraft.block.Block
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket
import net.minecraft.particle.ParticleEffect
import net.minecraft.registry.HolderSet
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.*
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.TestableWorld
import net.minecraft.world.World
import kotlin.math.floor
import kotlin.math.sqrt

fun ServerWorld.spawnParticles(particle: ParticleEffect, pos: Vec3d, velocity: Vec3d) =
    this.spawnParticles(particle, pos.x, pos.y, pos.z, 0, velocity.x, velocity.y, velocity.z, 1.0)

fun World.addParticle(parameters: ParticleEffect, pos: Vec3d, velocity: Vec3d) {
    this.addParticle(parameters, false, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
}

fun World.addParticle(parameters: ParticleEffect, alwaysSpawn: Boolean, pos: Vec3d, velocity: Vec3d) {
    this.addParticle(parameters, alwaysSpawn, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
}

fun ServerWorld.spawnParticles(
    particle: ParticleEffect, pos: Vec3d, velocity: Vec3d, distance: Double
): Int {
    val particleS2CPacket = ParticleS2CPacket(
        particle,
        distance > 32,
        pos.x,
        pos.y,
        pos.z,
        velocity.x.toFloat(),
        velocity.y.toFloat(),
        velocity.z.toFloat(),
        1f,
        0
    )
    var i = 0
    for (j in this.players.indices) {
        val serverPlayerEntity = this.players[j]
        if (this.sendToPlayerIfNearby(serverPlayerEntity, distance, pos.x, pos.y, pos.z, particleS2CPacket)) {
            ++i
        }
    }
    return i
}

fun ServerWorld.sendToPlayerIfNearby(
    player: ServerPlayerEntity,
    distance: Double,
    x: Double,
    y: Double,
    z: Double,
    packet: Packet<*>?
): Boolean {
    if (player.world != this) {
        return false
    } else {
        val blockPos = player.blockPos
        if (blockPos.isCenterWithinDistance(Vec3d(x, y, z), distance)) {
            player.networkHandler.send(packet)
            return true
        } else {
            return false
        }
    }
}

fun DoublePerlinNoiseSampler.sample(blockPos: BlockPos): Double = this.sample(blockPos.toVec3d())

fun DoublePerlinNoiseSampler.sample(vec3d: Vec3d): Double = this.sample(vec3d.x, vec3d.y, vec3d.z)

fun createCuboidShape(minXZ: Double, minY: Double, maxXZ: Double, maxY: Double): VoxelShape {
    return Block.createCuboidShape(minXZ, minY, minXZ, maxXZ, maxY, maxXZ)
}

fun createCuboidShape(min1: Double, min2: Double, max1: Double, max2: Double, axis: Direction.Axis): VoxelShape {
    return when (axis) {
        Direction.Axis.Y -> createCuboidShape(min1, min2, max1, max2)
        Direction.Axis.X -> Block.createCuboidShape(min2, min1, min1, max2, max1, max1)
        Direction.Axis.Z -> Block.createCuboidShape(min1, min1, min2, max1, max1, max2)
    }
}

fun Vec3d.normalizeHorizontal(yMult: Double = 1.0): Vec3d {
    val xz = sqrt(this.x * this.x + this.z * this.z)
    val y = this.y * yMult
    return if (xz < 1.0E-4) Vec3d(0.0, y, 0.0)
    else Vec3d(this.x / xz, y, this.z / xz)
}

fun Vec3d.getSquaredDistanceToCenter(vec: Vec3d): Double {
    val d: Double = this.x - vec.x
    val e: Double = this.y - vec.y
    val f: Double = this.z - vec.z
    return d * d + e * e + f * f
}

fun Vec3d.toBlockPos(): BlockPos {
    return BlockPos(floor(this.x).toInt(), floor(this.y).toInt(), floor(this.z).toInt())
}

fun Vec3i.toVec3d(): Vec3d {
    return Vec3d(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
}

fun box(double: Double): Box {
    return Box(-double, -double, -double, double, double, double)
}

fun TestableWorld.isInSet(pos: BlockPos, tag: HolderSet<Block>): Boolean = this.testBlockState(pos) { it.isIn(tag) }
fun TestableWorld.isInTag(pos: BlockPos, tag: TagKey<Block>): Boolean = this.testBlockState(pos) { it.isIn(tag) }

fun VoxelShape.rotate(times: Int) = rotateVoxelShape(times, this)
fun VoxelShape.rotateY(times: Int = 1) = rotateVoxelShapeY(times, this)

fun VoxelShape.rotateFromDown(direction: Direction) = when (direction) {
    Direction.DOWN -> this
    Direction.UP -> this.rotateY(2)
    Direction.NORTH -> this.rotateY(1)
    Direction.EAST -> this.rotateY().rotate(1)
    Direction.SOUTH -> this.rotateY().rotate(2)
    Direction.WEST -> this.rotateY().rotate(3)
    else -> this
}

fun rotateVoxelShape(times: Int, shape: VoxelShape): VoxelShape {
    val shapes = arrayOf(shape, VoxelShapes.empty())
    for (i in 0 until times) {
        shapes[0].forEachBox { minX, minY, minZ, maxX, maxY, maxZ ->
            shapes[1] = VoxelShapes.union(shapes[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX))
        }
        shapes[0] = shapes[1]
        shapes[1] = VoxelShapes.empty()
    }
    return shapes[0]
}

fun rotateVoxelShapeY(times: Int, shape: VoxelShape): VoxelShape {
    val shapes = arrayOf(shape, VoxelShapes.empty())
    for (i in 0 until times) {
        shapes[0].forEachBox { minX, minY, minZ, maxX, maxY, maxZ ->
            shapes[1] = VoxelShapes.union(shapes[1], VoxelShapes.cuboid(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY))
        }
        shapes[0] = shapes[1]
        shapes[1] = VoxelShapes.empty()
    }
    return shapes[0]
}


fun Direction.asProperty(): BooleanProperty {
    return when (this) {
        Direction.UP -> Properties.UP
        Direction.DOWN -> Properties.DOWN
        Direction.NORTH -> Properties.NORTH
        Direction.SOUTH -> Properties.SOUTH
        Direction.WEST -> Properties.WEST
        Direction.EAST -> Properties.EAST
    }
}

