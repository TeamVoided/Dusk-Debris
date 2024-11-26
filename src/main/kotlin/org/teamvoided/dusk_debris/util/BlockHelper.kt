package org.teamvoided.dusk_debris.util

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry
import net.minecraft.block.*
import net.minecraft.block.enums.NoteBlockInstrument
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.block.CoinPileBlock
import org.teamvoided.dusk_debris.block.RibbonBlock
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.block.not_blocks.GodhomeBronzePhase
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.particle.NethershroomSporeParticleEffect
import java.util.function.ToIntFunction


fun Block.cutout(): Block {
    DuskBlocks.CUTOUT_BLOCKS.add(this)
    return this
}

fun Block.translucent(): Block {
    DuskBlocks.TRANSLUCENT_BLOCKS.add(this)
    return this
}


val vesselBlockSound = BlockSoundGroup(
    1f,
    2f,
    SoundEvents.BLOCK_TRIAL_SPAWNER_BREAK,
    SoundEvents.BLOCK_TRIAL_SPAWNER_STEP,
    SoundEvents.BLOCK_TRIAL_SPAWNER_PLACE,
    SoundEvents.BLOCK_TRIAL_SPAWNER_HIT,
    SoundEvents.BLOCK_TRIAL_SPAWNER_FALL
)

val charredLogColor = MapColor.BLACK
val charredPlanksColor = MapColor.DEEPSLATE

val charredSignId = DuskDebris.id("entity/signs/charred")
val cypressSignId = DuskDebris.id("entity/signs/cypress")
val charredHangingSignId = DuskDebris.id("entity/signs/hanging/charred")
val cypressHangingSignId = DuskDebris.id("entity/signs/hanging/cypress")

val blueNethershroomSmoke = NethershroomSporeParticleEffect(0x39A2DB)
val purpleNethershroomSmoke = NethershroomSporeParticleEffect(0x573AD8)
val smokebombSmoke = NethershroomSporeParticleEffect(0x7F7F7F)
val gunpowderBarrelColor = 0xF7C53B
val gunpowderBarrelBlueColor = 0x7FD4FF
val bonecallerBlockSettings =
    AbstractBlock.Settings.create().mapColor(Blocks.BONE_BLOCK.defaultMapColor).sounds(BlockSoundGroup.GLASS)
        .instrument(NoteBlockInstrument.HAT).strength(1f, 0.0f).solidBlock(Blocks::nonSolid)
        .pistonBehavior(PistonBehavior.DESTROY)
val coin_stack_settings = AbstractBlock.Settings.create().nonSolid().strength(0.2f)
    .pistonBehavior(PistonBehavior.DESTROY)
val coin_pile_settings = AbstractBlock.Settings.create().nonSolid()
    .strength(0.2f).sounds(BlockSoundGroup.METAL).blockVision { state: BlockState, _, _ ->
        state.get(CoinPileBlock.LAYERS) >= CoinPileBlock.MAX_LAYERS
    }.pistonBehavior(PistonBehavior.DESTROY)

fun light(lightLevel: Int): ToIntFunction<BlockState> {
    return ToIntFunction { lightLevel }
}

fun godhomeLuminanceOf(mult: Int = 4, add: Int = 7): ToIntFunction<BlockState> {
    return ToIntFunction { state: BlockState -> state.get(DuskProperties.GODHOME_BRONZE_PHASE).id * mult + add }
}

fun oxidizeCopperSet(copperList: List<Pair<Block, Block>>) {
    copperList.forEachIndexed { idx, (regular: Block, waxed: Block) ->
        if (idx < copperList.size - 1) {
            OxidizableBlocksRegistry.registerOxidizableBlockPair(
                regular,
                copperList[idx + 1].first
            )
        }
        OxidizableBlocksRegistry.registerWaxableBlockPair(
            regular,
            waxed
        )
    }
}

fun registerSkull(id: String, skullType: SkullBlock.SkullType, instrument: NoteBlockInstrument): Block {
    return DuskBlocks.register(
        id, SkullBlock(
            skullType,
            AbstractBlock.Settings.create().instrument(instrument).strength(1.0f)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )
}

fun registerWallSkull(id: String, skullType: SkullBlock.SkullType, dropsLike: Block): Block {
    return DuskBlocks.register(
        id,
        WallSkullBlock(
            skullType,
            AbstractBlock.Settings.create().strength(1.0f).dropsLike(dropsLike)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )
}

fun registerRibbon(mapColor: MapColor): Block {
    return RibbonBlock(
        AbstractBlock.Settings.create().mapColor(mapColor).strength(0.1F)
            .sounds(BlockSoundGroup.WOOL).solidBlock(Blocks::nonSolid)
    )
}

fun charredLogOf(topColor: MapColor, sideColor: MapColor): Block {
    return PillarBlock(AbstractBlock.Settings.create().mapColor { state: BlockState ->
        if (state.get(
                PillarBlock.AXIS
            ) === Direction.Axis.Y
        ) topColor else sideColor
    }.instrument(NoteBlockInstrument.BASS).strength(2.0f).sounds(BlockSoundGroup.WOOD))
}