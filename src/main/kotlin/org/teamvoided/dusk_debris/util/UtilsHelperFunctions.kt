package org.teamvoided.dusk_debris.util

import net.minecraft.block.Block
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket
import net.minecraft.particle.ParticleEffect
import net.minecraft.registry.HolderSet
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.TestableWorld
import net.minecraft.world.World
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

fun createCuboidShape(minXZ: Double, minY: Double, maxXZ: Double, maxY: Double): VoxelShape {
    return Block.createCuboidShape(minXZ, minY, minXZ, maxXZ, maxY, maxXZ)
}

fun Vec3d.normalize(range: Float): Vec3d {
    val d = sqrt(this.x * this.x + (this.y * this.y) + (this.z * this.z))
    return if (d < 1.0E-4) Vec3d.ZERO else Vec3d((x * range) / d, (y * range) / d, (z * range) / d)
}

fun TestableWorld.isInSet(pos: BlockPos, tag: HolderSet<Block>): Boolean = this.testBlockState(pos) { it.isIn(tag) }
fun TestableWorld.isInTag(pos: BlockPos, tag: TagKey<Block>): Boolean = this.testBlockState(pos) { it.isIn(tag) }

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

fun VoxelShape.rotate(times: Int) = rotateVoxelShape(times, this)