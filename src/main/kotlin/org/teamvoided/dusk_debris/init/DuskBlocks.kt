package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.block.*
import net.minecraft.block.Blocks.legacyStairsOf
import net.minecraft.block.enums.NoteBlockInstrument
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.DuskBlockFamilies
import org.teamvoided.dusk_debris.block.DuskBlockSetType
import org.teamvoided.dusk_debris.block.GunpowderBarrelBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidCeilingHangingSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidWallHangingSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidWallSignBlock

@Suppress("HasPlatformType", "MemberVisibilityCanBePrivate", "unused", "DEPRECATION")
object DuskBlocks {
    val charredLogColor = MapColor.BLACK
    val charredPlanksColor = MapColor.DEEPSLATE

    val charredSignId = id("entity/signs/charred")
    val cypressSignId = id("entity/signs/cypress")

    val charredHangingSignId = id("entity/signs/hanging/charred")
    val cypressHangingSignId = id("entity/signs/hanging/cypress")

    val GUNPOWDER_BARREL = register(
        "gunpowder_barrel",
        GunpowderBarrelBlock(
            4,
            AbstractBlock.Settings.create().mapColor(MapColor.FIRE).instrument(NoteBlockInstrument.BASS)
                .strength(1f, 0.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable().solidBlock(Blocks::nonSolid)
        )
    )
    val STRONGHOLD_GUNPOWDER_BARREL = register(
        "stronghold_gunpowder_barrel",
        GunpowderBarrelBlock(
            8,
            AbstractBlock.Settings.create().mapColor(MapColor.FIRE).instrument(NoteBlockInstrument.BASS)
                .strength(1.5f, 0.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable().solidBlock(Blocks::nonSolid)
        )
    )
    val ANCIENT_BLACK_POWDER_BARREL = register(
        "ancient_black_powder_barrel",
        GunpowderBarrelBlock(
            12,
            AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).instrument(NoteBlockInstrument.BASS)
                .strength(2f, 0.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable().solidBlock(Blocks::nonSolid)
        )
    )
    val BLUNDERBOMB_BLOCK = register(
        "blunderbomb",
        BlunderbombBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.FIRE).instrument(NoteBlockInstrument.HAT)
                .strength(1f, 0.0f).sounds(BlockSoundGroup.GLASS).solidBlock(Blocks::nonSolid)
        )
    )

    val CYPRESS_LOG = register("cypress_log", Blocks.logOf(charredPlanksColor, charredLogColor))
    val STRIPPED_CYPRESS_LOG = register("stripped_cypress_log", Blocks.logOf(charredPlanksColor, charredPlanksColor))
    val CYPRESS_WOOD = register(
        "cypress_wood",
        PillarBlock(
            AbstractBlock.Settings.create().mapColor(charredLogColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sounds(BlockSoundGroup.WOOD).lavaIgnitable()
        )
    )
    val STRIPPED_CYPRESS_WOOD = register(
        "stripped_cypress_wood",
        PillarBlock(
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sounds(BlockSoundGroup.WOOD).lavaIgnitable()
        )
    )
    val CYPRESS_PLANKS = register(
        "cypress_planks",
        Block(
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable()
        )
    )
    val CYPRESS_STAIRS = register("cypress_stairs", legacyStairsOf(CYPRESS_PLANKS))
    val CYPRESS_SLAB = register(
        "cypress_slab",
        SlabBlock(
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable()
        )
    )
    val CYPRESS_DOOR = register(
        "cypress_door",
        DoorBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).nonOpaque().pistonBehavior(PistonBehavior.DESTROY).lavaIgnitable()
        )
    )
    val CYPRESS_TRAPDOOR = register(
        "cypress_trapdoor", TrapdoorBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).nonOpaque().allowsSpawning(Blocks::nonSpawnable).lavaIgnitable()
        )
    )
    val CYPRESS_SIGN = register(
        "cypress_sign",
        VoidSignBlock(
            cypressSignId,
            DuskBlockSetType.CYPRESS_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).lavaIgnitable()
        )
    )
    val CYPRESS_WALL_SIGN = register(
        "cypress_wall_sign",
        VoidWallSignBlock(
            cypressSignId,
            DuskBlockSetType.CYPRESS_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).dropsLike(CYPRESS_SIGN).lavaIgnitable()
        )
    )
    val CYPRESS_HANGING_SIGN = register(
        "cypress_hanging_sign",
        VoidCeilingHangingSignBlock(
            cypressHangingSignId,
            DuskBlockSetType.CYPRESS_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredLogColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).lavaIgnitable()
        )
    )
    val CYPRESS_WALL_HANGING_SIGN = register(
        "cypress_wall_hanging_sign",
        VoidWallHangingSignBlock(
            cypressHangingSignId,
            DuskBlockSetType.CYPRESS_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredLogColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).lavaIgnitable()
                .dropsLike(Blocks.OAK_HANGING_SIGN)
        )
    )
    val CYPRESS_BUTTON = register("cypress_button", Blocks.buttonOf(DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE))
    val CYPRESS_FENCE = register(
        "cypress_fence",
        FenceBlock(
            AbstractBlock.Settings.create().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD)
        )
    )
    val CYPRESS_FENCE_GATE = register(
        "cypress_fence_gate",
        FenceGateBlock(
            DuskBlockSetType.CYPRESS_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid()
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f)
        )
    )
    val CYPRESS_PRESSURE_PLATE = register(
        "cypress_pressure_plate",
        PressurePlateBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(0.5f)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )

    val VOLCANIC_SAND = register(
        "volcanic_sand",
        Block(
            AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.SNARE)
                .strength(0.5f)
                .sounds(BlockSoundGroup.SAND)
        )
    )
    val SUSPICIOUS_VOLCANIC_SAND = register(
        "suspicious_volcanic_sand",
        BrushableBlock(
            VOLCANIC_SAND,
            SoundEvents.ITEM_BRUSH_BRUSHING_SAND,
            SoundEvents.ITEM_BRUSH_BRUSHING_SAND_COMPLETE,
            AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.SNARE)
                .strength(0.25f).sounds(BlockSoundGroup.SUSPICIOUS_SAND).pistonBehavior(PistonBehavior.DESTROY)
        )
    )
    val VOLCANIC_SANDSTONE = register(
        "volcanic_sandstone",
        Block(
            AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(0.8f)
        )
    )
    val VOLCANIC_SANDSTONE_STAIRS = register("volcanic_sandstone_stairs", legacyStairsOf(VOLCANIC_SANDSTONE))
    val VOLCANIC_SANDSTONE_SLAB = register(
        "volcanic_sandstone_slab",
        SlabBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(2.0f, 6.0f)
        )
    )
    val VOLCANIC_SANDSTONE_WALL =
        register("volcanic_sandstone_wall", WallBlock(AbstractBlock.Settings.variantOf(VOLCANIC_SANDSTONE).solid()))
    val CUT_VOLCANIC_SANDSTONE = register(
        "cut_volcanic_sandstone",
        Block(
            AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(0.8f)
        )
    )
    val CUT_VOLCANIC_SANDSTONE_SLAB = register(
        "cut_volcanic_sandstone_slab",
        SlabBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(2.0f, 6.0f)
        )
    )
    val CHISELED_VOLCANIC_SANDSTONE = register(
        "chiseled_volcanic_sandstone",
        Block(
            AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(0.8f)
        )
    )
    val SMOOTH_VOLCANIC_SANDSTONE = register(
        "smooth_volcanic_sandstone",
        Block(
            AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(2.0f, 6.0f)
        )
    )
    val SMOOTH_VOLCANIC_SANDSTONE_STAIRS =
        register("smooth_volcanic_sandstone_stairs", legacyStairsOf(SMOOTH_VOLCANIC_SANDSTONE))
    val SMOOTH_VOLCANIC_SANDSTONE_SLAB =
        register(
            "smooth_volcanic_sandstone_slab",
            SlabBlock(AbstractBlock.Settings.variantOf(SMOOTH_VOLCANIC_SANDSTONE))
        )


//    Blocks.CRIMSON_PLANKS.getDefaultMapColor()

    val CHARRED_LOG = register("charred_log", charredLogOf(charredPlanksColor, charredLogColor))
    val STRIPPED_CHARRED_LOG = register("stripped_charred_log", charredLogOf(charredPlanksColor, charredPlanksColor))
    val CHARRED_WOOD = register(
        "charred_wood",
        PillarBlock(
            AbstractBlock.Settings.create().mapColor(charredLogColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sounds(BlockSoundGroup.WOOD)
        )
    )
    val STRIPPED_CHARRED_WOOD = register(
        "stripped_charred_wood",
        PillarBlock(
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sounds(BlockSoundGroup.WOOD)
        )
    )
    val CHARRED_PLANKS = register(
        "charred_planks",
        Block(
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD)
        )
    )
    val CHARRED_STAIRS = register("charred_stairs", legacyStairsOf(CHARRED_PLANKS))
    val CHARRED_SLAB = register(
        "charred_slab",
        SlabBlock(
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD)
        )
    )
    val CHARRED_DOOR = register(
        "charred_door",
        DoorBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(3.0f).nonOpaque().pistonBehavior(PistonBehavior.DESTROY)
        )
    )
    val CHARRED_TRAPDOOR = register(
        "charred_trapdoor", TrapdoorBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(3.0f).nonOpaque().allowsSpawning(Blocks::nonSpawnable)
        )
    )
    val CHARRED_SIGN = register(
        "charred_sign",
        VoidSignBlock(
            charredSignId,
            DuskBlockSetType.CHARRED_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f)
        )
    )
    val CHARRED_WALL_SIGN = register(
        "charred_wall_sign",
        VoidWallSignBlock(
            charredSignId,
            DuskBlockSetType.CHARRED_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).dropsLike(CHARRED_SIGN)
        )
    )
    val CHARRED_HANGING_SIGN = register(
        "charred_hanging_sign",
        VoidCeilingHangingSignBlock(
            charredHangingSignId,
            DuskBlockSetType.CHARRED_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredLogColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f)
        )
    )
    val CHARRED_WALL_HANGING_SIGN = register(
        "charred_wall_hanging_sign",
        VoidWallHangingSignBlock(
            charredHangingSignId,
            DuskBlockSetType.CHARRED_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredLogColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).lavaIgnitable()
                .dropsLike(Blocks.OAK_HANGING_SIGN)
        )
    )
    val CHARRED_BUTTON = register("charred_button", Blocks.buttonOf(DuskBlockSetType.CHARRED_BLOCK_SET_TYPE))
    val CHARRED_FENCE = register(
        "charred_fence",
        FenceBlock(
            AbstractBlock.Settings.create().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD)
        )
    )
    val CHARRED_FENCE_GATE = register(
        "charred_fence_gate",
        FenceGateBlock(
            DuskBlockSetType.CHARRED_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid()
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f)
        )
    )
    val CHARRED_PRESSURE_PLATE = register(
        "charred_pressure_plate",
        PressurePlateBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(0.5f)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )

    fun charredLogOf(topColor: MapColor, sideColor: MapColor): Block {
        return PillarBlock(AbstractBlock.Settings.create().mapColor { state: BlockState ->
            if (state.get(
                    PillarBlock.AXIS
                ) === Direction.Axis.Y
            ) topColor else sideColor
        }.instrument(NoteBlockInstrument.BASS).strength(2.0f).sounds(BlockSoundGroup.WOOD))
    }

    fun init() {
        DuskBlockFamilies.init()
        StrippableBlockRegistry.register(CHARRED_LOG, STRIPPED_CHARRED_LOG)
        StrippableBlockRegistry.register(CHARRED_WOOD, STRIPPED_CHARRED_WOOD)
        StrippableBlockRegistry.register(CYPRESS_LOG, STRIPPED_CYPRESS_LOG)
        StrippableBlockRegistry.register(CYPRESS_WOOD, STRIPPED_CYPRESS_WOOD)
    }

    fun register(id: String, block: Block): Block = Registry.register(Registries.BLOCK, id(id), block)
}