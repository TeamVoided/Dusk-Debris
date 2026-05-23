package org.teamvoided.dusk_debris.entity.helper

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.BlockCollisions
import net.minecraft.world.level.CollisionGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape
import org.joml.Vector2d
import org.teamvoided.dusk_debris.util.Utils.vec3d
import org.teamvoided.dusk_debris.util.hangingDirection
import org.teamvoided.dusk_debris.util.isHanging

object ClawLogic {
    @JvmStatic
    fun checkIfOnWall(entity: LivingEntity) {
        val box: AABB = entity.boundingBox
//        val box2 = Box(
//            box.minX, box.minY - 1.0E-6, box.minZ,
//            box.maxX, box.minY, box.maxZ
//        )
        val y2 = box.ysize * 0.25
        val y = (box.minY + y2 to box.maxY - y2)
        val boxSouth = AABB(
            box.minX,
            y.first,
            box.maxZ,
            box.maxX,
            y.second,
            box.maxZ + 1.0E-6
        )
        val boxNorth = AABB(
            box.minX,
            y.first,
            box.minZ - 1.0E-6,
            box.maxX,
            y.second,
            box.minZ
        )
        val boxEast = AABB(
            box.maxX,
            y.first,
            box.minZ,
            box.maxX + 1.0E-6,
            y.second,
            box.maxZ
        )
        val boxWest = AABB(
            box.minX - 1.0E-6,
            y.first,
            box.minZ,
            box.minX,
            y.second,
            box.maxZ
        )
        var dir = Vec3.ZERO
        val world = entity.level()
        if (world.collidesWithBlock(entity, boxSouth))
            dir = dir.add(Direction.SOUTH.normal.vec3d())
        else if (world.collidesWithBlock(entity, boxNorth))
            dir = dir.add(Direction.NORTH.normal.vec3d())
        if (world.collidesWithBlock(entity, boxEast))
            dir = dir.add(Direction.EAST.normal.vec3d())
        else if (world.collidesWithBlock(entity, boxWest))
            dir = dir.add(Direction.WEST.normal.vec3d())
        entity.hangingDirection = dir
        entity.isHanging = dir != Vec3.ZERO
    }

    @JvmStatic
    fun particles(entity: LivingEntity) {
        val world = entity.level()
        if (world.isClientSide && entity.isHanging) {
            val box: AABB = entity.boundingBox
            val dir = entity.hangingDirection
            val pos = Vector2d(
                if (dir.x < 0) box.minX else if (dir.x > 0) box.maxX else 0.0,
                if (dir.z < 0) box.minZ else if (dir.z > 0) box.maxZ else 0.0
            ).add(entity.position().x, entity.position().z)
            world.addParticle(
                BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.GRAVEL.defaultBlockState()),
                pos.x,
                entity.eyeY,
                pos.y,
                dir.x * 0.1,
                -entity.random.nextDouble() * 0.04,
                dir.z * 0.1
            )
        }
    }

    private fun CollisionGetter.collidesWithBlock(entity: Entity, box: AABB): Boolean {
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
        if (entity.isHanging && entity.deltaMovement.y < 0) {
            entity.setDeltaMovement(entity.deltaMovement.multiply(0.1, 0.8, 0.1))
            return 0.01
        }
        return entity.gravity
    }
}