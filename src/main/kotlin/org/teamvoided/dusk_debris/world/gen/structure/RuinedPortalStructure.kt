//package org.teamvoided.dusk_debris.world.gen.structure
//
//import com.google.common.collect.ImmutableList
//import com.mojang.datafixers.kinds.App
//import com.mojang.serialization.Codec
//import com.mojang.serialization.MapCodec
//import com.mojang.serialization.codecs.RecordCodecBuilder
//import net.minecraft.registry.Holder
//import net.minecraft.structure.StructurePiecesCollector
//import net.minecraft.structure.StructureType
//import net.minecraft.structure.piece.RuinedPortalStructurePiece
//import net.minecraft.util.BlockMirror
//import net.minecraft.util.BlockRotation
//import net.minecraft.util.Identifier
//import net.minecraft.util.Util
//import net.minecraft.util.dynamic.Codecs
//import net.minecraft.util.math.BlockBox
//import net.minecraft.util.math.BlockPos
//import net.minecraft.util.math.MathHelper
//import net.minecraft.util.random.RandomGenerator
//import net.minecraft.world.HeightLimitView
//import net.minecraft.world.Heightmap
//import net.minecraft.world.biome.Biome
//import net.minecraft.world.biome.source.BiomeCoords
//import net.minecraft.world.gen.ChunkRandom
//import net.minecraft.world.gen.RandomState
//import net.minecraft.world.gen.chunk.ChunkGenerator
//import net.minecraft.world.gen.chunk.VerticalBlockSample
//import net.minecraft.world.gen.feature.StructureFeature
//import java.util.*
//import java.util.function.BiFunction
//import java.util.function.Function
//import java.util.stream.Collectors
//
//class RuinedPortalStructure : StructureFeature {
//    private var setups: List<Setup>? = null
//
//    constructor(settings: StructureSettings, setups: List<Setup>) : super(settings) {
//        this.setups = setups
//    }
//
//    constructor(settings: StructureSettings, setup: Setup) : this(settings, listOf<Setup>(setup))
//
//    public override fun findGenerationPos(context: GenerationContext): Optional<GenerationStub> {
//        val properties = RuinedPortalStructurePiece.Properties()
//        val chunkRandom = context.random()
//        var setup: Setup? = null
//        if (setups!!.size > 1) {
//            var f = 0.0f
//
//            var setup2: Setup
//            val var6: Iterator<*> = setups!!.iterator()
//            while (var6.hasNext()) {
//                setup2 = var6.next() as Setup
//                f += setup2.weight()
//            }
//
//            var g = chunkRandom.nextFloat()
//            val var22: Iterator<*> = setups!!.iterator()
//
//            while (var22.hasNext()) {
//                val setup3 = var22.next() as Setup
//                g -= setup3.weight() / f
//                if (g < 0.0f) {
//                    setup = setup3
//                    break
//                }
//            }
//        } else {
//            setup = setups!!.first as Setup
//        }
//
//        checkNotNull(setup)
//        properties.airPocket = sampleRandom(chunkRandom, setup.airPocketChance())
//        properties.mossiness = setup.mossiness()
//        properties.overgrown = setup.overgrown()
//        properties.vines = setup.vines()
//        properties.replaceWithBlackstone = setup.replaceWithBlackstone()
//        val identifier = if (chunkRandom.nextFloat() < 0.05f) {
//            Identifier.ofDefault(GIANT_PORTAL_STRUCTURE_IDS[chunkRandom.nextInt(GIANT_PORTAL_STRUCTURE_IDS.size)])
//        } else {
//            Identifier.ofDefault(PORTAL_STRUCTURE_IDS[chunkRandom.nextInt(PORTAL_STRUCTURE_IDS.size)])
//        }
//
//        val structure = context.structureTemplateManager().getStructureOrBlank(identifier)
//        val blockRotation = Util.getRandom(BlockRotation.entries.toTypedArray(), chunkRandom) as BlockRotation
//        val blockMirror = if (chunkRandom.nextFloat() < 0.5f) BlockMirror.NONE else BlockMirror.FRONT_BACK
//        val blockPos = BlockPos(structure.size.x / 2, 0, structure.size.z / 2)
//        val chunkGenerator = context.chunkGenerator()
//        val heightLimitView = context.world()
//        val randomState = context.randomState()
//        val blockPos2 = context.chunkPos().startPos
//        val blockBox = structure.calculateBoundingBox(blockPos2, blockRotation, blockPos, blockMirror)
//        val blockPos3 = blockBox.center
//        val i = chunkGenerator.getHeight(
//            blockPos3.x,
//            blockPos3.z,
//            RuinedPortalStructurePiece.getHeightmapType(setup.placement()),
//            heightLimitView,
//            randomState
//        ) - 1
//        val j = findSuitableY(
//            chunkRandom,
//            chunkGenerator,
//            setup.placement(),
//            properties.airPocket,
//            i,
//            blockBox.blockCountY,
//            blockBox,
//            heightLimitView,
//            randomState
//        )
//        val blockPos4 = BlockPos(blockPos2.x, j, blockPos2.z)
//        return Optional.of(GenerationStub(blockPos4) { collector: StructurePiecesCollector ->
//            if (setup.canBeCold()) {
//                properties.cold = isCold(
//                    blockPos4,
//                    context.chunkGenerator().biomeSource.getNoiseBiome(
//                        BiomeCoords.fromBlock(blockPos4.x),
//                        BiomeCoords.fromBlock(blockPos4.y),
//                        BiomeCoords.fromBlock(blockPos4.z),
//                        randomState.sampler
//                    )
//                )
//            }
//            collector.addPiece(
//                RuinedPortalStructurePiece(
//                    context.structureTemplateManager(),
//                    blockPos4,
//                    setup.placement(),
//                    properties,
//                    identifier,
//                    structure,
//                    blockRotation,
//                    blockMirror,
//                    blockPos
//                )
//            )
//        })
//    }
//
//    override fun getType(): StructureType<*> {
//        return StructureType.RUINED_PORTAL
//    }
//
//    @JvmRecord
//    data class Setup(
//        val placement: RuinedPortalStructurePiece.VerticalPlacement,
//        val airPocketChance: Float,
//        val mossiness: Float,
//        val overgrown: Boolean,
//        val vines: Boolean,
//        val canBeCold: Boolean,
//        val replaceWithBlackstone: Boolean,
//        val weight: Float
//    ) {
//        fun placement(): RuinedPortalStructurePiece.VerticalPlacement {
//            return this.placement
//        }
//
//        fun airPocketChance(): Float {
//            return this.airPocketChance
//        }
//
//        fun mossiness(): Float {
//            return this.mossiness
//        }
//
//        fun overgrown(): Boolean {
//            return this.overgrown
//        }
//
//        fun vines(): Boolean {
//            return this.vines
//        }
//
//        fun canBeCold(): Boolean {
//            return this.canBeCold
//        }
//
//        fun replaceWithBlackstone(): Boolean {
//            return this.replaceWithBlackstone
//        }
//
//        fun weight(): Float {
//            return this.weight
//        }
//
//        companion object {
//            val CODEC: Codec<Setup> = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<Setup> ->
//                instance.group(
//                    RuinedPortalStructurePiece.VerticalPlacement.CODEC.fieldOf("placement")
//                        .forGetter { obj: Setup -> obj.placement() },
//                    Codec.floatRange(0.0f, 1.0f).fieldOf("air_pocket_probability")
//                        .forGetter { obj: Setup -> obj.airPocketChance() },
//                    Codec.floatRange(0.0f, 1.0f).fieldOf("mossiness").forGetter { obj: Setup -> obj.mossiness() },
//                    Codec.BOOL.fieldOf("overgrown").forGetter { obj: Setup -> obj.overgrown() },
//                    Codec.BOOL.fieldOf("vines").forGetter { obj: Setup -> obj.vines() },
//                    Codec.BOOL.fieldOf("can_be_cold").forGetter { obj: Setup -> obj.canBeCold() },
//                    Codec.BOOL.fieldOf("replace_with_blackstone")
//                        .forGetter { obj: Setup -> obj.replaceWithBlackstone() },
//                    Codecs.POSITIVE_FLOAT.fieldOf("weight").forGetter { obj: Setup -> obj.weight() })
//                    .apply(instance) { placement: RuinedPortalStructurePiece.VerticalPlacement, airPocketChance: Float, mossiness: Float, overgrown: Boolean, vines: Boolean, canBeCold: Boolean, replaceWithBlackstone: Boolean, weight: Float ->
//                        Setup(
//                            placement,
//                            airPocketChance,
//                            mossiness,
//                            overgrown,
//                            vines,
//                            canBeCold,
//                            replaceWithBlackstone,
//                            weight
//                        )
//                    }
//            }
//        }
//    }
//
//    companion object {
//        private val PORTAL_STRUCTURE_IDS = arrayOf(
//            "ruined_portal/portal_1",
//            "ruined_portal/portal_2",
//            "ruined_portal/portal_3",
//            "ruined_portal/portal_4",
//            "ruined_portal/portal_5",
//            "ruined_portal/portal_6",
//            "ruined_portal/portal_7",
//            "ruined_portal/portal_8",
//            "ruined_portal/portal_9",
//            "ruined_portal/portal_10"
//        )
//        private val GIANT_PORTAL_STRUCTURE_IDS =
//            arrayOf("ruined_portal/giant_portal_1", "ruined_portal/giant_portal_2", "ruined_portal/giant_portal_3")
//        private const val GIANT_PORTAL_CHANCE = 0.05f
//        private const val MIN_Y_INDEX = 15
//        val CODEC: MapCodec<RuinedPortalStructure> =
//            RecordCodecBuilder.mapCodec { instance ->
//                instance.group<StructureSettings, List<Setup>>(
//                    settingsCodec(instance),
//                    Codecs.withNonEmptyList(Setup.CODEC.listOf()).fieldOf("setups").forGetter { it.setups }
//                ).apply(instance, ::RuinedPortalStructure)
//            }
//
//        private fun sampleRandom(random: ChunkRandom, chance: Float): Boolean {
//            return if (chance == 0.0f) {
//                false
//            } else if (chance == 1.0f) {
//                true
//            } else {
//                random.nextFloat() < chance
//            }
//        }
//
//        private fun isCold(pos: BlockPos, biome: Holder<Biome>): Boolean {
//            return (biome.value() as Biome).isColdEnoughToSnow(pos)
//        }
//
//        private fun findSuitableY(
//            random: RandomGenerator,
//            chunkGenerator: ChunkGenerator,
//            verticalPlacement: RuinedPortalStructurePiece.VerticalPlacement,
//            airPocket: Boolean,
//            height: Int,
//            blockCountY: Int,
//            boundingBox: BlockBox,
//            world: HeightLimitView,
//            randomState: RandomState
//        ): Int {
//            val i = world.bottomY + 15
//            val j: Int
//            if (verticalPlacement == RuinedPortalStructurePiece.VerticalPlacement.IN_NETHER) {
//                j = if (airPocket) {
//                    MathHelper.nextBetween(random, 32, 100)
//                } else if (random.nextFloat() < 0.5f) {
//                    MathHelper.nextBetween(random, 27, 29)
//                } else {
//                    MathHelper.nextBetween(random, 29, 100)
//                }
//            } else {
//                val k: Int
//                if (verticalPlacement == RuinedPortalStructurePiece.VerticalPlacement.IN_MOUNTAIN) {
//                    k = height - blockCountY
//                    j = getRandomInt(random, 70, k)
//                } else if (verticalPlacement == RuinedPortalStructurePiece.VerticalPlacement.UNDERGROUND) {
//                    k = height - blockCountY
//                    j = getRandomInt(random, i, k)
//                } else if (verticalPlacement == RuinedPortalStructurePiece.VerticalPlacement.PARTLY_BURIED) {
//                    j = height - blockCountY + MathHelper.nextBetween(random, 2, 8)
//                } else {
//                    j = height
//                }
//            }
//
//            val list: List<BlockPos> = ImmutableList.of(
//                BlockPos(boundingBox.minX, 0, boundingBox.minZ),
//                BlockPos(boundingBox.maxX, 0, boundingBox.minZ),
//                BlockPos(boundingBox.minX, 0, boundingBox.maxZ),
//                BlockPos(boundingBox.maxX, 0, boundingBox.maxZ)
//            )
//            val list2: List<VerticalBlockSample> =
//                list.stream().map { chunkGenerator.getColumnSample(it.x, it.z, world, randomState) }
//                    .collect(Collectors.toList())
//            val type =
//                if (verticalPlacement == RuinedPortalStructurePiece.VerticalPlacement.ON_OCEAN_FLOOR) Heightmap.Type.OCEAN_FLOOR_WG else Heightmap.Type.WORLD_SURFACE_WG
//            var l = j
//            while (l > i) {
//                var m = 0
//                list2.forEach {
//                    val blockState = it.getState(l)
//                    if (type.blockPredicate.test(blockState)) {
//                        ++m
//                        if (m == 3) {
//                            return l
//                        }
//                    }
//                }
//
//                --l
//            }
//
//            return l
//        }
//
//        private fun getRandomInt(random: RandomGenerator, min: Int, max: Int): Int {
//            return if (min < max) MathHelper.nextBetween(random, min, max) else max
//        }
//    }
//}
