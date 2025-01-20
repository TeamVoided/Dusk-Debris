package org.teamvoided.dusk_debris.util.extra

import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.random.RandomGenerator
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import org.teamvoided.dusk_debris.util.Utils

class Tree {
    fun branch(
        config: DefaultFeatureConfig,
        world: StructureWorldAccess,
        random: RandomGenerator,
        origin: BlockPos,
        width: Int,
        height: Int
    ) {
        val logBlock = SimpleBlockStateProvider.of(Blocks.WHITE_STAINED_GLASS.defaultState)
        var posY = height - width - random.nextInt(width)
        val height3 = height / 3
        println("$posY, $height3")
        while (posY > height3) {
            val rotation = random.nextFloat() * Utils.rotate360
//            val axis = ((rotation - rotate45) * (180 / pi)) / 90f
//            val axis = if ((rotation + rotate45) % pi > rotate90) {
//                Direction.Axis.Z
//            } else {
//                Direction.Axis.X
//            }

            var rotX = 0
            var rotZ = 0
            val angleY = 1 + random.nextInt(4)
            val the = width / 2
            for (offset in the..the + (0.4 * (height - posY)).toInt()) {
                rotX = (1.5f + MathHelper.cos(rotation) * offset).toInt()
                rotZ = (1.5f + MathHelper.sin(rotation) * offset).toInt()
                val blockPos = origin.add(rotX, posY - offset / angleY, rotZ)
//                this.placeTrunkBlock(world, replacer, random, blockPos, config)
//                this.setBlockState(
//                    world, blockPos,
//                    logBlock.getBlockState(random, blockPos)
////                        .withIfExists(Properties.AXIS, axis)
//                )
            }
            posY -= 1 + random.nextInt(2)
            println("$posY, $height3")
        }
        println(" ")
    }
}