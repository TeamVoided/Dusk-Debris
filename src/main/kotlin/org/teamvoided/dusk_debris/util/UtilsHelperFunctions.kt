package org.teamvoided.dusk_debris.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderSet
import net.minecraft.core.Vec3i
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.levelgen.synth.NormalNoise
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import kotlin.math.floor
import kotlin.math.sqrt

fun ServerLevel.spawnParticles(particle: ParticleOptions, pos: Vec3, velocity: Vec3) =
    this.sendParticles(particle, pos.x, pos.y, pos.z, 0, velocity.x, velocity.y, velocity.z, 1.0)

fun Level.addParticle(parameters: ParticleOptions, pos: Vec3, velocity: Vec3) {
    this.addParticle(parameters, false, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
}

fun Level.addParticle(parameters: ParticleOptions, alwaysSpawn: Boolean, pos: Vec3, velocity: Vec3) {
    this.addParticle(parameters, alwaysSpawn, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
}

fun ServerLevel.spawnParticles(
    particle: ParticleOptions, pos: Vec3, velocity: Vec3, distance: Double
): Int {
    val particleS2CPacket = ClientboundLevelParticlesPacket(
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
    for (j in this.players().indices) {
        val serverPlayerEntity = this.players()[j]
        if (this.sendToPlayerIfNearby(serverPlayerEntity, distance, pos.x, pos.y, pos.z, particleS2CPacket)) {
            ++i
        }
    }
    return i
}

fun ServerLevel.sendToPlayerIfNearby(
    player: ServerPlayer,
    distance: Double,
    x: Double,
    y: Double,
    z: Double,
    packet: Packet<*>?
): Boolean {
    if (player.level() != this) {
        return false
    } else {
        val blockPos = player.blockPosition()
        if (blockPos.closerToCenterThan(Vec3(x, y, z), distance)) {
            player.connection.send(packet)
            return true
        } else {
            return false
        }
    }
}

fun NormalNoise.sample(blockPos: BlockPos): Double = this.sample(blockPos.toVec3d())

fun NormalNoise.sample(vec3d: Vec3): Double = this.getValue(vec3d.x, vec3d.y, vec3d.z)

fun createCuboidShape(minXZ: Double, minY: Double, maxXZ: Double, maxY: Double): VoxelShape {
    return Block.box(minXZ, minY, minXZ, maxXZ, maxY, maxXZ)
}

fun createCuboidShape(min1: Double, min2: Double, max1: Double, max2: Double, axis: Direction.Axis): VoxelShape {
    return when (axis) {
        Direction.Axis.Y -> createCuboidShape(min1, min2, max1, max2)
        Direction.Axis.X -> Block.box(min2, min1, min1, max2, max1, max1)
        Direction.Axis.Z -> Block.box(min1, min1, min2, max1, max1, max2)
    }
}

fun Vec3.normalizeHorizontal(yMult: Double = 1.0): Vec3 {
    val xz = sqrt(this.x * this.x + this.z * this.z)
    val y = this.y * yMult
    return if (xz < 1.0E-4) Vec3(0.0, y, 0.0)
    else Vec3(this.x / xz, y, this.z / xz)
}

fun Vec3.getSquaredDistanceToCenter(vec: Vec3): Double {
    val d: Double = this.x - vec.x
    val e: Double = this.y - vec.y
    val f: Double = this.z - vec.z
    return d * d + e * e + f * f
}

fun Vec3.toBlockPos(): BlockPos {
    return BlockPos(floor(this.x).toInt(), floor(this.y).toInt(), floor(this.z).toInt())
}

fun Vec3i.toVec3d(): Vec3 {
    return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
}

fun box(double: Double): AABB {
    return AABB(-double, -double, -double, double, double, double)
}

fun LevelSimulatedReader.isInSet(pos: BlockPos, tag: HolderSet<Block>): Boolean = this.isStateAtPosition(pos) { it.`is`(tag) }
fun LevelSimulatedReader.isInTag(pos: BlockPos, tag: TagKey<Block>): Boolean = this.isStateAtPosition(pos) { it.`is`(tag) }

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
    val shapes = arrayOf(shape, Shapes.empty())
    for (i in 0 until times) {
        shapes[0].forAllBoxes { minX, minY, minZ, maxX, maxY, maxZ ->
            shapes[1] = Shapes.or(shapes[1], Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX))
        }
        shapes[0] = shapes[1]
        shapes[1] = Shapes.empty()
    }
    return shapes[0]
}

fun rotateVoxelShapeY(times: Int, shape: VoxelShape): VoxelShape {
    val shapes = arrayOf(shape, Shapes.empty())
    for (i in 0 until times) {
        shapes[0].forAllBoxes { minX, minY, minZ, maxX, maxY, maxZ ->
            shapes[1] = Shapes.or(shapes[1], Shapes.box(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY))
        }
        shapes[0] = shapes[1]
        shapes[1] = Shapes.empty()
    }
    return shapes[0]
}


fun Direction.asProperty(): BooleanProperty {
    return when (this) {
        Direction.UP -> BlockStateProperties.UP
        Direction.DOWN -> BlockStateProperties.DOWN
        Direction.NORTH -> BlockStateProperties.NORTH
        Direction.SOUTH -> BlockStateProperties.SOUTH
        Direction.WEST -> BlockStateProperties.WEST
        Direction.EAST -> BlockStateProperties.EAST
    }
}

