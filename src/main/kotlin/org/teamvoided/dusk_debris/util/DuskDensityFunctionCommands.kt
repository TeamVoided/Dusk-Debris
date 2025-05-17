package org.teamvoided.dusk_debris.util

import com.mojang.brigadier.context.CommandContext
import net.minecraft.block.Blocks
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.function.ToFloatFunction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Spline
import net.minecraft.world.biome.source.util.VanillaTerrainParametersCreator
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator
import kotlin.math.max
import kotlin.math.min

fun splineCommand(cx: CommandContext<ServerCommandSource>): Int {
    val world = cx.source.world
    //if (!DuskDebris.isDev()) {
    //    world.players.forEach { it.sendMessage(Text.literal("do not run the spline command"), false) }
    //    return 1
    //}
    val xSize = 100
    val zSize = 100
    val xRange = xSize * (3 / 4f)
    val zRange = zSize * (3 / 4f)
    val height = world.dimension.minY..(world.dimension.height - world.dimension.minY)
    for (x in -xSize..xSize) {
        val xSample = sample(x, xRange, -1f, 1f) //x / xRange
        val xAlt = x > xRange || -x > xRange
        for (z in -zSize..zSize) {
            val zSample =
                sample(z, zRange, -1f, 1f) //-3 * (((z / zRange).absoluteValue - (2f / 3f)).absoluteValue - (1f / 3f))
            val zAlt = z > zRange || -z > zRange

            val cont = 1f
            val eros = 0f
            val ridg = 0f
            val riFl = zSample
            val plat = xSample
            val gcrf = riFl
            val data = OverworldTerrainCreator.TerrainParametersData(
                ToFloatFunction.createUnlimited { cont },
                ToFloatFunction.createUnlimited { eros },
                ToFloatFunction.createUnlimited { ridg },
                ToFloatFunction.createUnlimited { riFl },
                ToFloatFunction.createUnlimited { plat },
                ToFloatFunction.createUnlimited { gcrf }
            )
            val spline: Spline<Float, ToFloatFunction<Float>> = OverworldTerrainCreator.offsetSpline(data, false)
            //val spline: Spline<Float, ToFloatFunction<Float>> = VanillaTerrainParametersCreator.method_42056(
            //    data.continents,
            //    data.erosion,
            //    data.ridgesFolded,
            //    false
            //)
            if (spline !is Spline.Multipoint<Float, ToFloatFunction<Float>>) return 1
            val the = spline.apply(0f)

            val yHeight = 128 * (the + 0.5f)
            for (y in height) {
                val block2 =
                    if (yHeight < y)
                        if (y < 63 && !(xAlt || zAlt)) Blocks.BLUE_STAINED_GLASS
                        else Blocks.AIR
                    else if (xAlt || zAlt) Blocks.TINTED_GLASS
                    else Blocks.STONE
                world.setBlockState(BlockPos(x, y, z), block2.defaultState)
            }
        }
    }

    world.players.forEach { it.sendMessage(Text.literal("spline placed"), false) }
    return 1
}

private fun sample(value: Int, range: Float, num1: Float, num2: Float): Float {
    val min = min(num1, num2)
    val max = max(num1, num2)
    return MathHelper.lerp((value / range + 1f) / 2f, min, max)
}