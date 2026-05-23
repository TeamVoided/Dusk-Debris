package org.teamvoided.dusk_debris.util

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import org.teamvoided.dusk_debris.DuskDebris
import org.teamvoided.dusk_debris.block.not_blocks.DuskProperties
import org.teamvoided.dusk_debris.block.sot.CoinPileBlock
import org.teamvoided.dusk_debris.block.sot.RibbonBlock
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.particle.color.NethershroomSporeParticleEffect
import java.util.function.ToIntFunction


fun Block.cutout(): Block {
    DuskBlocks.CUTOUT_BLOCKS.add(this)
    return this
}

fun Block.translucent(): Block {
    DuskBlocks.TRANSLUCENT_BLOCKS.add(this)
    return this
}

fun Block.grassTint(): Block {
    DuskBlocks.GRASS_TINT_BLOCKS.add(this)
    return this
}


val vesselBlockSound = SoundType(
    1f,
    2f,
    SoundEvents.TRIAL_SPAWNER_BREAK,
    SoundEvents.TRIAL_SPAWNER_STEP,
    SoundEvents.TRIAL_SPAWNER_PLACE,
    SoundEvents.TRIAL_SPAWNER_HIT,
    SoundEvents.TRIAL_SPAWNER_FALL
)

val charredLogColor: MapColor = MapColor.COLOR_BLACK
val charredPlanksColor: MapColor = MapColor.DEEPSLATE

val charredSignId : ResourceLocation = DuskDebris.id("entity/signs/charred")
val charredHangingSignId: ResourceLocation = DuskDebris.id("entity/signs/hanging/charred")
val cypressSignId : ResourceLocation = DuskDebris.id("entity/signs/cypress")
val cypressHangingSignId : ResourceLocation = DuskDebris.id("entity/signs/hanging/cypress")
val sequoiaSignId: ResourceLocation = DuskDebris.id("entity/signs/sequoia")
val sequoiaHangingSignId : ResourceLocation = DuskDebris.id("entity/signs/hanging/sequoia")

val blueNethershroomSmoke = NethershroomSporeParticleEffect(0x39A2DB)
val purpleNethershroomSmoke = NethershroomSporeParticleEffect(0x573AD8)
val smokebombSmoke = NethershroomSporeParticleEffect(0x7F7F7F)
val gunpowderBarrelColor = 0xF7C53B
val gunpowderBarrelBlueColor = 0x7FD4FF
val bonecallerBlockSettings =
    BlockBehaviour.Properties.of().mapColor(Blocks.BONE_BLOCK.defaultMapColor()).sound(SoundType.GLASS)
        .instrument(NoteBlockInstrument.HAT).strength(1f, 0.0f).isRedstoneConductor(Blocks::never)
        .pushReaction(PushReaction.DESTROY)
val coin_stack_settings = BlockBehaviour.Properties.of().forceSolidOff().strength(0.2f)
    .pushReaction(PushReaction.DESTROY)
val coin_pile_settings = BlockBehaviour.Properties.of().forceSolidOff()
    .strength(0.2f).sound(SoundType.METAL).isViewBlocking { state: BlockState, _, _ ->
        state.getValue(CoinPileBlock.LAYERS) >= CoinPileBlock.MAX_LAYERS
    }.pushReaction(PushReaction.DESTROY)

fun light(lightLevel: Int): ToIntFunction<BlockState> {
    return ToIntFunction { lightLevel }
}

fun godhomeLuminanceOf(mult: Int = 4, add: Int = 7): ToIntFunction<BlockState> {
    return ToIntFunction { state: BlockState -> state.getValue(DuskProperties.GODHOME_BRONZE_PHASE).id * mult + add }
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

fun registerSkull(id: String, skullType: SkullBlock.Type, instrument: NoteBlockInstrument): Block {
    return DuskBlocks.register(
        id, SkullBlock(
            skullType,
            BlockBehaviour.Properties.of().instrument(instrument).strength(1.0f)
                .pushReaction(PushReaction.DESTROY)
        )
    )
}

fun registerWallSkull(id: String, skullType: SkullBlock.Type, dropsLike: Block): Block {
    return DuskBlocks.register(
        id,
        WallSkullBlock(
            skullType,
            BlockBehaviour.Properties.of().strength(1.0f).dropsLike(dropsLike)
                .pushReaction(PushReaction.DESTROY)
        )
    )
}

fun registerRibbon(mapColor: MapColor): Block {
    return RibbonBlock(
        BlockBehaviour.Properties.of().mapColor(mapColor).strength(0.1F)
            .sound(SoundType.WOOL).isRedstoneConductor(Blocks::never)
    )
}

fun charredLogOf(topColor: MapColor, sideColor: MapColor): Block {
    return RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor { state: BlockState ->
        if (state.getValue(
                RotatedPillarBlock.AXIS
            ) === Direction.Axis.Y
        ) topColor else sideColor
    }.instrument(NoteBlockInstrument.BASS).strength(2.0f).sound(SoundType.WOOD))
}