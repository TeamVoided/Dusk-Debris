package org.teamvoided.dusk_debris.block

import net.minecraft.block.BlockState
import net.minecraft.block.MushroomBlock
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.World

class NethershroomBlock(settings: Settings) : MushroomBlock(settings) {
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: RandomGenerator) {
        if (random.nextDouble() >= 0.8) {
            val x = pos.x
            val y = pos.y
            val z = pos.z
            val xOffset = x.toDouble() + random.nextDouble()
            val yOffset = y.toDouble()
            val zOffset = z.toDouble() + random.nextDouble()
            world.addParticle(
                ParticleTypes.FALLING_SPORE_BLOSSOM,
                xOffset,
                yOffset,
                zOffset,
                0.0,
                0.0,
                0.0
            )


            val mutable = BlockPos.Mutable()
            val particleRange = 10
            val blockState = world.getBlockState(mutable)
            mutable[
                x + MathHelper.nextInt(random, -particleRange, particleRange),
                y + MathHelper.nextInt(
                    random,
                    -particleRange - (particleRange / 2),
                    particleRange - (particleRange / 2)
                )] =
                z + MathHelper.nextInt(random, -particleRange, particleRange)
            if (!blockState.isFullCube(world, mutable)) {
                world.addParticle(
                    ParticleTypes.SPORE_BLOSSOM_AIR,
                    mutable.x.toDouble() + random.nextDouble(),
                    mutable.y.toDouble() + random.nextDouble(),
                    mutable.z.toDouble() + random.nextDouble(),
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }
}