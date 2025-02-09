package org.teamvoided.dusk_debris.data.gen.world.gen

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.BootstrapContext
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.VerticalSurfaceType
import net.minecraft.world.biome.Biomes
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings
import net.minecraft.world.gen.chunk.GenerationShapeConfig
import net.minecraft.world.gen.noise.NoiseParametersKeys
import net.minecraft.world.gen.surfacebuilder.SurfaceRules
import net.minecraft.world.gen.surfacebuilder.SurfaceRules.*
import org.teamvoided.dusk_debris.data.gen.world.gen.DensityFunctionCreator.createNether
import org.teamvoided.dusk_debris.data.worldgen.DuskBiomes
import org.teamvoided.dusk_debris.data.worldgen.DuskNoiseSettings

object NoiseSettingsGenerator {
    //ChunkGeneratorSettings

    fun bootstrap(c: BootstrapContext<ChunkGeneratorSettings>) {
        c.register(DuskNoiseSettings.NETHER, c.createNetherSettings(false, false))
//        c.register(DuskNoiseSettings.NETHER_LARGE_BIOME, createNetherSettings(c, false, true))
//        c.register(DuskNoiseSettings.NETHER_AMPLIFIED, createNetherSettings(c, true, false))

//        c.register(ChunkGeneratorSettings.END, ChunkGeneratorSettings.createEndSettings(c))
//        c.register(
//            ChunkGeneratorSettings.FLOATING_ISLANDS,
//            ChunkGeneratorSettings.createFloatingIslandsSettings(c)
//        )
    }


    private fun BootstrapContext<*>.createNetherSettings(
        amplified: Boolean,
        largeBiomes: Boolean
    ): ChunkGeneratorSettings {
        return ChunkGeneratorSettings(
            GenerationShapeConfig.create(0, 256, 1, 2),
            Blocks.NETHERRACK.defaultState,
            Blocks.LAVA.defaultState,
            this.createNether(amplified, largeBiomes),
            getNetherRules(),
            listOf(),
            32,
            false,
            true,
            false,
            false
        )
    }

    fun getNetherRules(): MaterialRule {
        val lava = block(Blocks.LAVA)
        val gravel = block(Blocks.GRAVEL)
        val bedrock = block(Blocks.BEDROCK)
        val netherrack = block(Blocks.NETHERRACK)
        val netherWartBlock = block(Blocks.NETHER_WART_BLOCK)
        val crimsonNylium = block(Blocks.CRIMSON_NYLIUM)
        val warpedWartBlock = block(Blocks.WARPED_WART_BLOCK)
        val warpedNylium = block(Blocks.WARPED_NYLIUM)
        val basalt = block(Blocks.BASALT)
        val blackstone = block(Blocks.BLACKSTONE)
        val soulSand = block(Blocks.SOUL_SAND)
        val soulSoil = block(Blocks.SOUL_SOIL)

        val netherWastes = biome(Biomes.NETHER_WASTES, DuskBiomes.NETHER_WASTES)
        val crimsonForest =
            biome(Biomes.CRIMSON_FOREST, DuskBiomes.CRIMSON_FOREST, DuskBiomes.CRIMSON_WASTES)
        val warpedForest =
            biome(Biomes.WARPED_FOREST, DuskBiomes.WARPED_FOREST, DuskBiomes.WARPED_WASTES)
        val basaltDelta = biome(Biomes.BASALT_DELTAS, DuskBiomes.BASALT_DELTAS)
        val soulValley = biome(Biomes.SOUL_SAND_VALLEY, DuskBiomes.SOUL_SAND_VALLEY)

        val aboveY31 = aboveY(YOffset.fixed(31), 0)
        val aboveY32 = aboveY(YOffset.fixed(32), 0)
        val aboveY30AndDepth = aboveYWithStoneDepth(YOffset.fixed(30), 0)
        val belowY35AndDepth = not(aboveYWithStoneDepth(YOffset.fixed(35), 0))
        val aboveY5BelowTop = aboveY(YOffset.belowTop(5), 0)
        val hole = hole()
        val soulSandLayer = noiseThreshold(NoiseParametersKeys.SOUL_SAND_LAYER, -0.012)
        val gravelLayer = noiseThreshold(NoiseParametersKeys.GRAVEL_LAYER, -0.012)
        val blackstoneStripsCondition =
            condition(
                noiseThreshold(NoiseParametersKeys.CALCITE, -0.0125, 0.0125),
                blackstone
            )
        val patch = noiseThreshold(NoiseParametersKeys.PATCH, -0.012)
        val netherStateSelector = noiseThreshold(NoiseParametersKeys.NETHER_STATE_SELECTOR, 0.0)
        val atSeaLevel = condition(
            patch,
            condition(
                aboveY30AndDepth,
                condition(belowY35AndDepth, gravel)
            )
        )

        val basaltDeltasSurface = condition(
            basaltDelta,
            sequence(
                condition(
                    UNDER_CEILING,
                    basalt
                ),
                condition(
                    UNDER_FLOOR,
                    sequence(
                        atSeaLevel,
                        condition(netherStateSelector, basalt),
                        blackstone
                    )
                )
            )
        )
        val blackstoneStrips = condition(
            not(
                basaltDelta
            ),
            sequence(
                condition(
                    stoneDepth(0, true, VerticalSurfaceType.FLOOR),
                    blackstoneStripsCondition
                ),
                condition(
                    stoneDepth(0, true, VerticalSurfaceType.CEILING),
                    blackstoneStripsCondition
                )
            )
        )
        val soulValleySurface = condition(
            soulValley,
            sequence(
                condition(
                    UNDER_CEILING,
                    sequence(
                        condition(
                            netherStateSelector,
                            soulSand
                        ), soulSoil
                    )
                ),
                condition(
                    UNDER_FLOOR,
                    sequence(
                        atSeaLevel,
                        condition(netherStateSelector, soulSand),
                        soulSoil
                    )
                )
            )
        )
        val netherwartForestSurface = condition(
            ON_FLOOR,
            sequence(
                condition(
                    not(aboveY32),
                    condition(hole, lava)
                ),
                wartForest(warpedForest, warpedNylium, warpedWartBlock),
                wartForest(crimsonForest, crimsonNylium, netherWartBlock)
            )
        )
        val netherWastesSurface = condition(
            netherWastes,
            sequence(
                condition(
                    UNDER_FLOOR,
                    condition(
                        soulSandLayer,
                        sequence(
                            condition(
                                not(hole),
                                condition(
                                    aboveY30AndDepth,
                                    condition(
                                        belowY35AndDepth,
                                        soulSand
                                    )
                                )
                            ), netherrack
                        )
                    )
                ),
                condition(
                    ON_FLOOR,
                    condition(
                        aboveY31,
                        condition(
                            belowY35AndDepth,
                            condition(
                                gravelLayer,
                                sequence(
                                    condition(
                                        aboveY32,
                                        gravel
                                    ),
                                    condition(
                                        not(hole),
                                        gravel
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        return sequence(
            condition(
                verticalGradient(
                    "bedrock_floor",
                    YOffset.getBottom(),
                    YOffset.aboveBottom(5)
                ), bedrock
            ),
            condition(
                not(
                    verticalGradient(
                        "bedrock_roof",
                        YOffset.belowTop(5),
                        YOffset.getTop()
                    )
                ), bedrock
            ),
            condition(aboveY5BelowTop, netherrack),
            basaltDeltasSurface,
            blackstoneStrips,
            soulValleySurface,
            netherwartForestSurface,
            netherWastesSurface
        )
    }

    fun wartForest(
        biome: MaterialCondition,
        nyliumBlock: MaterialRule,
        wartBlock: MaterialRule
    ): MaterialRule {
        val netherrackNoise = noiseThreshold(NoiseParametersKeys.NETHERRACK, 0.54)
        val netherWartCondition = noiseThreshold(NoiseParametersKeys.NETHER_WART, (1.17 / 2))
        return condition(
            biome,
            condition(
                not(netherrackNoise),
                condition(
                    aboveY(YOffset.fixed(31), 0),
                    sequence(
                        condition(
                            netherWartCondition,
                            wartBlock
                        ), nyliumBlock
                    )
                )
            )
        )
    }

    private fun block(block: Block): MaterialRule {
        return block(block.defaultState)
    }
}