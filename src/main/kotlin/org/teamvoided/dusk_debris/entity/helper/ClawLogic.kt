package org.teamvoided.dusk_debris.entity.helper

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockCollisions
import net.minecraft.world.CollisionView
import org.teamvoided.dusk_debris.util.Utils.vec3d
import org.teamvoided.dusk_debris.util.hangingDirection
import org.teamvoided.dusk_debris.util.isHanging
import java.util.function.BiFunction

object ClawLogic {
    @JvmStatic
    fun checkIfOnWall(entity: LivingEntity) {
        val box: Box = entity.bounds
//        val box2 = Box(
//            box.minX, box.minY - 1.0E-6, box.minZ,
//            box.maxX, box.minY, box.maxZ
//        )
        val y2 = box.yLength * 0.25
        val y = (box.minY + y2 to box.maxY - y2)
        val boxSouth = Box(
            box.minX,
            y.first,
            box.maxZ,
            box.maxX,
            y.second,
            box.maxZ + 1.0E-6
        )
        val boxNorth = Box(
            box.minX,
            y.first,
            box.minZ - 1.0E-6,
            box.maxX,
            y.second,
            box.minZ
        )
        val boxEast = Box(
            box.maxX,
            y.first,
            box.minZ,
            box.maxX + 1.0E-6,
            y.second,
            box.maxZ
        )
        val boxWest = Box(
            box.minX - 1.0E-6,
            y.first,
            box.minZ,
            box.minX,
            y.second,
            box.maxZ
        )
        var dir = Vec3d.ZERO
        val world = entity.world
        if (world.collidesWithBlock(entity, boxSouth))
            dir = dir.add(Direction.SOUTH.vector.vec3d())
        if (world.collidesWithBlock(entity, boxNorth))
            dir = dir.add(Direction.NORTH.vector.vec3d())
        if (world.collidesWithBlock(entity, boxEast))
            dir = dir.add(Direction.EAST.vector.vec3d())
        if (world.collidesWithBlock(entity, boxWest))
            dir = dir.add(Direction.WEST.vector.vec3d())
        entity.hangingDirection = dir.normalize()
    }

    private fun CollisionView.collidesWithBlock(entity: Entity, box: Box): Boolean {
        val blockCollisions: BlockCollisions<VoxelShape> = BlockCollisions(
            this, entity, box, false
        ) { _: BlockPos, shape: VoxelShape -> shape }

        do {
            if (!blockCollisions.hasNext()) {
                return false
            }
        } while ((blockCollisions.next())!!.isEmpty)

        return true
    }

    @JvmStatic
    fun slideDown(entity: LivingEntity): Double {
        if (entity.isHanging && entity.velocity.y < 0) {
            entity.velocity = entity.velocity.multiply(0.1, 0.8, 0.1)
            return 0.01
        }
        return entity.gravity
    }
}