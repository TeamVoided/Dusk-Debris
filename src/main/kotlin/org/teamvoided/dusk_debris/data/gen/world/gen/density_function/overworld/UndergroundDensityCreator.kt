package org.teamvoided.dusk_debris.data.gen.world.gen.density_function.overworld

import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.Spline
import net.minecraft.world.gen.DensityFunction
import net.minecraft.world.gen.DensityFunctions
import net.minecraft.world.gen.noise.NoiseParametersKeys
import net.minecraft.world.gen.noise.NoiseRouterData
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.dense
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.noiseHold
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.OverworldDensityFunctionCreator.flatCacheNoi2D
import org.teamvoided.dusk_debris.data.gen.world.gen.density_function.OverworldDensityFunctionCreator.wrap
import org.teamvoided.dusk_debris.data.worldgen.DuskDensityFunctions
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseParametersKeys
import org.teamvoided.dusk_debris.util.world_helper.add
import org.teamvoided.dusk_debris.world.gen.terrain_parameters.OverworldTerrainCreator
import voidlib.devin.world.gen.*

object UndergroundDensityCreator {
    fun BootstrapContext<*>.underground(slopedCheese: DensityFunction): DensityFunction {
        val surfaceOrCave = add(
            min(
                this.dense(DuskDensityFunctions.LAKE_CAVE_DENSITY),
                add(
                    0.27,
                    noise(this.noiseHold(NoiseParametersKeys.CAVE_CHEESE), 0.6666666666666666)
                )
            ).clamp(-1.0, 1.0),
            add(
                1.5,
                multiply(
                    -0.64,
                    slopedCheese
                )
            ).clamp(0.0, 0.5)
        )
        val caveLayerNoise = noise(this.noiseHold(NoiseParametersKeys.CAVE_LAYER), 8.0)
        val caveLayer = multiply(4, caveLayerNoise.square())
        val entrances = min(
            min(
                add(
                    caveLayer,
                    surfaceOrCave
                ),
                this.dense(NoiseRouterData.CAVES_ENTRANCES_OVERWORLD)
            ),
            add(
                this.dense(NoiseRouterData.CAVES_SPAGHETTI_2D_OVERWORLD),
                this.dense(NoiseRouterData.CAVES_SPAGHETTI_ROUGHNESS_FUNCTION_OVERWORLD)
            )
        )
        val cavePillars = this.dense(NoiseRouterData.CAVES_PILLARS_OVERWORLD)
        val cavePillarsRange = rangeChoice(
            cavePillars,
            -1000000,
            0.03,
            const(-1000000),
            cavePillars
        )
        return max(entrances, cavePillarsRange)
    }


    fun BootstrapContext<DensityFunction>.caveRiver(
        data: OverworldTerrainCreator.TerrainParametersData<DensityFunctions.Spline.Point, DensityFunctions.Spline.FunctionWrapper>,
        condition: RegistryKey<DensityFunction>,
        density: RegistryKey<DensityFunction>,
    ) {
        this.register(
            condition,
            rangeChoice(
                this.dense(NoiseRouterData.Y),
                45,
                10000,
                flatCache(cache2D(copySpline(OverworldTerrainCreator.undergroundRiverCondition(data)))),
                const(0)
            )
        )
        this.register(
            density,
            rangeChoice(
                this.dense(condition),
                0.5,
                1.1,
                add(
                    -0.003,
                    add(
                        interpolated(
                            multiply(
                                add(
                                    0.7,
                                    multiply(
                                        0.3,
                                        flatCacheNoi2D(DuskNoiseParametersKeys.UR_HEIGHT)
                                    )
                                ),
                                rangeChoice(
                                    this.dense(DuskDensityFunctions.UR_TYPE),
                                    0,
                                    10000,
                                    clampedGradientY(-10, 62, -1, 0),
                                    clampedGradientY(-10, 138, -1, 1),
                                )
                            )
                        ).square(),
                        interpolated(
                            multiply(
                                this.dense(NoiseRouterData.RIDGES_OVERWORLD),
                                copySpline(
                                    Spline.builder(this.wrap(NoiseRouterData.Y))
                                        .add(60f, 1.5f)
                                        .add(70f, 1.5f)
                                        .add(90f, 3f, 0.05f)
                                        .add(100f, 3.25f, 0.05f)
                                        .build()
                                )
                            )
                        ).square()
                    )
                ),
                const(1000000)
            )
        )
    }

    fun BootstrapContext<DensityFunction>.caveLake() {
        this.register(
            DuskDensityFunctions.LAKE_CAVE_CAVERN,
            cacheOnce(
                add(
                    clampedGradientY(-16, 84, 1.5, -1.5).cube().abs(),
                    noise(
                        this.noiseHold(DuskNoiseParametersKeys.LAKE_CAVE_CAVERNS),
                        1.0,
                        2.0
                    )
                )
            )
        )
    }

    fun BootstrapContext<DensityFunction>.caveLake(
        condition: RegistryKey<DensityFunction>,
        aquifer: RegistryKey<DensityFunction>,
        density: RegistryKey<DensityFunction>,
    ) {
        this.register(
            aquifer,
            rangeChoice(
                this.dense(NoiseRouterData.Y),
                -8,
                32,
                rangeChoice(
                    this.dense(DuskDensityFunctions.LAKE_CAVE_CAVERN),
                    -100000,
                    0.2,
                    const(1),
                    const(0)
                ),
                const(0)
            )
        )
        this.register(
            density,
            add(
                this.dense(DuskDensityFunctions.LAKE_CAVE_CAVERN),
                multiply(
                    0.5,
                    noise(
                        this.noiseHold(DuskNoiseParametersKeys.LAKE_CAVE_PILLARS),
                        1.0,
                        0.05
                    ).square()
                )
            ).clamp(-1.0, 1.0)
        )
    }
}