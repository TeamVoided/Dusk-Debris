package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.block.*
import net.minecraft.block.AbstractBlock.Settings.copy
import net.minecraft.block.Blocks.*
import net.minecraft.block.Oxidizable.OxidizationLevel
import net.minecraft.block.enums.NoteBlockInstrument
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Color
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.*
import org.teamvoided.dusk_debris.block.throwable_bomb.BlunderbombBlock
import org.teamvoided.dusk_debris.block.throwable_bomb.BonecallerBlock
import org.teamvoided.dusk_debris.block.throwable_bomb.FirebombBlock
import org.teamvoided.dusk_debris.block.throwable_bomb.bonecaller.BogcallerBlock
import org.teamvoided.dusk_debris.block.throwable_bomb.bonecaller.BonechillerBlock
import org.teamvoided.dusk_debris.block.throwable_bomb.bonecaller.BonewitherBlock
import org.teamvoided.dusk_debris.block.throwable_bomb.bonecaller.ShadecallerBlock
import org.teamvoided.dusk_debris.block.throwable_bomb.nethershroom_throwable_block.BlindbombBlock
import org.teamvoided.dusk_debris.block.throwable_bomb.nethershroom_throwable_block.PocketpoisonBlock
import org.teamvoided.dusk_debris.block.throwable_bomb.nethershroom_throwable_block.SmokebombBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidCeilingHangingSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidWallHangingSignBlock
import org.teamvoided.dusk_debris.block.voided.sign.VoidWallSignBlock
import org.teamvoided.dusk_debris.data.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.util.*

@Suppress("HasPlatformType", "MemberVisibilityCanBePrivate", "unused", "DEPRECATION")
object DuskBlocks {
    val BLOCKS = mutableSetOf<Block>()
    val CUTOUT_BLOCKS = mutableSetOf<Block>()
    val TRANSLUCENT_BLOCKS = mutableSetOf<Block>()


    val BRONZE_BLOCK = register(
        "bronze_block", Block(
            AbstractBlock.Settings.create().mapColor(MapColor.BROWN).toolRequired().strength(3.0F, 6.0F)
                .sounds(BlockSoundGroup.COPPER)
        )
    )
    val CUT_BRONZE = register("cut_bronze", Block(copy(BRONZE_BLOCK)))
    val CUT_BRONZE_STAIRS =
        register("cut_bronze_stairs", StairsBlock(CUT_BRONZE.defaultState, copy(CUT_BRONZE)))
    val CUT_BRONZE_SLAB = register("cut_bronze_slab", SlabBlock(copy(CUT_BRONZE)))
    val CUT_BRONZE_WALL = register("cut_bronze_wall", WallBlock(copy(CUT_BRONZE)))
    val BRONZE_TRAPDOOR = register(
        "bronze_trapdoor", TrapdoorBlock(
            DuskBlockSetType.BRONZE, copy(BRONZE_BLOCK).nonOpaque().allowsSpawning(Blocks::nonSpawnable)
        )
    ).cutout()
    val BRONZE_GRATE = register(
        "bronze_grate", WaxedCopperGrateBlock(
            copy(BRONZE_BLOCK).sounds(BlockSoundGroup.BLOCK_COPPER_GRATE_BREAK).nonOpaque().toolRequired()
                .allowsSpawning(Blocks::nonSpawnable).solidBlock(Blocks::nonSolid).suffocates(Blocks::nonSolid)
                .blockVision(Blocks::nonSolid)
        )
    ).cutout()
    val BRONZE_BULB = register(
        "bronze_bulb", BronzeBulbBlock(
            copy(BRONZE_BLOCK).sounds(BlockSoundGroup.BLOCK_COPPER_BULB_BREAK).toolRequired()
                .solidBlock(Blocks::nonSolid).luminance(godhomeLuminanceOf())
        )
    )
    val BRONZE_SHIFT_BLOCK = register("bronze_shift_block", ShiftBlock(copy(BRONZE_BLOCK)))

    val PALE_SOUL_LANTERN = register(
        "pale_soul_lantern",
        LanternBlock(copy(SOUL_LANTERN).luminance(light(5)))
    ).cutout()
    val PALE_SOUL_VESSEL = register(
        "pale_soul_vessel",
        SoulVesselBlock(copy(PALE_SOUL_LANTERN).sounds(vesselBlockSound))
    ).cutout()

    val BLUE_NETHERSHROOM = register(
        "blue_nethershroom",
        NethershroomPlantBlock(
            4,
            DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM,
            blueNethershroomSmoke,
            StatusEffects.POISON,
            true,
            AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).strength(0.1F)
                .sounds(BlockSoundGroup.FUNGUS).noCollision()
        )
    ).cutout()
    val BLUE_NETHERSHROOM_BLOCK = register(
        "blue_nethershroom_block",
        NethershroomBlock(
            4,
            blueNethershroomSmoke,
            StatusEffects.POISON,
            true,
            AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).instrument(NoteBlockInstrument.BASS)
                .strength(0.2f).sounds(BlockSoundGroup.NETHER_WOOD)
        )
    )
    val PURPLE_NETHERSHROOM = register(
        "purple_nethershroom",
        NethershroomPlantBlock(
            16,
            DuskConfiguredFeatures.HUGE_PURPLE_NETHERSHROOM,
            purpleNethershroomSmoke,
            StatusEffects.BLINDNESS,
            false,
            AbstractBlock.Settings.create().mapColor(MapColor.PURPLE).strength(0.1F)
                .sounds(BlockSoundGroup.FUNGUS).noCollision()
        )
    ).cutout()
    val PURPLE_NETHERSHROOM_BLOCK = register(
        "purple_nethershroom_block",
        NethershroomBlock(
            16,
            purpleNethershroomSmoke,
            StatusEffects.BLINDNESS,
            false,
            AbstractBlock.Settings.create().mapColor(MapColor.PURPLE).instrument(NoteBlockInstrument.BASS)
                .strength(0.2f).sounds(BlockSoundGroup.NETHER_WOOD)
        )
    )
    val NETHERSHROOM_STEM = register(
        "nethershroom_stem",
        MushroomBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.BASS).strength(0.2f)
                .sounds(BlockSoundGroup.NETHER_WOOD)
        )
    )

    val GUNPOWDER = register(
        "gunpowder",
        GunpowderBlock(
            AbstractBlock.Settings.create().mapColor(FIRE.defaultMapColor).sounds(BlockSoundGroup.SAND)
                .lavaIgnitable().noCollision().breakInstantly().pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val GUNPOWDER_BARREL = register(
        "gunpowder_barrel",
        GunpowderBarrelBlock(
            4,
            4,
            gunpowderBarrelColor,
            AbstractBlock.Settings.create().mapColor(FIRE.defaultMapColor).instrument(NoteBlockInstrument.BASS)
                .strength(1f, 0.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable().solidBlock(Blocks::nonSolid)
        )
    )
    val STRONGHOLD_GUNPOWDER_BARREL = register(
        "stronghold_gunpowder_barrel", 16,
        GunpowderBarrelBlock(
            10,
            24,
            gunpowderBarrelColor,
            AbstractBlock.Settings.create().mapColor(FIRE.defaultMapColor).instrument(NoteBlockInstrument.BASS)
                .strength(1.5f, 0.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable().solidBlock(Blocks::nonSolid)
        )
    )
    val ANCIENT_BLACK_POWDER_BARREL = register(
        "ancient_black_powder_barrel", 1,
        GunpowderBarrelBlock(
            16,
            32,
            gunpowderBarrelBlueColor,
            AbstractBlock.Settings.create().mapColor(SOUL_FIRE.defaultMapColor)
                .instrument(NoteBlockInstrument.BASS).strength(2f, 0.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable()
                .solidBlock(Blocks::nonSolid)
        )
    )
    val BLUNDERBOMB_BLOCK = registerNoItem(
        "blunderbomb",
        BlunderbombBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.FIRE).instrument(NoteBlockInstrument.HAT)
                .strength(1f, 0.0f).sounds(BlockSoundGroup.GLASS).solidBlock(Blocks::nonSolid)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val FIREBOMB_BLOCK = registerNoItem(
        "firebomb",
        FirebombBlock(
            AbstractBlock.Settings.create().mapColor(MapColor.FIRE).instrument(NoteBlockInstrument.HAT)
                .strength(1f, 0.0f).sounds(BlockSoundGroup.GLASS).solidBlock(Blocks::nonSolid)
                .pistonBehavior(PistonBehavior.DESTROY).luminance { _: BlockState -> 8 }
        )
    ).cutout()
    val BONECALLER_BLOCK = registerNoItem("bonecaller", BonecallerBlock(bonecallerBlockSettings)).cutout()
    val BONECHILLER_BLOCK = registerNoItem("bonechiller", BonechillerBlock(bonecallerBlockSettings)).cutout()
    val BOGCALLER_BLOCK = registerNoItem("bogcaller", BogcallerBlock(bonecallerBlockSettings)).cutout()
    val BONEWITHER_BLOCK = registerNoItem("bonewither", BonewitherBlock(bonecallerBlockSettings)).cutout()
    val SHADECALLER_BLOCK = registerNoItem("shadecaller", ShadecallerBlock(bonecallerBlockSettings)).cutout()
    val SMOKEBOMB_BLOCK = registerNoItem(
        "smokebomb",
        SmokebombBlock(
            AbstractBlock.Settings.create().mapColor(WHITE_STAINED_GLASS.defaultMapColor).sounds(BlockSoundGroup.GLASS)
                .instrument(NoteBlockInstrument.HAT).strength(1f, 0.0f).solidBlock(Blocks::nonSolid)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val POCKETPOISON_BLOCK = registerNoItem(
        "pocketpoison",
        PocketpoisonBlock(
            AbstractBlock.Settings.create().mapColor(BLUE_NETHERSHROOM.defaultMapColor).sounds(BlockSoundGroup.GLASS)
                .instrument(NoteBlockInstrument.HAT).strength(1f, 0.0f).solidBlock(Blocks::nonSolid)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val BLINDBOMB_BLOCK = registerNoItem(
        "blindbomb",
        BlindbombBlock(
            AbstractBlock.Settings.create().mapColor(PURPLE_NETHERSHROOM.defaultMapColor).sounds(BlockSoundGroup.GLASS)
                .instrument(NoteBlockInstrument.HAT).strength(1f, 0.0f).solidBlock(Blocks::nonSolid)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val RED_RIBBON = register("red_ribbon", registerRibbon(RED_WOOL.defaultMapColor))
    val ORANGE_RIBBON = register("orange_ribbon", registerRibbon(ORANGE_WOOL.defaultMapColor))
    val YELLOW_RIBBON = register("yellow_ribbon", registerRibbon(YELLOW_WOOL.defaultMapColor))
    val LIME_RIBBON = register("lime_ribbon", registerRibbon(LIME_WOOL.defaultMapColor))
    val GREEN_RIBBON = register("green_ribbon", registerRibbon(GREEN_WOOL.defaultMapColor))
    val CYAN_RIBBON = register("cyan_ribbon", registerRibbon(CYAN_WOOL.defaultMapColor))
    val BLUE_RIBBON = register("blue_ribbon", registerRibbon(BLUE_WOOL.defaultMapColor))
    val LIGHT_BLUE_RIBBON = register("light_blue_ribbon", registerRibbon(LIGHT_BLUE_WOOL.defaultMapColor))
    val PURPLE_RIBBON = register("purple_ribbon", registerRibbon(PURPLE_WOOL.defaultMapColor))
    val MAGENTA_RIBBON = register("magenta_ribbon", registerRibbon(MAGENTA_WOOL.defaultMapColor))
    val PINK_RIBBON = register("pink_ribbon", registerRibbon(PINK_WOOL.defaultMapColor))
    val BROWN_RIBBON = register("brown_ribbon", registerRibbon(BROWN_WOOL.defaultMapColor))
    val WHITE_RIBBON = register("white_ribbon", registerRibbon(WHITE_WOOL.defaultMapColor))
    val LIGHT_GRAY_RIBBON = register("light_gray_ribbon", registerRibbon(LIGHT_GRAY_WOOL.defaultMapColor))
    val GRAY_RIBBON = register("gray_ribbon", registerRibbon(GRAY_WOOL.defaultMapColor))
    val BLACK_RIBBON = register("black_ribbon", registerRibbon(BLACK_WOOL.defaultMapColor))

    val TREACHEROUS_GOLD_BLOCK = register(
        "treacherous_gold_block", Block(
            AbstractBlock.Settings.variantOf(GOLD_BLOCK).strength(3.5f, 6.0f)
        )
    )
    val TARNISHED_GOLD_BLOCK = register(
        "tarnished_gold_block", Block(
            AbstractBlock.Settings.variantOf(TREACHEROUS_GOLD_BLOCK).mapColor(MapColor.YELLOW_TERRACOTTA)
                .strength(3.5f, 6.0f)
        )
    )
    val LOST_SILVER_BLOCK = register(
        "lost_silver_block", Block(
            AbstractBlock.Settings.create().mapColor(MapColor.METAL).toolRequired().strength(3.5f, 6.0f)
                .sounds(BlockSoundGroup.METAL)
        )
    )
    val SUNKEN_BRONZE_BLOCK = register(
        "sunken_bronze_block", Block(
            AbstractBlock.Settings.create().mapColor(MapColor.BLACK_TERRACOTTA).toolRequired().strength(3.5f, 6.0f)
                .sounds(BlockSoundGroup.METAL)
        )
    )
    val TREACHEROUS_GOLD_COIN_STACK = registerNoItem(
        "treacherous_gold_coin_stack",
        CoinStackBlock(coin_stack_settings.mapColor(TREACHEROUS_GOLD_BLOCK.defaultMapColor))
    ).cutout()
    val TREACHEROUS_GOLD_COIN_PILE = registerNoItem(
        "treacherous_gold_coin_pile", CoinPileBlock(coin_pile_settings.mapColor(TREACHEROUS_GOLD_BLOCK.defaultMapColor))
    )
    val TARNISHED_GOLD_COIN_STACK = registerNoItem(
        "tarnished_gold_coin_stack", CoinStackBlock(coin_stack_settings.mapColor(TARNISHED_GOLD_BLOCK.defaultMapColor))
    ).cutout()
    val TARNISHED_GOLD_COIN_PILE = registerNoItem(
        "tarnished_gold_coin_pile", CoinPileBlock(coin_pile_settings.mapColor(TARNISHED_GOLD_BLOCK.defaultMapColor))
    )
    val LOST_SILVER_COIN_STACK = registerNoItem(
        "lost_silver_coin_stack", CoinStackBlock(coin_stack_settings.mapColor(LOST_SILVER_BLOCK.defaultMapColor))
    ).cutout()
    val LOST_SILVER_COIN_PILE = registerNoItem(
        "lost_silver_coin_pile", CoinPileBlock(coin_pile_settings.mapColor(LOST_SILVER_BLOCK.defaultMapColor))
    )
    val SUNKEN_BRONZE_COIN_STACK = registerNoItem(
        "sunken_bronze_coin_stack", CoinStackBlock(coin_stack_settings.mapColor(SUNKEN_BRONZE_BLOCK.defaultMapColor))
    ).cutout()
    val SUNKEN_BRONZE_COIN_PILE = registerNoItem(
        "sunken_bronze_coin_pile", CoinPileBlock(coin_pile_settings.mapColor(SUNKEN_BRONZE_BLOCK.defaultMapColor))
    )
    val GOLDEN_VESSEL = register(
        "golden_vessel", 16,
        MysteriousVesselBlock(
            AbstractBlock.Settings.create().mapColor(TREACHEROUS_GOLD_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val DROWNED_VESSEL = register(
        "drowned_vessel", 16,
        MysteriousVesselBlock(
            AbstractBlock.Settings.create().mapColor(TARNISHED_GOLD_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val PURE_VESSEL = register(
        "pure_vessel", 16,
        MysteriousVesselBlock(
            AbstractBlock.Settings.create().mapColor(LOST_SILVER_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val DARKENED_VESSEL = register(
        "darkened_vessel", 16,
        MysteriousVesselBlock(
            AbstractBlock.Settings.create().mapColor(SUNKEN_BRONZE_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val GILDED_CHALICE = register(
        "gilded_chalice", 16, GildedChaliceBlock(
            AbstractBlock.Settings.create().mapColor(TREACHEROUS_GOLD_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )
    val TARNISHED_CHALICE = register(
        "tarnished_chalice", 16, GildedChaliceBlock(
            AbstractBlock.Settings.create().mapColor(TARNISHED_GOLD_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )
    val SILVERED_CHALICE = register(
        "silvered_chalice", 16, GildedChaliceBlock(
            AbstractBlock.Settings.create().mapColor(LOST_SILVER_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )
    val BRONZED_CHALICE = register(
        "bronzed_chalice", 16, GildedChaliceBlock(
            AbstractBlock.Settings.create().mapColor(SUNKEN_BRONZE_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )
    val LAPIS_RELIC = register(
        "lapis_relic", 16, PerculiarRelicBlock(
            AbstractBlock.Settings.create().mapColor(LAPIS_BLOCK.defaultMapColor).pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val GOLDEN_RUBY_CROWN = register(
        "golden_ruby_crown", 16, RoyalCrownBlock(
            AbstractBlock.Settings.create().mapColor(REDSTONE_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val GOLDEN_SAPPHIRE_CROWN = register(
        "golden_sapphire_crown", 16, RoyalCrownBlock(
            AbstractBlock.Settings.create().mapColor(LAPIS_BLOCK.defaultMapColor).pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val GOLDEN_QUARTZ_CROWN = register(
        "golden_quartz_crown", 16, RoyalCrownBlock(
            AbstractBlock.Settings.create().mapColor(QUARTZ_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()

    val LEGENDARY_CRYSTAL_CROWN = register(
        "legendary_crystal_crown", 16, RoyalCrownBlock(
            AbstractBlock.Settings.create().mapColor(DIAMOND_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val FORGOTTEN_CHEST = register(
        "forgotten_chest",
        TreasureChestBlock(
            AbstractBlock.Settings.create().pistonBehavior(PistonBehavior.IGNORE)
        )
    )
//    val GLOOM_SKULL = registerSkull(
//        "gloomed_skull",
//        DuskSkullType.GLOOM,
//        NoteBlockInstrument.SKELETON
//    )
//    val GLOOM_WALL_SKULL = registerWallSkull(
//        "skeleton_wall_skull",
//        DuskSkullType.GLOOM,
//        GLOOM_SKULL
//    )
//    val STRAY_SKULL = registerSkull(
//        "stray_skull",
//        DuskSkullType.STRAY,
//        NoteBlockInstrument.SKELETON
//    )
//    val STRAY_WALL_SKULL = registerWallSkull(
//        "stray_wall_skull",
//        DuskSkullType.STRAY,
//        STRAY_SKULL
//    )
//    val BOGGED_SKULL = registerSkull(
//        "bogged_skull",
//        DuskSkullType.BOGGED,
//        NoteBlockInstrument.SKELETON
//    )
//    val BOGGED_WALL_SKULL = registerWallSkull(
//        "bogged_wall_skull",
//        DuskSkullType.BOGGED,
//        BOGGED_SKULL
//    )

    val CRYSTAL_BLOCK = register(
        "crystal_block",
        Block(AbstractBlock.Settings.copy(AMETHYST_BLOCK))
    )
    val CRYSTAL_PILLAR_BLOCK = register(
        "crystal_pillar_block",
        PillarBlock(AbstractBlock.Settings.copy(CRYSTAL_BLOCK))
    )

    val WAXED_OXIDIZED_COPPER_FAN = register(
        "waxed_oxidized_copper_fan", FanBlock(
            4,
            AbstractBlock.Settings.create().mapColor(OXIDIZED_COPPER.defaultMapColor).strength(3.0F, 6.0F)
                .sounds(BlockSoundGroup.BLOCK_COPPER_BULB_BREAK).toolRequired().solidBlock(Blocks::nonSolid)
        )
    )
    val WAXED_WEATHERED_COPPER_FAN = register(
        "waxed_weathered_copper_fan",
        FanBlock(
            8,
            AbstractBlock.Settings.copy(WAXED_OXIDIZED_COPPER_FAN).mapColor(WEATHERED_COPPER.defaultMapColor)
        )
    )
    val WAXED_EXPOSED_COPPER_FAN = register(
        "waxed_exposed_copper_fan",
        FanBlock(
            12,
            AbstractBlock.Settings.copy(WAXED_WEATHERED_COPPER_FAN).mapColor(EXPOSED_COPPER.defaultMapColor)
        )
    )
    val WAXED_COPPER_FAN = register(
        "waxed_copper_fan",
        FanBlock(15, AbstractBlock.Settings.copy(WAXED_EXPOSED_COPPER_FAN).mapColor(COPPER_BLOCK.defaultMapColor))
    )

    val OXIDIZED_COPPER_FAN = register(
        "oxidized_copper_fan",
        OxidizableFanBlock(
            OxidizationLevel.OXIDIZED, 4,
            AbstractBlock.Settings.copy(WAXED_OXIDIZED_COPPER_FAN).ticksRandomly()
        )
    )
    val WEATHERED_COPPER_FAN = register(
        "weathered_copper_fan",
        OxidizableFanBlock(
            OxidizationLevel.EXPOSED, 8,
            AbstractBlock.Settings.copy(WAXED_WEATHERED_COPPER_FAN).ticksRandomly()
        )
    )
    val EXPOSED_COPPER_FAN = register(
        "exposed_copper_fan",
        OxidizableFanBlock(
            OxidizationLevel.WEATHERED, 12,
            copy(WAXED_EXPOSED_COPPER_FAN).ticksRandomly()
        )
    )
    val COPPER_FAN = register(
        "copper_fan",
        OxidizableFanBlock(
            OxidizationLevel.UNAFFECTED, 15,
            copy(WAXED_COPPER_FAN).ticksRandomly()
        )
    )

    val PAPER_BLOCK = register(
        "paper_block",
        PaperBlock(
            AbstractBlock.Settings.create().mapColor(WHITE_WOOL.defaultMapColor).strength(0.25F)
        )
    )

    val BOG_MUD = registerNoItem(
        "bog_mud", BogMudBlock(
            AbstractBlock.Settings.variantOf(MUD).dynamicBounds()
                .allowsSpawning(Blocks::spawnable)
                .solidBlock(Blocks::nonSolid)
                .blockVision(Blocks::solid)
                .suffocates(Blocks::nonSolid)
        )
    )

    val CYPRESS_LEAVES = register("cypress_leaves", leavesOf(BlockSoundGroup.GRASS)).cutout()
    val CYPRESS_LOG = register("cypress_log", logOf(charredPlanksColor, charredLogColor))
    val STRIPPED_CYPRESS_LOG = register("stripped_cypress_log", logOf(charredPlanksColor, charredPlanksColor))
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
    val CYPRESS_DOOR = registerNoItem(
        "cypress_door",
        DoorBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).nonOpaque().pistonBehavior(PistonBehavior.DESTROY).lavaIgnitable()
        )
    ).cutout()
    val CYPRESS_TRAPDOOR = register(
        "cypress_trapdoor", TrapdoorBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).nonOpaque().allowsSpawning(Blocks::nonSpawnable).lavaIgnitable()
        )
    ).cutout()
    val CYPRESS_SIGN = registerNoItem(
        "cypress_sign",
        VoidSignBlock(
            cypressSignId,
            DuskBlockSetType.CYPRESS_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).lavaIgnitable()
        )
    )
    val CYPRESS_WALL_SIGN = registerNoItem(
        "cypress_wall_sign",
        VoidWallSignBlock(
            cypressSignId,
            DuskBlockSetType.CYPRESS_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).dropsLike(CYPRESS_SIGN).lavaIgnitable()
        )
    )
    val CYPRESS_HANGING_SIGN = registerNoItem(
        "cypress_hanging_sign",
        VoidCeilingHangingSignBlock(
            cypressHangingSignId,
            DuskBlockSetType.CYPRESS_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredLogColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).lavaIgnitable()
        )
    )
    val CYPRESS_WALL_HANGING_SIGN = registerNoItem(
        "cypress_wall_hanging_sign",
        VoidWallHangingSignBlock(
            cypressHangingSignId,
            DuskBlockSetType.CYPRESS_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredLogColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).lavaIgnitable()
                .dropsLike(OAK_HANGING_SIGN)
        )
    )
    val CYPRESS_BUTTON = register("cypress_button", buttonOf(DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE))
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
        GravelBlock(
            Color(1644825),
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
    val ROARING_GEYSER = register(
        "roaring_geyser",
        RoaringGeyserBlock(AbstractBlock.Settings.create().ticksRandomly())
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
    val CHARRED_DOOR = registerNoItem(
        "charred_door",
        DoorBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(3.0f).nonOpaque().pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val CHARRED_TRAPDOOR = register(
        "charred_trapdoor", TrapdoorBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(3.0f).nonOpaque().allowsSpawning(Blocks::nonSpawnable)
        )
    ).cutout()
    val CHARRED_SIGN = registerNoItem(
        "charred_sign",
        VoidSignBlock(
            charredSignId,
            DuskBlockSetType.CHARRED_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f)
        )
    )
    val CHARRED_WALL_SIGN = registerNoItem(
        "charred_wall_sign",
        VoidWallSignBlock(
            charredSignId,
            DuskBlockSetType.CHARRED_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).dropsLike(CHARRED_SIGN)
        )
    )
    val CHARRED_HANGING_SIGN = registerNoItem(
        "charred_hanging_sign",
        VoidCeilingHangingSignBlock(
            charredHangingSignId,
            DuskBlockSetType.CHARRED_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredLogColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f)
        )
    )
    val CHARRED_WALL_HANGING_SIGN = registerNoItem(
        "charred_wall_hanging_sign",
        VoidWallHangingSignBlock(
            charredHangingSignId,
            DuskBlockSetType.CHARRED_SIGN_TYPE,
            AbstractBlock.Settings.create().mapColor(charredLogColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).lavaIgnitable()
                .dropsLike(OAK_HANGING_SIGN)
        )
    )
    val CHARRED_BUTTON = register("charred_button", buttonOf(DuskBlockSetType.CHARRED_BLOCK_SET_TYPE))
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

    fun init() {
        DuskBlockSetType.init()
        StrippableBlockRegistry.register(CHARRED_LOG, STRIPPED_CHARRED_LOG)
        StrippableBlockRegistry.register(CHARRED_WOOD, STRIPPED_CHARRED_WOOD)
        StrippableBlockRegistry.register(CYPRESS_LOG, STRIPPED_CYPRESS_LOG)
        StrippableBlockRegistry.register(CYPRESS_WOOD, STRIPPED_CYPRESS_WOOD)

        oxidizeCopperSet(DuskBlockLists.copperFans)
    }

    fun register(id: String, block: Block): Block {
        val regBlock = registerNoItem(id, block)
        DuskItems.register(id, BlockItem(regBlock, Item.Settings()))
        return regBlock
    }

    fun register(id: String, maxCount: Int, block: Block): Block {
        val regBlock = registerNoItem(id, block)
        DuskItems.register(id, BlockItem(regBlock, Item.Settings().maxCount(maxCount)))
        return regBlock
    }

    fun registerNoItem(id: String, block: Block): Block {
        val regBlock = Registry.register(Registries.BLOCK, id(id), block)
        BLOCKS.add(regBlock)
        return regBlock
    }
}