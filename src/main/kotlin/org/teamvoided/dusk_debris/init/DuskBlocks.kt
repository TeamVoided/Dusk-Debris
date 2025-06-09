package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.block.*
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.AbstractBlock.Settings.copy
import net.minecraft.block.Blocks.*
import net.minecraft.block.Oxidizable.OxidizationLevel
import net.minecraft.block.enums.NoteBlockInstrument
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.block.sapling.SaplingBlock
import net.minecraft.block.sapling.TreeGrower
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.TallBlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Color
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.*
import org.teamvoided.dusk_debris.block.big.BigLanternWithSpiralBlock
import org.teamvoided.dusk_debris.block.sot.GildedChaliceBlock
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
import org.teamvoided.dusk_debris.data.worldgen.DuskConfiguredFeatures
import org.teamvoided.dusk_debris.init.misc.DuskBlockSettings
import org.teamvoided.dusk_debris.item.StrongScaffoldingItem
import org.teamvoided.dusk_debris.util.*

@Suppress("MemberVisibilityCanBePrivate", "unused", "DEPRECATION")
object DuskBlocks {
    val BLOCKS = mutableSetOf<Block>()
    val CUTOUT_BLOCKS = mutableSetOf<Block>()
    val TRANSLUCENT_BLOCKS = mutableSetOf<Block>()

    val TEST_BLOCK = register("test_block", EntityTestParticleBlock(copy(STONE)))

    val STRONG_SCAFFOLDING =
        registerStrongScaffolding("strong_scaffolding", StrongScaffoldingBlock(copy(SCAFFOLDING))).cutout()
    //val STONE_CHEST = register("stone_chest", DuskDoubleChestBlock(copy(CHEST)) { DuskBlockEntities.STONE_CHEST })

    val ACID = registerNoItem("acid", FluidBlock(DuskFluids.ACID, copy(WATER).mapColor(MapColor.LIME)))
    val FOG_BUBBLE = registerNoItem(
        "fog_bubble", BubbleBlock(
            Settings.create().mapColor(MapColor.PURPLE_TERRACOTTA).strength(0.25f)
                .sounds(BlockSoundGroup.HONEY).solidBlock(Blocks::nonSolid).ticksRandomly()
        )
    ).translucent()
    val PURPLE_BUBBLE_BLOSSOM = register(
        "purple_bubble_blossom", BubbleBlossomBlock(copy(SPORE_BLOSSOM).ticksRandomly())
    ).cutout()

    val MYTHROCK = register("mythrock", Block(copy(STONE)))
    val MYTHROCK_ARTERY = register("mythrock_artery", MysticalStreamBlock(copy(MYTHROCK)))
    val MYTHROCK_HEART = register("mythrock_heart", MysticalPulseBlock(copy(MYTHROCK).ticksRandomly()))

    val BRONZE_BLOCK = register(
        "bronze_block", Block(
            Settings.create().mapColor(MapColor.BROWN).toolRequired().strength(3.0F, 6.0F)
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
            Settings.create().mapColor(MapColor.LIGHT_BLUE).strength(0.1F)
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
            Settings.create().mapColor(MapColor.LIGHT_BLUE).instrument(NoteBlockInstrument.BASS)
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
            Settings.create().mapColor(MapColor.PURPLE).strength(0.1F)
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
            Settings.create().mapColor(MapColor.PURPLE).instrument(NoteBlockInstrument.BASS)
                .strength(0.2f).sounds(BlockSoundGroup.NETHER_WOOD)
        )
    )
    val NETHERSHROOM_STEM = register(
        "nethershroom_stem",
        MushroomBlock(
            Settings.create().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.BASS).strength(0.2f)
                .sounds(BlockSoundGroup.NETHER_WOOD)
        )
    )

    val GUNPOWDER = register(
        "gunpowder",
        GunpowderBlock(
            Settings.create().mapColor(FIRE.defaultMapColor).sounds(BlockSoundGroup.SAND)
                .lavaIgnitable().noCollision().breakInstantly().pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val GUNPOWDER_BARREL = register(
        "gunpowder_barrel",
        GunpowderBarrelBlock(
            4,
            4,
            gunpowderBarrelColor,
            Settings.create().mapColor(FIRE.defaultMapColor).instrument(NoteBlockInstrument.BASS)
                .strength(1f, 0.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable().solidBlock(Blocks::nonSolid)
        )
    )
    val STRONGHOLD_GUNPOWDER_BARREL = register(
        "stronghold_gunpowder_barrel", 16,
        GunpowderBarrelBlock(
            10,
            24,
            gunpowderBarrelColor,
            Settings.create().mapColor(FIRE.defaultMapColor).instrument(NoteBlockInstrument.BASS)
                .strength(1.5f, 0.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable().solidBlock(Blocks::nonSolid)
        )
    )
    val ANCIENT_BLACK_POWDER_BARREL = register(
        "ancient_black_powder_barrel", 1,
        GunpowderBarrelBlock(
            16,
            32,
            gunpowderBarrelBlueColor,
            Settings.create().mapColor(SOUL_FIRE.defaultMapColor)
                .instrument(NoteBlockInstrument.BASS).strength(2f, 0.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable()
                .solidBlock(Blocks::nonSolid)
        )
    )
    val BLUNDERBOMB_BLOCK = registerNoItem(
        "blunderbomb",
        BlunderbombBlock(
            Settings.create().mapColor(MapColor.FIRE).instrument(NoteBlockInstrument.HAT)
                .strength(1f, 0.0f).sounds(BlockSoundGroup.GLASS).solidBlock(Blocks::nonSolid)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val FIREBOMB_BLOCK = registerNoItem(
        "firebomb",
        FirebombBlock(
            Settings.create().mapColor(MapColor.FIRE).instrument(NoteBlockInstrument.HAT)
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
            Settings.create().mapColor(WHITE_STAINED_GLASS.defaultMapColor).sounds(BlockSoundGroup.GLASS)
                .instrument(NoteBlockInstrument.HAT).strength(1f, 0.0f).solidBlock(Blocks::nonSolid)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val POCKETPOISON_BLOCK = registerNoItem(
        "pocketpoison",
        PocketpoisonBlock(
            Settings.create().mapColor(BLUE_NETHERSHROOM.defaultMapColor).sounds(BlockSoundGroup.GLASS)
                .instrument(NoteBlockInstrument.HAT).strength(1f, 0.0f).solidBlock(Blocks::nonSolid)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val BLINDBOMB_BLOCK = registerNoItem(
        "blindbomb",
        BlindbombBlock(
            Settings.create().mapColor(PURPLE_NETHERSHROOM.defaultMapColor).sounds(BlockSoundGroup.GLASS)
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

    val RED_CARPET_STAIRS = register("red_carpet_stairs", CarpetStairBlock(copy(RED_CARPET)))

    val TREACHEROUS_GOLD_BLOCK = register(
        "treacherous_gold_block", Block(
            Settings.variantOf(GOLD_BLOCK).strength(3.5f, 6.0f)
        )
    )
    val TARNISHED_GOLD_BLOCK = register(
        "tarnished_gold_block", Block(
            Settings.variantOf(TREACHEROUS_GOLD_BLOCK).mapColor(MapColor.YELLOW_TERRACOTTA)
                .strength(3.5f, 6.0f)
        )
    )
    val LOST_SILVER_BLOCK = register(
        "lost_silver_block", Block(
            Settings.create().mapColor(MapColor.METAL).toolRequired().strength(3.5f, 6.0f)
                .sounds(BlockSoundGroup.METAL)
        )
    )
    val SUNKEN_BRONZE_BLOCK = register(
        "sunken_bronze_block", Block(
            Settings.create().mapColor(MapColor.BLACK_TERRACOTTA).toolRequired().strength(3.5f, 6.0f)
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
            Settings.create().mapColor(TREACHEROUS_GOLD_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val DROWNED_VESSEL = register(
        "drowned_vessel", 16,
        MysteriousVesselBlock(
            Settings.create().mapColor(TARNISHED_GOLD_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val PURE_VESSEL = register(
        "pure_vessel", 16,
        MysteriousVesselBlock(
            Settings.create().mapColor(LOST_SILVER_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val DARKENED_VESSEL = register(
        "darkened_vessel", 16,
        MysteriousVesselBlock(
            Settings.create().mapColor(SUNKEN_BRONZE_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val GILDED_CHALICE = register(
        "gilded_chalice", 16, GildedChaliceBlock(
            Settings.create().mapColor(TREACHEROUS_GOLD_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val TARNISHED_CHALICE = register(
        "tarnished_chalice", 16, GildedChaliceBlock(
            Settings.create().mapColor(TARNISHED_GOLD_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val SILVERED_CHALICE = register(
        "silvered_chalice", 16, GildedChaliceBlock(
            Settings.create().mapColor(LOST_SILVER_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val BRONZED_CHALICE = register(
        "bronzed_chalice", 16, GildedChaliceBlock(
            Settings.create().mapColor(SUNKEN_BRONZE_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val LAPIS_RELIC = register(
        "lapis_relic", 16, PerculiarRelicBlock(
            Settings.create().mapColor(LAPIS_BLOCK.defaultMapColor).pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val GOLDEN_RUBY_CROWN = register(
        "golden_ruby_crown", 16, RoyalCrownBlock(
            Settings.create().mapColor(REDSTONE_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val GOLDEN_SAPPHIRE_CROWN = register(
        "golden_sapphire_crown", 16, RoyalCrownBlock(
            Settings.create().mapColor(LAPIS_BLOCK.defaultMapColor).pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val GOLDEN_QUARTZ_CROWN = register(
        "golden_quartz_crown", 16, RoyalCrownBlock(
            Settings.create().mapColor(QUARTZ_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()

    val LEGENDARY_CRYSTAL_CROWN = register(
        "legendary_crystal_crown", 16, RoyalCrownBlock(
            Settings.create().mapColor(DIAMOND_BLOCK.defaultMapColor)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val FORGOTTEN_CHEST = register(
        "forgotten_chest",
        TreasureChestBlock(
            Settings.create().pistonBehavior(PistonBehavior.IGNORE)
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
        Block(copy(AMETHYST_BLOCK))
    )
    val CRYSTAL_PILLAR_BLOCK = register(
        "crystal_pillar_block",
        PillarBlock(copy(CRYSTAL_BLOCK))
    )

    val WAXED_OXIDIZED_COPPER_FAN = register(
        "waxed_oxidized_copper_fan", FanBlock(
            4,
            Settings.create().mapColor(OXIDIZED_COPPER.defaultMapColor).strength(3.0F, 6.0F)
                .sounds(BlockSoundGroup.BLOCK_COPPER_BULB_BREAK).toolRequired().solidBlock(Blocks::nonSolid)
        )
    )
    val WAXED_WEATHERED_COPPER_FAN = register(
        "waxed_weathered_copper_fan",
        FanBlock(
            8,
            copy(WAXED_OXIDIZED_COPPER_FAN).mapColor(WEATHERED_COPPER.defaultMapColor)
        )
    )
    val WAXED_EXPOSED_COPPER_FAN = register(
        "waxed_exposed_copper_fan",
        FanBlock(
            12,
            copy(WAXED_WEATHERED_COPPER_FAN).mapColor(EXPOSED_COPPER.defaultMapColor)
        )
    )
    val WAXED_COPPER_FAN = register(
        "waxed_copper_fan",
        FanBlock(15, copy(WAXED_EXPOSED_COPPER_FAN).mapColor(COPPER_BLOCK.defaultMapColor))
    )

    val OXIDIZED_COPPER_FAN = register(
        "oxidized_copper_fan",
        OxidizableFanBlock(
            OxidizationLevel.OXIDIZED, 4,
            copy(WAXED_OXIDIZED_COPPER_FAN).ticksRandomly()
        )
    )
    val WEATHERED_COPPER_FAN = register(
        "weathered_copper_fan",
        OxidizableFanBlock(
            OxidizationLevel.EXPOSED, 8,
            copy(WAXED_WEATHERED_COPPER_FAN).ticksRandomly()
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
            Settings.create().mapColor(WHITE_WOOL.defaultMapColor).strength(0.25F)
        )
    )

    val BOG_MUD = registerNoItem(
        "bog_mud", BogMudBlock(
            Settings.variantOf(MUD).dynamicBounds()
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
            Settings.create().mapColor(charredLogColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sounds(BlockSoundGroup.WOOD).lavaIgnitable()
        )
    )
    val STRIPPED_CYPRESS_WOOD = register("stripped_cypress_wood", PillarBlock(copy(CYPRESS_WOOD)))
    val CYPRESS_PLANKS = register(
        "cypress_planks", Block(
            Settings.create().mapColor(MapColor.RED).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable()
        )
    )
    val CYPRESS_STAIRS = register("cypress_stairs", legacyStairsOf(CYPRESS_PLANKS))
    val CYPRESS_SLAB = register("cypress_slab", SlabBlock(copy(CYPRESS_PLANKS)))
    val CYPRESS_DOOR = registerDoor(
        "cypress_door",
        DoorBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            Settings.create().mapColor(CYPRESS_PLANKS.defaultMapColor)
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).nonOpaque().pistonBehavior(PistonBehavior.DESTROY).lavaIgnitable()
        )
    ).cutout()
    val CYPRESS_TRAPDOOR = register(
        "cypress_trapdoor", TrapdoorBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).nonOpaque().allowsSpawning(Blocks::nonSpawnable).lavaIgnitable()
        )
    ).cutout()
    val CYPRESS_SIGN = registerNoItem(
        "cypress_sign",
        VoidSignBlock(
            cypressSignId,
            DuskBlockSetType.CYPRESS_WOOD_TYPE,
            Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).lavaIgnitable()
        )
    )
    val CYPRESS_WALL_SIGN = registerNoItem(
        "cypress_wall_sign",
        VoidWallSignBlock(
            cypressSignId,
            DuskBlockSetType.CYPRESS_WOOD_TYPE,
            Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).dropsLike(CYPRESS_SIGN).lavaIgnitable()
        )
    )
    val CYPRESS_HANGING_SIGN = registerNoItem(
        "cypress_hanging_sign",
        VoidCeilingHangingSignBlock(
            cypressHangingSignId,
            DuskBlockSetType.CYPRESS_WOOD_TYPE,
            Settings.create().mapColor(charredLogColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).lavaIgnitable()
        )
    )
    val CYPRESS_WALL_HANGING_SIGN = registerNoItem(
        "cypress_wall_hanging_sign",
        VoidWallHangingSignBlock(
            cypressHangingSignId,
            DuskBlockSetType.CYPRESS_WOOD_TYPE,
            Settings.create().mapColor(charredLogColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).lavaIgnitable()
                .dropsLike(OAK_HANGING_SIGN)
        )
    )
    val CYPRESS_BUTTON = register("cypress_button", buttonOf(DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE))
    val CYPRESS_FENCE = register(
        "cypress_fence",
        FenceBlock(
            Settings.create().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD)
        )
    )
    val CYPRESS_FENCE_GATE = register(
        "cypress_fence_gate",
        FenceGateBlock(
            DuskBlockSetType.CYPRESS_WOOD_TYPE,
            Settings.create().mapColor(charredPlanksColor).solid()
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f)
        )
    )
    val CYPRESS_PRESSURE_PLATE = register(
        "cypress_pressure_plate",
        PressurePlateBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            Settings.create().mapColor(charredPlanksColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(0.5f)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )

    val SEQUOIA_LEAVES = register("sequoia_leaves", LongLeavesBlock(copy(SPRUCE_LEAVES))).cutout()
    val SEQUOIA_LOG = register("sequoia_log", logOf(charredPlanksColor, charredLogColor))
    val STRIPPED_SEQUOIA_LOG = register("stripped_sequoia_log", logOf(charredPlanksColor, charredPlanksColor))
    val SEQUOIA_WOOD = register(
        "sequoia_wood", PillarBlock(
            Settings.create().mapColor(charredLogColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sounds(BlockSoundGroup.WOOD).lavaIgnitable()
        )
    )
    val STRIPPED_SEQUOIA_WOOD = register("stripped_sequoia_wood", PillarBlock(copy(SEQUOIA_WOOD)))
    val SEQUOIA_PLANKS = register(
        "sequoia_planks", Block(
            Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD).lavaIgnitable()
        )
    )
    val SEQUOIA_STAIRS = register("sequoia_stairs", legacyStairsOf(SEQUOIA_PLANKS))
    val SEQUOIA_SLAB = register("sequoia_slab", SlabBlock(copy(SEQUOIA_PLANKS)))
    val SEQUOIA_DOOR = registerDoor(
        "sequoia_door", DoorBlock(
            DuskBlockSetType.SEQUOIA_BLOCK_SET_TYPE,
            Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).nonOpaque().pistonBehavior(PistonBehavior.DESTROY).lavaIgnitable()
        )
    ).cutout()
    val SEQUOIA_TRAPDOOR = register(
        "sequoia_trapdoor", TrapdoorBlock(
            DuskBlockSetType.SEQUOIA_BLOCK_SET_TYPE,
            Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).nonOpaque().allowsSpawning(Blocks::nonSpawnable).lavaIgnitable()
        )
    ).cutout()
    val SEQUOIA_SIGN = registerNoItem(
        "sequoia_sign", VoidSignBlock(
            sequoiaSignId,
            DuskBlockSetType.SEQUOIA_WOOD_TYPE,
            Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).lavaIgnitable()
        )
    )
    val SEQUOIA_WALL_SIGN = registerNoItem(
        "sequoia_wall_sign", VoidWallSignBlock(
            sequoiaSignId,
            DuskBlockSetType.SEQUOIA_WOOD_TYPE,
            copy(SEQUOIA_SIGN).dropsLike(SEQUOIA_SIGN)
        )
    )
    val SEQUOIA_HANGING_SIGN = registerNoItem(
        "sequoia_hanging_sign", VoidCeilingHangingSignBlock(
            sequoiaHangingSignId,
            DuskBlockSetType.SEQUOIA_WOOD_TYPE,
            copy(SEQUOIA_SIGN)
        )
    )
    val SEQUOIA_WALL_HANGING_SIGN = registerNoItem(
        "sequoia_wall_hanging_sign", VoidWallHangingSignBlock(
            sequoiaHangingSignId,
            DuskBlockSetType.SEQUOIA_WOOD_TYPE,
            copy(SEQUOIA_HANGING_SIGN).dropsLike(SEQUOIA_HANGING_SIGN)
        )
    )
    val SEQUOIA_BUTTON = register("sequoia_button", buttonOf(DuskBlockSetType.SEQUOIA_BLOCK_SET_TYPE))
    val SEQUOIA_FENCE = register(
        "sequoia_fence", FenceBlock(
            copy(SEQUOIA_PLANKS)
        )
    )
    val SEQUOIA_FENCE_GATE = register(
        "sequoia_fence_gate",
        FenceGateBlock(
            DuskBlockSetType.SEQUOIA_WOOD_TYPE,
            copy(SEQUOIA_FENCE).solid()
        )
    )
    val SEQUOIA_PRESSURE_PLATE = register(
        "sequoia_pressure_plate",
        PressurePlateBlock(
            DuskBlockSetType.SEQUOIA_BLOCK_SET_TYPE,
            Settings.create().mapColor(SEQUOIA_PLANKS.defaultMapColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(0.5f)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )
    val POISON_BIRCH_LEAVES = register("poison_birch_leaves", PoisonLeavesBlock(copy(BIRCH_LEAVES))).cutout()


    val VOLCANIC_SAND = register(
        "volcanic_sand",
        GravelBlock(
            Color(1644825),
            Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.SNARE)
                .strength(0.5f)
                .sounds(BlockSoundGroup.SAND)
        )
    )

    @JvmStatic
    val SUSPICIOUS_VOLCANIC_SAND = register(
        "suspicious_volcanic_sand",
        BrushableBlock(
            VOLCANIC_SAND,
            SoundEvents.ITEM_BRUSH_BRUSHING_SAND,
            SoundEvents.ITEM_BRUSH_BRUSHING_SAND_COMPLETE,
            Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.SNARE)
                .strength(0.25f).sounds(BlockSoundGroup.SUSPICIOUS_SAND).pistonBehavior(PistonBehavior.DESTROY)
        )
    )
    val ROARING_GEYSER = register(
        "roaring_geyser",
        RoaringGeyserBlock(Settings.create().ticksRandomly())
    )
    val VOLCANIC_SANDSTONE = register(
        "volcanic_sandstone",
        Block(
            Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(0.8f)
        )
    )
    val VOLCANIC_SANDSTONE_STAIRS = register("volcanic_sandstone_stairs", legacyStairsOf(VOLCANIC_SANDSTONE))
    val VOLCANIC_SANDSTONE_SLAB = register(
        "volcanic_sandstone_slab",
        SlabBlock(
            Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(2.0f, 6.0f)
        )
    )
    val VOLCANIC_SANDSTONE_WALL =
        register("volcanic_sandstone_wall", WallBlock(Settings.variantOf(VOLCANIC_SANDSTONE).solid()))
    val CUT_VOLCANIC_SANDSTONE = register(
        "cut_volcanic_sandstone",
        Block(
            Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(0.8f)
        )
    )
    val CUT_VOLCANIC_SANDSTONE_SLAB = register(
        "cut_volcanic_sandstone_slab",
        SlabBlock(
            Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(2.0f, 6.0f)
        )
    )
    val CHISELED_VOLCANIC_SANDSTONE = register(
        "chiseled_volcanic_sandstone",
        Block(
            Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(0.8f)
        )
    )
    val SMOOTH_VOLCANIC_SANDSTONE = register(
        "smooth_volcanic_sandstone",
        Block(
            Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .toolRequired().strength(2.0f, 6.0f)
        )
    )
    val SMOOTH_VOLCANIC_SANDSTONE_STAIRS =
        register("smooth_volcanic_sandstone_stairs", legacyStairsOf(SMOOTH_VOLCANIC_SANDSTONE))
    val SMOOTH_VOLCANIC_SANDSTONE_SLAB =
        register(
            "smooth_volcanic_sandstone_slab",
            SlabBlock(Settings.variantOf(SMOOTH_VOLCANIC_SANDSTONE))
        )


    val CHARRED_LOG = register("charred_log", charredLogOf(charredPlanksColor, charredLogColor))
    val STRIPPED_CHARRED_LOG = register("stripped_charred_log", charredLogOf(charredPlanksColor, charredPlanksColor))
    val CHARRED_WOOD = register(
        "charred_wood",
        PillarBlock(
            Settings.create().mapColor(charredLogColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sounds(BlockSoundGroup.WOOD)
        )
    )
    val STRIPPED_CHARRED_WOOD = register(
        "stripped_charred_wood",
        PillarBlock(
            Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sounds(BlockSoundGroup.WOOD)
        )
    )
    val CHARRED_PLANKS = register(
        "charred_planks",
        Block(
            Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD)
        )
    )
    val CHARRED_STAIRS = register("charred_stairs", legacyStairsOf(CHARRED_PLANKS))
    val CHARRED_SLAB = register(
        "charred_slab",
        SlabBlock(
            Settings.create().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD)
        )
    )
    val CHARRED_DOOR = registerDoor(
        "charred_door",
        DoorBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            Settings.create().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(3.0f).nonOpaque().pistonBehavior(PistonBehavior.DESTROY)
        )
    ).cutout()
    val CHARRED_TRAPDOOR = register(
        "charred_trapdoor", TrapdoorBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            Settings.create().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(3.0f).nonOpaque().allowsSpawning(Blocks::nonSpawnable)
        )
    ).cutout()
    val CHARRED_SIGN = registerNoItem(
        "charred_sign",
        VoidSignBlock(
            charredSignId,
            DuskBlockSetType.CHARRED_WOOD_TYPE,
            Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f)
        )
    )
    val CHARRED_WALL_SIGN = registerNoItem(
        "charred_wall_sign",
        VoidWallSignBlock(
            charredSignId,
            DuskBlockSetType.CHARRED_WOOD_TYPE,
            Settings.create().mapColor(charredPlanksColor).solid().instrument(NoteBlockInstrument.BASS)
                .noCollision().strength(1.0f).dropsLike(CHARRED_SIGN)
        )
    )
    val CHARRED_HANGING_SIGN = registerNoItem(
        "charred_hanging_sign",
        VoidCeilingHangingSignBlock(
            charredHangingSignId,
            DuskBlockSetType.CHARRED_WOOD_TYPE,
            Settings.create().mapColor(charredLogColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f)
        )
    )
    val CHARRED_WALL_HANGING_SIGN = registerNoItem(
        "charred_wall_hanging_sign",
        VoidWallHangingSignBlock(
            charredHangingSignId,
            DuskBlockSetType.CHARRED_WOOD_TYPE,
            Settings.create().mapColor(charredLogColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).lavaIgnitable()
                .dropsLike(OAK_HANGING_SIGN)
        )
    )
    val CHARRED_BUTTON = register("charred_button", buttonOf(DuskBlockSetType.CHARRED_BLOCK_SET_TYPE))
    val CHARRED_FENCE = register(
        "charred_fence",
        FenceBlock(
            Settings.create().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).sounds(BlockSoundGroup.WOOD)
        )
    )
    val CHARRED_FENCE_GATE = register(
        "charred_fence_gate",
        FenceGateBlock(
            DuskBlockSetType.CHARRED_WOOD_TYPE,
            Settings.create().mapColor(charredPlanksColor).solid()
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f)
        )
    )
    val CHARRED_PRESSURE_PLATE = register(
        "charred_pressure_plate",
        PressurePlateBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            Settings.create().mapColor(charredPlanksColor).solid()
                .instrument(NoteBlockInstrument.BASS).noCollision().strength(0.5f)
                .pistonBehavior(PistonBehavior.DESTROY)
        )
    )

    // region DnD
    val GALLERY_MAPLE_SAPLING = register(
        "gallery_maple_sapling", SaplingBlock(
            TreeGrower.AZALEA,
            Settings.create()
                .mapColor(MapColor.RED).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.AZALEA)
                .pistonBehavior(PistonBehavior.DESTROY).luminance(light(1))
        ).cutout()
    )
    val POTTED_GALLERY_MAPLE_SAPLING =
        registerNoItem("potted_gallery_maple_sapling", pottedVariant(GALLERY_MAPLE_SAPLING)).cutout()
    val GALLERY_MAPLE_LEAVES = register(
        "gallery_maple_leaves", LeavesBlock(
            Settings.create().strength(0.2f).ticksRandomly()
                .nonOpaque().allowsSpawning(Blocks::allowOcelotsAndParrots).suffocates(Blocks::nonSolid)
                .blockVision(Blocks::nonSolid).pistonBehavior(PistonBehavior.DESTROY).solidBlock(Blocks::nonSolid)
                .sounds(BlockSoundGroup.GRASS).mapColor(MapColor.RED)
        ).cutout()
    )
    val GALLERY_MAPLE_LOG = register("gallery_maple_log", logOf(MapColor.GRAY, MapColor.BROWN, BlockSoundGroup.WOOD))
    val GALLERY_MAPLE_WOOD = register(
        "gallery_maple_wood", PillarBlock(
            Settings.create().mapColor(MapColor.BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0f)
                .sounds(BlockSoundGroup.WOOD)
        )
    )
    val STRIPPED_GALLERY_MAPLE_LOG = register(
        "stripped_gallery_maple_log", logOf(MapColor.GRAY, MapColor.GRAY, BlockSoundGroup.WOOD)
    )

    val STRIPPED_GALLERY_MAPLE_WOOD = register(
        "stripped_gallery_maple_wood", PillarBlock(copy(GALLERY_MAPLE_WOOD).mapColor(MapColor.GRAY))
    )
    val GALLERY_MAPLE_PLANKS = register(
        "gallery_maple_planks", Block(
            Settings.create()
                .mapColor(MapColor.GRAY).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F)
                .sounds(BlockSoundGroup.WOOD)
        )
    )
    val GALLERY_MAPLE_STAIRS =
        register("gallery_maple_stairs", stairsOf(GALLERY_MAPLE_PLANKS))
    val GALLERY_MAPLE_SLAB =
        register("gallery_maple_slab", slabOf(GALLERY_MAPLE_PLANKS))
    val GALLERY_MAPLE_FENCE =
        register("gallery_maple_fence", fenceOf(GALLERY_MAPLE_PLANKS))
    val GALLERY_MAPLE_FENCE_GATE = register(
        "gallery_maple_fence_gate", fenceGateOf(DuskBlockSetType.GALLERY_MAPLE_WOOD_TYPE, GALLERY_MAPLE_PLANKS)
    )
    val GALLERY_MAPLE_DOOR = registerNoItem(
        "gallery_maple_door", doorOf(DuskBlockSetType.GALLERY_MAPLE_BLOCK_SET_TYPE, GALLERY_MAPLE_PLANKS).cutout()
    )
    val GALLERY_MAPLE_TRAPDOOR = register(
        "gallery_maple_trapdoor",
        trapdoorOf(DuskBlockSetType.GALLERY_MAPLE_BLOCK_SET_TYPE, GALLERY_MAPLE_DOOR).cutout()
    )
    val GALLERY_MAPLE_PRESSURE_PLATE = register(
        "gallery_maple_pressure_plate",
        pressurePlateOf(DuskBlockSetType.GALLERY_MAPLE_BLOCK_SET_TYPE, GALLERY_MAPLE_PLANKS)
    )
    val GALLERY_MAPLE_BUTTON =
        register("gallery_maple_button", buttonOf(DuskBlockSetType.GALLERY_MAPLE_BLOCK_SET_TYPE))

    val GALLERY_MAPLE_SIGN = registerNoItem(
        "gallery_maple_sign", signOf(DuskBlockSetType.GALLERY_MAPLE_WOOD_TYPE, GALLERY_MAPLE_PLANKS)
    )
    val GALLERY_MAPLE_WALL_SIGN = registerNoItem(
        "gallery_maple_wall_sign",
        wallSignOf(DuskBlockSetType.GALLERY_MAPLE_WOOD_TYPE, GALLERY_MAPLE_PLANKS, GALLERY_MAPLE_SIGN)
    )
    val GALLERY_MAPLE_HANGING_SIGN = registerNoItem(
        "gallery_maple_hanging_sign", hangingSignOf(DuskBlockSetType.GALLERY_MAPLE_WOOD_TYPE, GALLERY_MAPLE_PLANKS)
    )
    val GALLERY_MAPLE_WALL_HANGING_SIGN = registerNoItem(
        "gallery_maple_wall_hanging_sign",
        wallHangingSignOf(DuskBlockSetType.GALLERY_MAPLE_WOOD_TYPE, GALLERY_MAPLE_PLANKS, GALLERY_MAPLE_HANGING_SIGN)
    )

    val BONEWOOD_PLANKS = register(
        "bonewood_planks", Block(
            Settings.create()
                .mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.XYLOPHONE).strength(2.0F, 3.0F)
                .sounds(bonewoodSound)
        )
    )
    val BONEWOOD_STAIRS =
        register("bonewood_stairs", stairsOf(BONEWOOD_PLANKS))
    val BONEWOOD_SLAB =
        register("bonewood_slab", slabOf(BONEWOOD_PLANKS))
    val BONEWOOD_FENCE =
        register("bonewood_fence", fenceOf(BONEWOOD_PLANKS))
    val BONEWOOD_FENCE_GATE = register(
        "bonewood_fence_gate", FenceGateBlock(DuskBlockSetType.BONEWOOD_WOOD_TYPE, copy(BONEWOOD_PLANKS).solid())
    )
    val BONEWOOD_DOOR = registerNoItem(
        "bonewood_door",
        DoorBlock(DuskBlockSetType.BONEWOOD_BLOCK_SET_TYPE, copy(BONEWOOD_PLANKS).strength(3.0f).nonOpaque()).cutout()
    )
    val BONEWOOD_TRAPDOOR = register(
        "bonewood_trapdoor",
        TrapdoorBlock(
            DuskBlockSetType.BONEWOOD_BLOCK_SET_TYPE, copy(BONEWOOD_DOOR).allowsSpawning(Blocks::nonSpawnable),
        ).cutout()
    )
    val WITHERING_BONEWOOD_PLANKS = register(
        "withering_bonewood_planks",
        Block(copy(BONEWOOD_PLANKS).mapColor(MapColor.BLACK).sounds(witheringBonewoodSound))
    )
    val WITHERING_BONEWOOD_STAIRS =
        register("withering_bonewood_stairs", stairsOf(WITHERING_BONEWOOD_PLANKS))

    val WITHERING_BONEWOOD_SLAB = register("withering_bonewood_slab", slabOf(WITHERING_BONEWOOD_PLANKS))

    val WITHERING_BONEWOOD_FENCE =
        register("withering_bonewood_fence", fenceOf(WITHERING_BONEWOOD_PLANKS))

    val WITHERING_BONEWOOD_FENCE_GATE = register(
        "withering_bonewood_fence_gate",
        FenceGateBlock(DuskBlockSetType.WITHERING_BONEWOOD_WOOD_TYPE, copy(WITHERING_BONEWOOD_PLANKS).solid())
    )
    val WITHERING_BONEWOOD_DOOR = registerNoItem(
        "withering_bonewood_door",
        DoorBlock(
            DuskBlockSetType.WITHERING_BONEWOOD_BLOCK_SET_TYPE,
            copy(WITHERING_BONEWOOD_PLANKS).strength(3.0f).nonOpaque(),
        ).cutout()
    )
    val WITHERING_BONEWOOD_TRAPDOOR = register(
        "withering_bonewood_trapdoor", TrapdoorBlock(
            DuskBlockSetType.WITHERING_BONEWOOD_BLOCK_SET_TYPE,
            copy(WITHERING_BONEWOOD_DOOR).allowsSpawning(Blocks::nonSpawnable),
        ).cutout()
    )

    val PAINTED_ROSE = register("painted_rose", PaintedRoseBlock(DuskBlockSettings.PAINTED_ROSE).cutout())


    val BROWN_TREE_FUNGUS = register("brown_tree_fungus", TransparentBlock(copy(BROWN_MUSHROOM)).cutout())

    val SPIDERLILY = register(
        "spiderlily", SpiderlilyBlock(copy(ROSE_BUSH).ticksRandomly())
    )
    val JOUNCESHROOM_BLOCK = register(
        "jounceshroom_block", MushroomLaunchBlock(
            copy(BROWN_MUSHROOM_BLOCK).sounds(BlockSoundGroup.SHROOMLIGHT).mapColor(MapColor.PURPLE_TERRACOTTA)
        )
    )
    val WATER_FERN = registerNoItem("water_fern", WaterFernBlock(copy(LILY_PAD)).cutout())

    val BUNNY_GRAVE = register("bunny_grave", BunnyGraveBlock(copy(STONE_BRICK_WALL)))

    // celestal block
    /*  val BIG_CELESTAL_CHAIN = register(
          "big_celestal_chain", BigChainBlock(copy(CHAIN).sounds(BlockSoundGroup.BLOCK_VAULT_BREAK)).cutout()
      )*/
    val BIG_MOON_LANTERN = register(
        "big_moon_lantern",
        BigLanternWithSpiralBlock(
            0xE01638,
            0x8B3DB5,
            copy(/*BIG_SOUL_LANTERN*/ LANTERN).sounds(BlockSoundGroup.BLOCK_TRIAL_SPAWNER_BREAK)
        )
    )
    val BIG_EARTH_LANTERN = register(
        "big_earth_lantern", BigLanternWithSpiralBlock(0xE5AE16, 0xE5B816, copy(BIG_MOON_LANTERN))
    )
    val BIG_COMET_LANTERN = register(
        "big_comet_lantern", BigLanternWithSpiralBlock(0xE57716, 0xCC6C28, copy(BIG_MOON_LANTERN))
    )
    val BIG_SUN_LANTERN = register(
        "big_sun_lantern", BigLanternWithSpiralBlock(0x16E5E5, 0x1470CC, copy(BIG_MOON_LANTERN))
    )
    val BIG_STAR_LANTERN = register(
        "big_star_lantern", BigLanternWithSpiralBlock(0x7E16E5, 0xE52DE5, copy(BIG_MOON_LANTERN))
    )
    val BIG_NEBULAE_LANTERN = register(
        "big_nebulae_lantern", BigLanternWithSpiralBlock(0x24CADA, 0x52D973, copy(BIG_MOON_LANTERN))
    )
    val BIG_ECLIPSE_LANTERN = register(
        "big_eclipse_lantern", BigLanternWithSpiralBlock(0xE5E5E5, 0xBFBFBF, copy(BIG_MOON_LANTERN))
    )

    // Haunted graves
    val HAUNTED_GRAVESTONE = registerHGravestone("haunted_gravestone", STONE)
    val SMALL_HAUNTED_GRAVESTONE = registerSmallHGravestone("small_haunted_gravestone", STONE)
    val HAUNTED_DEEPSLATE_GRAVESTONE = registerHGravestone("haunted_deepslate_gravestone", DEEPSLATE)
    val SMALL_HAUNTED_DEEPSLATE_GRAVESTONE =
        registerSmallHGravestone("small_haunted_deepslate_gravestone", DEEPSLATE)
    val HAUNTED_TUFF_GRAVESTONE = registerHGravestone("haunted_tuff_gravestone", TUFF)
    val SMALL_HAUNTED_TUFF_GRAVESTONE = registerSmallHGravestone("small_haunted_tuff_gravestone", TUFF)
    val HAUNTED_BLACKSTONE_GRAVESTONE = registerHGravestone("haunted_blackstone_gravestone", BLACKSTONE)
    val SMALL_HAUNTED_BLACKSTONE_GRAVESTONE =
        registerSmallHGravestone("small_haunted_blackstone_gravestone", BLACKSTONE)


    val CELESTAL_BELL = register("celestal_bell", CelestalBellBlock(copy(BELL)))

    val MOONCORE = register(
        "mooncore", CrytalClusterWithParticlesBlock(12.0f, 2.0f, DuskBlockSettings.MOONCORE).cutout()
    )
    val TALL_REDSTONE_CRYSTAL = register(
        "tall_redstone_crystal", TallRedstoneCrystalBlock(DuskBlockSettings.REDSTONE_CRYSTAL).cutout()
    )
    val POT_O_SCREAMS = register("pot_o_screams", PotOScreamsBlock(copy(DECORATED_POT)))
    val CHEST_O_SOULS = register("chest_o_souls", ChestOSoulsBlock(copy(CHEST)))

    val QUARTER_BLOCK_PILE = registerNoItem("quarter_block_pile", QuarterBlockPileBlock(Settings.create())).cutout()
    // endregion


    val STATUE = register("statue", StatueBlock(copy(STONE)))

    fun init() {
        DuskBlockSetType.init()
        StrippableBlockRegistry.register(CHARRED_LOG, STRIPPED_CHARRED_LOG)
        StrippableBlockRegistry.register(CHARRED_WOOD, STRIPPED_CHARRED_WOOD)
        StrippableBlockRegistry.register(CYPRESS_LOG, STRIPPED_CYPRESS_LOG)
        StrippableBlockRegistry.register(CYPRESS_WOOD, STRIPPED_CYPRESS_WOOD)
        StrippableBlockRegistry.register(SEQUOIA_LOG, STRIPPED_SEQUOIA_LOG)
        StrippableBlockRegistry.register(SEQUOIA_WOOD, STRIPPED_SEQUOIA_WOOD)

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

    fun registerStrongScaffolding(id: String, block: Block): Block {
        val regBlock = registerNoItem(id, block)
        DuskItems.register(id, StrongScaffoldingItem(regBlock, Item.Settings()))
        return regBlock
    }

    fun registerDoor(id: String, block: Block): Block {
        val regBlock = registerNoItem(id, block)
        DuskItems.register(id, TallBlockItem(regBlock, Item.Settings()))
        return regBlock
    }

    fun registerNoItem(id: String, block: Block): Block {
        val regBlock = Registry.register(Registries.BLOCK, id(id), block)
        BLOCKS.add(regBlock)
        return regBlock
    }


}