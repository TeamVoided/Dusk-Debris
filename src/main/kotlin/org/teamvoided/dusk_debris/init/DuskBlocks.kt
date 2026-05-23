package org.teamvoided.dusk_debris.init

import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.block.BogMudBlock
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.ColorRGBA
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.DoubleHighBlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.Blocks.*
import net.minecraft.world.level.block.WeatheringCopper.WeatherState
import net.minecraft.world.level.block.grower.TreeGrower
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.*
import org.teamvoided.dusk_debris.block.big.BigLanternWithSpiralBlock
import org.teamvoided.dusk_debris.block.sot.*
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
import org.teamvoided.dusks_and_dungeons.util.block.hoe

@Suppress("MemberVisibilityCanBePrivate", "unused", "DEPRECATION")
object DuskBlocks {
    val BLOCKS = mutableSetOf<Block>()
    val CUTOUT_BLOCKS = mutableSetOf<Block>()
    val TRANSLUCENT_BLOCKS = mutableSetOf<Block>()
    val GRASS_TINT_BLOCKS = mutableSetOf<Block>()

    val TEST_BLOCK = register("test_block", EntityTestParticleBlock(ofFullCopy(STONE)))

    val STRONG_SCAFFOLDING =
        registerStrongScaffolding("strong_scaffolding", StrongScaffoldingBlock(ofFullCopy(SCAFFOLDING))).cutout()
    val OVERGROWTH_BLOCK = register("overgrowth_block", OvergrowthBlock(ofFullCopy(MOSS_BLOCK))).grassTint().hoe()
    val OVERGROWTH_BUSH = register("overgrowth_bush", OvergrowthBushBlock(ofFullCopy(AZALEA))).cutout().grassTint()
    val OVERGROWTH_LEAVES = register("overgrowth_leaves", LeavesBlock(ofFullCopy(AZALEA_LEAVES))).cutout().grassTint().hoe()

    //val STONE_CHEST = register("stone_chest", DuskDoubleChestBlock(copy(CHEST)) { DuskBlockEntities.STONE_CHEST })

    val ACID = registerNoItem("acid", LiquidBlock(DuskFluids.ACID, ofFullCopy(WATER).mapColor(MapColor.COLOR_LIGHT_GREEN)))
    val FOG_BUBBLE = registerNoItem(
        "fog_bubble", BubbleBlock(
            Properties.of().mapColor(MapColor.TERRACOTTA_PURPLE).strength(0.25f)
                .sound(SoundType.HONEY_BLOCK).isRedstoneConductor(Blocks::never).randomTicks()
        )
    ).translucent()
    val PURPLE_BUBBLE_BLOSSOM = register(
        "purple_bubble_blossom", BubbleBlossomBlock(ofFullCopy(SPORE_BLOSSOM).randomTicks())
    ).cutout()

    val MYTHROCK = register("mythrock", Block(ofFullCopy(STONE)))
    val MYTHROCK_ARTERY = register("mythrock_artery", MysticalStreamBlock(ofFullCopy(MYTHROCK)))
    val MYTHROCK_HEART = register("mythrock_heart", MysticalPulseBlock(ofFullCopy(MYTHROCK).randomTicks()))

    val EXHAUST_BLOCK = register("exhaust_block", ExhaustBlock(ofFullCopy(DEEPSLATE).randomTicks()))

    val BRONZE_BLOCK = register(
        "bronze_block", Block(
            Properties.of().mapColor(MapColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(3.0F, 6.0F)
                .sound(SoundType.COPPER)
        )
    )
    val CUT_BRONZE = register("cut_bronze", Block(ofFullCopy(BRONZE_BLOCK)))
    val CUT_BRONZE_STAIRS =
        register("cut_bronze_stairs", StairBlock(CUT_BRONZE.defaultBlockState(), ofFullCopy(CUT_BRONZE)))
    val CUT_BRONZE_SLAB = register("cut_bronze_slab", SlabBlock(ofFullCopy(CUT_BRONZE)))
    val CUT_BRONZE_WALL = register("cut_bronze_wall", WallBlock(ofFullCopy(CUT_BRONZE)))
    val BRONZE_TRAPDOOR = register(
        "bronze_trapdoor", TrapDoorBlock(
            DuskBlockSetType.BRONZE, ofFullCopy(BRONZE_BLOCK).noOcclusion().isValidSpawn(Blocks::never)
        )
    ).cutout()
    val BRONZE_GRATE = register(
        "bronze_grate", WaterloggedTransparentBlock(
            ofFullCopy(BRONZE_BLOCK).sound(SoundType.COPPER_GRATE).noOcclusion().requiresCorrectToolForDrops()
                .isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never)
                .isViewBlocking(Blocks::never)
        )
    ).cutout()
    val BRONZE_BULB = register(
        "bronze_bulb", BronzeBulbBlock(
            ofFullCopy(BRONZE_BLOCK).sound(SoundType.COPPER_BULB).requiresCorrectToolForDrops()
                .isRedstoneConductor(Blocks::never).lightLevel(godhomeLuminanceOf())
        )
    )
    val BRONZE_SHIFT_BLOCK = register("bronze_shift_block", ShiftBlock(ofFullCopy(BRONZE_BLOCK)))

    val PALE_SOUL_LANTERN = register(
        "pale_soul_lantern",
        LanternBlock(ofFullCopy(SOUL_LANTERN).lightLevel(light(5)))
    ).cutout()
    val PALE_SOUL_VESSEL = register(
        "pale_soul_vessel",
        SoulVesselBlock(ofFullCopy(PALE_SOUL_LANTERN).sound(vesselBlockSound))
    ).cutout()

    val BLUE_NETHERSHROOM = register(
        "blue_nethershroom",
        NethershroomPlantBlock(
            4,
            DuskConfiguredFeatures.HUGE_BLUE_NETHERSHROOM,
            blueNethershroomSmoke,
            MobEffects.POISON,
            true,
            Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).strength(0.1F)
                .sound(SoundType.FUNGUS).noCollission()
        )
    ).cutout()
    val BLUE_NETHERSHROOM_BLOCK = register(
        "blue_nethershroom_block",
        NethershroomBlock(
            4,
            blueNethershroomSmoke,
            MobEffects.POISON,
            true,
            Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).instrument(NoteBlockInstrument.BASS)
                .strength(0.2f).sound(SoundType.NETHER_WOOD)
        )
    )
    val PURPLE_NETHERSHROOM = register(
        "purple_nethershroom",
        NethershroomPlantBlock(
            16,
            DuskConfiguredFeatures.HUGE_PURPLE_NETHERSHROOM,
            purpleNethershroomSmoke,
            MobEffects.BLINDNESS,
            false,
            Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.1F)
                .sound(SoundType.FUNGUS).noCollission()
        )
    ).cutout()
    val PURPLE_NETHERSHROOM_BLOCK = register(
        "purple_nethershroom_block",
        NethershroomBlock(
            16,
            purpleNethershroomSmoke,
            MobEffects.BLINDNESS,
            false,
            Properties.of().mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASS)
                .strength(0.2f).sound(SoundType.NETHER_WOOD)
        )
    )
    val NETHERSHROOM_STEM = register(
        "nethershroom_stem",
        HugeMushroomBlock(
            Properties.of().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.BASS).strength(0.2f)
                .sound(SoundType.NETHER_WOOD)
        )
    )

    val GUNPOWDER = register(
        "gunpowder",
        GunpowderBlock(
            Properties.of().mapColor(FIRE.defaultMapColor()).sound(SoundType.SAND)
                .ignitedByLava().noCollission().instabreak().pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val GUNPOWDER_BARREL = register(
        "gunpowder_barrel",
        GunpowderBarrelBlock(
            4,
            4,
            gunpowderBarrelColor,
            Properties.of().mapColor(FIRE.defaultMapColor()).instrument(NoteBlockInstrument.BASS)
                .strength(1f, 0.0f).sound(SoundType.WOOD).ignitedByLava().isRedstoneConductor(Blocks::never)
        )
    )
    val STRONGHOLD_GUNPOWDER_BARREL = register(
        "stronghold_gunpowder_barrel", 16,
        GunpowderBarrelBlock(
            10,
            24,
            gunpowderBarrelColor,
            Properties.of().mapColor(FIRE.defaultMapColor()).instrument(NoteBlockInstrument.BASS)
                .strength(1.5f, 0.0f).sound(SoundType.WOOD).ignitedByLava().isRedstoneConductor(Blocks::never)
        )
    )
    val ANCIENT_BLACK_POWDER_BARREL = register(
        "ancient_black_powder_barrel", 1,
        GunpowderBarrelBlock(
            16,
            32,
            gunpowderBarrelBlueColor,
            Properties.of().mapColor(SOUL_FIRE.defaultMapColor())
                .instrument(NoteBlockInstrument.BASS).strength(2f, 0.0f).sound(SoundType.WOOD).ignitedByLava()
                .isRedstoneConductor(Blocks::never)
        )
    )
    val BLUNDERBOMB_BLOCK = registerNoItem(
        "blunderbomb",
        BlunderbombBlock(
            Properties.of().mapColor(MapColor.FIRE).instrument(NoteBlockInstrument.HAT)
                .strength(1f, 0.0f).sound(SoundType.GLASS).isRedstoneConductor(Blocks::never)
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val FIREBOMB_BLOCK = registerNoItem(
        "firebomb",
        FirebombBlock(
            Properties.of().mapColor(MapColor.FIRE).instrument(NoteBlockInstrument.HAT)
                .strength(1f, 0.0f).sound(SoundType.GLASS).isRedstoneConductor(Blocks::never)
                .pushReaction(PushReaction.DESTROY).lightLevel { _: BlockState -> 8 }
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
            Properties.of().mapColor(WHITE_STAINED_GLASS.defaultMapColor()).sound(SoundType.GLASS)
                .instrument(NoteBlockInstrument.HAT).strength(1f, 0.0f).isRedstoneConductor(Blocks::never)
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val POCKETPOISON_BLOCK = registerNoItem(
        "pocketpoison",
        PocketpoisonBlock(
            Properties.of().mapColor(BLUE_NETHERSHROOM.defaultMapColor()).sound(SoundType.GLASS)
                .instrument(NoteBlockInstrument.HAT).strength(1f, 0.0f).isRedstoneConductor(Blocks::never)
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val BLINDBOMB_BLOCK = registerNoItem(
        "blindbomb",
        BlindbombBlock(
            Properties.of().mapColor(PURPLE_NETHERSHROOM.defaultMapColor()).sound(SoundType.GLASS)
                .instrument(NoteBlockInstrument.HAT).strength(1f, 0.0f).isRedstoneConductor(Blocks::never)
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val RED_RIBBON = register("red_ribbon", registerRibbon(RED_WOOL.defaultMapColor()))
    val ORANGE_RIBBON = register("orange_ribbon", registerRibbon(ORANGE_WOOL.defaultMapColor()))
    val YELLOW_RIBBON = register("yellow_ribbon", registerRibbon(YELLOW_WOOL.defaultMapColor()))
    val LIME_RIBBON = register("lime_ribbon", registerRibbon(LIME_WOOL.defaultMapColor()))
    val GREEN_RIBBON = register("green_ribbon", registerRibbon(GREEN_WOOL.defaultMapColor()))
    val CYAN_RIBBON = register("cyan_ribbon", registerRibbon(CYAN_WOOL.defaultMapColor()))
    val BLUE_RIBBON = register("blue_ribbon", registerRibbon(BLUE_WOOL.defaultMapColor()))
    val LIGHT_BLUE_RIBBON = register("light_blue_ribbon", registerRibbon(LIGHT_BLUE_WOOL.defaultMapColor()))
    val PURPLE_RIBBON = register("purple_ribbon", registerRibbon(PURPLE_WOOL.defaultMapColor()))
    val MAGENTA_RIBBON = register("magenta_ribbon", registerRibbon(MAGENTA_WOOL.defaultMapColor()))
    val PINK_RIBBON = register("pink_ribbon", registerRibbon(PINK_WOOL.defaultMapColor()))
    val BROWN_RIBBON = register("brown_ribbon", registerRibbon(BROWN_WOOL.defaultMapColor()))
    val WHITE_RIBBON = register("white_ribbon", registerRibbon(WHITE_WOOL.defaultMapColor()))
    val LIGHT_GRAY_RIBBON = register("light_gray_ribbon", registerRibbon(LIGHT_GRAY_WOOL.defaultMapColor()))
    val GRAY_RIBBON = register("gray_ribbon", registerRibbon(GRAY_WOOL.defaultMapColor()))
    val BLACK_RIBBON = register("black_ribbon", registerRibbon(BLACK_WOOL.defaultMapColor()))

    val RED_CARPET_STAIRS = register("red_carpet_stairs", CarpetStairBlock(ofFullCopy(RED_CARPET)))

    val TREACHEROUS_GOLD_BLOCK = register(
        "treacherous_gold_block", Block(
            Properties.ofLegacyCopy(GOLD_BLOCK).strength(3.5f, 6.0f)
        )
    )
    val TARNISHED_GOLD_BLOCK = register(
        "tarnished_gold_block", Block(
            Properties.ofLegacyCopy(TREACHEROUS_GOLD_BLOCK).mapColor(MapColor.TERRACOTTA_YELLOW)
                .strength(3.5f, 6.0f)
        )
    )
    val LOST_SILVER_BLOCK = register(
        "lost_silver_block", Block(
            Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.5f, 6.0f)
                .sound(SoundType.METAL)
        )
    )
    val SUNKEN_BRONZE_BLOCK = register(
        "sunken_bronze_block", Block(
            Properties.of().mapColor(MapColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(3.5f, 6.0f)
                .sound(SoundType.METAL)
        )
    )
    val TREACHEROUS_GOLD_COIN_STACK = registerNoItem(
        "treacherous_gold_coin_stack",
        CoinStackBlock(coin_stack_settings.mapColor(TREACHEROUS_GOLD_BLOCK.defaultMapColor()))
    ).cutout()
    val TREACHEROUS_GOLD_COIN_PILE = registerNoItem(
        "treacherous_gold_coin_pile", CoinPileBlock(coin_pile_settings.mapColor(TREACHEROUS_GOLD_BLOCK.defaultMapColor()))
    )
    val TARNISHED_GOLD_COIN_STACK = registerNoItem(
        "tarnished_gold_coin_stack", CoinStackBlock(coin_stack_settings.mapColor(TARNISHED_GOLD_BLOCK.defaultMapColor()))
    ).cutout()
    val TARNISHED_GOLD_COIN_PILE = registerNoItem(
        "tarnished_gold_coin_pile", CoinPileBlock(coin_pile_settings.mapColor(TARNISHED_GOLD_BLOCK.defaultMapColor()))
    )
    val LOST_SILVER_COIN_STACK = registerNoItem(
        "lost_silver_coin_stack", CoinStackBlock(coin_stack_settings.mapColor(LOST_SILVER_BLOCK.defaultMapColor()))
    ).cutout()
    val LOST_SILVER_COIN_PILE = registerNoItem(
        "lost_silver_coin_pile", CoinPileBlock(coin_pile_settings.mapColor(LOST_SILVER_BLOCK.defaultMapColor()))
    )
    val SUNKEN_BRONZE_COIN_STACK = registerNoItem(
        "sunken_bronze_coin_stack", CoinStackBlock(coin_stack_settings.mapColor(SUNKEN_BRONZE_BLOCK.defaultMapColor()))
    ).cutout()
    val SUNKEN_BRONZE_COIN_PILE = registerNoItem(
        "sunken_bronze_coin_pile", CoinPileBlock(coin_pile_settings.mapColor(SUNKEN_BRONZE_BLOCK.defaultMapColor()))
    )
    val GOLDEN_VESSEL = register(
        "golden_vessel", 16,
        MysteriousVesselBlock(
            Properties.of().mapColor(TREACHEROUS_GOLD_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val DROWNED_VESSEL = register(
        "drowned_vessel", 16,
        MysteriousVesselBlock(
            Properties.of().mapColor(TARNISHED_GOLD_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val PURE_VESSEL = register(
        "pure_vessel", 16,
        MysteriousVesselBlock(
            Properties.of().mapColor(LOST_SILVER_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val DARKENED_VESSEL = register(
        "darkened_vessel", 16,
        MysteriousVesselBlock(
            Properties.of().mapColor(SUNKEN_BRONZE_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val GILDED_CHALICE = register(
        "gilded_chalice", 16, GildedChaliceBlock(
            Properties.of().mapColor(TREACHEROUS_GOLD_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val TARNISHED_CHALICE = register(
        "tarnished_chalice", 16, GildedChaliceBlock(
            Properties.of().mapColor(TARNISHED_GOLD_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val SILVERED_CHALICE = register(
        "silvered_chalice", 16, GildedChaliceBlock(
            Properties.of().mapColor(LOST_SILVER_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val BRONZED_CHALICE = register(
        "bronzed_chalice", 16, GildedChaliceBlock(
            Properties.of().mapColor(SUNKEN_BRONZE_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()

    val STACKED_CHALICE = register("stacked_chalice", 16, StackedChaliceBlock(ofFullCopy(GILDED_CHALICE))).cutout()

    val LAPIS_RELIC = register(
        "lapis_relic", 16, PerculiarRelicBlock(
            Properties.of().mapColor(LAPIS_BLOCK.defaultMapColor()).pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val GOLDEN_RUBY_CROWN = register(
        "golden_ruby_crown", 16, RoyalCrownBlock(
            Properties.of().mapColor(REDSTONE_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val GOLDEN_SAPPHIRE_CROWN = register(
        "golden_sapphire_crown", 16, RoyalCrownBlock(
            Properties.of().mapColor(LAPIS_BLOCK.defaultMapColor()).pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val GOLDEN_QUARTZ_CROWN = register(
        "golden_quartz_crown", 16, RoyalCrownBlock(
            Properties.of().mapColor(QUARTZ_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()

    val LEGENDARY_CRYSTAL_CROWN = register(
        "legendary_crystal_crown", 16, RoyalCrownBlock(
            Properties.of().mapColor(DIAMOND_BLOCK.defaultMapColor())
                .pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val FORGOTTEN_CHEST = register(
        "forgotten_chest",
        TreasureChestBlock(
            Properties.of().pushReaction(PushReaction.IGNORE)
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
        Block(ofFullCopy(AMETHYST_BLOCK))
    )
    val CRYSTAL_PILLAR_BLOCK = register(
        "crystal_pillar_block",
        RotatedPillarBlock(ofFullCopy(CRYSTAL_BLOCK))
    )

    val WAXED_OXIDIZED_COPPER_FAN = register(
        "waxed_oxidized_copper_fan", FanBlock(
            4,
            Properties.of().mapColor(OXIDIZED_COPPER.defaultMapColor()).strength(3.0F, 6.0F)
                .sound(SoundType.COPPER_BULB).requiresCorrectToolForDrops().isRedstoneConductor(Blocks::never)
        )
    )
    val WAXED_WEATHERED_COPPER_FAN = register(
        "waxed_weathered_copper_fan",
        FanBlock(
            8,
            ofFullCopy(WAXED_OXIDIZED_COPPER_FAN).mapColor(WEATHERED_COPPER.defaultMapColor())
        )
    )
    val WAXED_EXPOSED_COPPER_FAN = register(
        "waxed_exposed_copper_fan",
        FanBlock(
            12,
            ofFullCopy(WAXED_WEATHERED_COPPER_FAN).mapColor(EXPOSED_COPPER.defaultMapColor())
        )
    )
    val WAXED_COPPER_FAN = register(
        "waxed_copper_fan",
        FanBlock(15, ofFullCopy(WAXED_EXPOSED_COPPER_FAN).mapColor(COPPER_BLOCK.defaultMapColor()))
    )

    val OXIDIZED_COPPER_FAN = register(
        "oxidized_copper_fan",
        OxidizableFanBlock(
            WeatherState.OXIDIZED, 4,
            ofFullCopy(WAXED_OXIDIZED_COPPER_FAN).randomTicks()
        )
    )
    val WEATHERED_COPPER_FAN = register(
        "weathered_copper_fan",
        OxidizableFanBlock(
            WeatherState.EXPOSED, 8,
            ofFullCopy(WAXED_WEATHERED_COPPER_FAN).randomTicks()
        )
    )
    val EXPOSED_COPPER_FAN = register(
        "exposed_copper_fan",
        OxidizableFanBlock(
            WeatherState.WEATHERED, 12,
            ofFullCopy(WAXED_EXPOSED_COPPER_FAN).randomTicks()
        )
    )
    val COPPER_FAN = register(
        "copper_fan",
        OxidizableFanBlock(
            WeatherState.UNAFFECTED, 15,
            ofFullCopy(WAXED_COPPER_FAN).randomTicks()
        )
    )

    val PAPER_BLOCK = register(
        "paper_block",
        PaperBlock(
            Properties.of().mapColor(WHITE_WOOL.defaultMapColor()).strength(0.25F)
        )
    )

    val BOG_MUD = registerNoItem(
        "bog_mud", BogMudBlock(
            Properties.ofLegacyCopy(MUD).dynamicShape()
                .isValidSpawn(Blocks::always)
                .isRedstoneConductor(Blocks::never)
                .isViewBlocking(Blocks::always)
                .isSuffocating(Blocks::never)
        )
    )

    val CYPRESS_LEAVES = register("cypress_leaves", leaves(SoundType.GRASS)).cutout()
    val CYPRESS_LOG = register("cypress_log", log(charredPlanksColor, charredLogColor))
    val STRIPPED_CYPRESS_LOG = register("stripped_cypress_log", log(charredPlanksColor, charredPlanksColor))
    val CYPRESS_WOOD = register(
        "cypress_wood",
        RotatedPillarBlock(
            Properties.of().mapColor(charredLogColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sound(SoundType.WOOD).ignitedByLava()
        )
    )
    val STRIPPED_CYPRESS_WOOD = register("stripped_cypress_wood", RotatedPillarBlock(ofFullCopy(CYPRESS_WOOD)))
    val CYPRESS_PLANKS = register(
        "cypress_planks", Block(
            Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sound(SoundType.WOOD).ignitedByLava()
        )
    )
    val CYPRESS_STAIRS = register("cypress_stairs", legacyStair(CYPRESS_PLANKS))
    val CYPRESS_SLAB = register("cypress_slab", SlabBlock(ofFullCopy(CYPRESS_PLANKS)))
    val CYPRESS_DOOR = registerDoor(
        "cypress_door",
        DoorBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            Properties.of().mapColor(CYPRESS_PLANKS.defaultMapColor())
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).noOcclusion().pushReaction(PushReaction.DESTROY).ignitedByLava()
        )
    ).cutout()
    val CYPRESS_TRAPDOOR = register(
        "cypress_trapdoor", TrapDoorBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            Properties.of().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).noOcclusion().isValidSpawn(Blocks::never).ignitedByLava()
        )
    ).cutout()
    val CYPRESS_SIGN = registerNoItem(
        "cypress_sign",
        VoidSignBlock(
            cypressSignId,
            DuskBlockSetType.CYPRESS_WOOD_TYPE,
            Properties.of().mapColor(charredPlanksColor).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                .noCollission().strength(1.0f).ignitedByLava()
        )
    )
    val CYPRESS_WALL_SIGN = registerNoItem(
        "cypress_wall_sign",
        VoidWallSignBlock(
            cypressSignId,
            DuskBlockSetType.CYPRESS_WOOD_TYPE,
            Properties.of().mapColor(charredPlanksColor).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                .noCollission().strength(1.0f).dropsLike(CYPRESS_SIGN).ignitedByLava()
        )
    )
    val CYPRESS_HANGING_SIGN = registerNoItem(
        "cypress_hanging_sign",
        VoidCeilingHangingSignBlock(
            cypressHangingSignId,
            DuskBlockSetType.CYPRESS_WOOD_TYPE,
            Properties.of().mapColor(charredLogColor).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                .noCollission().strength(1.0f).ignitedByLava()
        )
    )
    val CYPRESS_WALL_HANGING_SIGN = registerNoItem(
        "cypress_wall_hanging_sign",
        VoidWallHangingSignBlock(
            cypressHangingSignId,
            DuskBlockSetType.CYPRESS_WOOD_TYPE,
            Properties.of().mapColor(charredLogColor).forceSolidOn()
                .instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0f).ignitedByLava()
                .dropsLike(OAK_HANGING_SIGN)
        )
    )
    val CYPRESS_BUTTON = register("cypress_button", woodenButton(DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE))
    val CYPRESS_FENCE = register(
        "cypress_fence",
        FenceBlock(
            Properties.of().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).sound(SoundType.WOOD)
        )
    )
    val CYPRESS_FENCE_GATE = register(
        "cypress_fence_gate",
        FenceGateBlock(
            DuskBlockSetType.CYPRESS_WOOD_TYPE,
            Properties.of().mapColor(charredPlanksColor).forceSolidOn()
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f)
        )
    )
    val CYPRESS_PRESSURE_PLATE = register(
        "cypress_pressure_plate",
        PressurePlateBlock(
            DuskBlockSetType.CYPRESS_BLOCK_SET_TYPE,
            Properties.of().mapColor(charredPlanksColor).forceSolidOn()
                .instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5f)
                .pushReaction(PushReaction.DESTROY)
        )
    )

    val SEQUOIA_LEAVES = register("sequoia_leaves", LongLeavesBlock(ofFullCopy(SPRUCE_LEAVES))).cutout()
    val SEQUOIA_LOG = register("sequoia_log", log(charredPlanksColor, charredLogColor))
    val STRIPPED_SEQUOIA_LOG = register("stripped_sequoia_log", log(charredPlanksColor, charredPlanksColor))
    val SEQUOIA_WOOD = register(
        "sequoia_wood", RotatedPillarBlock(
            Properties.of().mapColor(charredLogColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sound(SoundType.WOOD).ignitedByLava()
        )
    )
    val STRIPPED_SEQUOIA_WOOD = register("stripped_sequoia_wood", RotatedPillarBlock(ofFullCopy(SEQUOIA_WOOD)))
    val SEQUOIA_PLANKS = register(
        "sequoia_planks", Block(
            Properties.of().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sound(SoundType.WOOD).ignitedByLava()
        )
    )
    val SEQUOIA_STAIRS = register("sequoia_stairs", legacyStair(SEQUOIA_PLANKS))
    val SEQUOIA_SLAB = register("sequoia_slab", SlabBlock(ofFullCopy(SEQUOIA_PLANKS)))
    val SEQUOIA_DOOR = registerDoor(
        "sequoia_door", DoorBlock(
            DuskBlockSetType.SEQUOIA_BLOCK_SET_TYPE,
            Properties.of().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).noOcclusion().pushReaction(PushReaction.DESTROY).ignitedByLava()
        )
    ).cutout()
    val SEQUOIA_TRAPDOOR = register(
        "sequoia_trapdoor", TrapDoorBlock(
            DuskBlockSetType.SEQUOIA_BLOCK_SET_TYPE,
            Properties.of().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(3.0f).noOcclusion().isValidSpawn(Blocks::never).ignitedByLava()
        )
    ).cutout()
    val SEQUOIA_SIGN = registerNoItem(
        "sequoia_sign", VoidSignBlock(
            sequoiaSignId,
            DuskBlockSetType.SEQUOIA_WOOD_TYPE,
            Properties.of().mapColor(charredPlanksColor).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                .noCollission().strength(1.0f).ignitedByLava()
        )
    )
    val SEQUOIA_WALL_SIGN = registerNoItem(
        "sequoia_wall_sign", VoidWallSignBlock(
            sequoiaSignId,
            DuskBlockSetType.SEQUOIA_WOOD_TYPE,
            ofFullCopy(SEQUOIA_SIGN).dropsLike(SEQUOIA_SIGN)
        )
    )
    val SEQUOIA_HANGING_SIGN = registerNoItem(
        "sequoia_hanging_sign", VoidCeilingHangingSignBlock(
            sequoiaHangingSignId,
            DuskBlockSetType.SEQUOIA_WOOD_TYPE,
            ofFullCopy(SEQUOIA_SIGN)
        )
    )
    val SEQUOIA_WALL_HANGING_SIGN = registerNoItem(
        "sequoia_wall_hanging_sign", VoidWallHangingSignBlock(
            sequoiaHangingSignId,
            DuskBlockSetType.SEQUOIA_WOOD_TYPE,
            ofFullCopy(SEQUOIA_HANGING_SIGN).dropsLike(SEQUOIA_HANGING_SIGN)
        )
    )
    val SEQUOIA_BUTTON = register("sequoia_button", woodenButton(DuskBlockSetType.SEQUOIA_BLOCK_SET_TYPE))
    val SEQUOIA_FENCE = register(
        "sequoia_fence", FenceBlock(
            ofFullCopy(SEQUOIA_PLANKS)
        )
    )
    val SEQUOIA_FENCE_GATE = register(
        "sequoia_fence_gate",
        FenceGateBlock(
            DuskBlockSetType.SEQUOIA_WOOD_TYPE,
            ofFullCopy(SEQUOIA_FENCE).forceSolidOn()
        )
    )
    val SEQUOIA_PRESSURE_PLATE = register(
        "sequoia_pressure_plate",
        PressurePlateBlock(
            DuskBlockSetType.SEQUOIA_BLOCK_SET_TYPE,
            Properties.of().mapColor(SEQUOIA_PLANKS.defaultMapColor()).forceSolidOn()
                .instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5f)
                .pushReaction(PushReaction.DESTROY)
        )
    )
    val POISON_BIRCH_LEAVES = register("poison_birch_leaves", PoisonLeavesBlock(ofFullCopy(BIRCH_LEAVES))).cutout()


    val VOLCANIC_SAND = register(
        "volcanic_sand",
        ColoredFallingBlock(
            ColorRGBA(1644825),
            Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.SNARE)
                .strength(0.5f)
                .sound(SoundType.SAND)
        )
    )

    @JvmStatic
    val SUSPICIOUS_VOLCANIC_SAND = register(
        "suspicious_volcanic_sand",
        BrushableBlock(
            VOLCANIC_SAND,
            SoundEvents.BRUSH_SAND,
            SoundEvents.BRUSH_SAND_COMPLETED,
            Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.SNARE)
                .strength(0.25f).sound(SoundType.SUSPICIOUS_SAND).pushReaction(PushReaction.DESTROY)
        )
    )
    val ROARING_GEYSER = register(
        "roaring_geyser",
        RoaringGeyserBlock(Properties.of().randomTicks())
    )
    val VOLCANIC_SANDSTONE = register(
        "volcanic_sandstone",
        Block(
            Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops().strength(0.8f)
        )
    )
    val VOLCANIC_SANDSTONE_STAIRS = register("volcanic_sandstone_stairs", legacyStair(VOLCANIC_SANDSTONE))
    val VOLCANIC_SANDSTONE_SLAB = register(
        "volcanic_sandstone_slab",
        SlabBlock(
            Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops().strength(2.0f, 6.0f)
        )
    )
    val VOLCANIC_SANDSTONE_WALL =
        register("volcanic_sandstone_wall", WallBlock(Properties.ofLegacyCopy(VOLCANIC_SANDSTONE).forceSolidOn()))
    val CUT_VOLCANIC_SANDSTONE = register(
        "cut_volcanic_sandstone",
        Block(
            Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops().strength(0.8f)
        )
    )
    val CUT_VOLCANIC_SANDSTONE_SLAB = register(
        "cut_volcanic_sandstone_slab",
        SlabBlock(
            Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops().strength(2.0f, 6.0f)
        )
    )
    val CHISELED_VOLCANIC_SANDSTONE = register(
        "chiseled_volcanic_sandstone",
        Block(
            Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops().strength(0.8f)
        )
    )
    val SMOOTH_VOLCANIC_SANDSTONE = register(
        "smooth_volcanic_sandstone",
        Block(
            Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops().strength(2.0f, 6.0f)
        )
    )
    val SMOOTH_VOLCANIC_SANDSTONE_STAIRS =
        register("smooth_volcanic_sandstone_stairs", legacyStair(SMOOTH_VOLCANIC_SANDSTONE))
    val SMOOTH_VOLCANIC_SANDSTONE_SLAB =
        register(
            "smooth_volcanic_sandstone_slab",
            SlabBlock(Properties.ofLegacyCopy(SMOOTH_VOLCANIC_SANDSTONE))
        )


    val CHARRED_LOG = register("charred_log", charredLogOf(charredPlanksColor, charredLogColor))
    val STRIPPED_CHARRED_LOG = register("stripped_charred_log", charredLogOf(charredPlanksColor, charredPlanksColor))
    val CHARRED_WOOD = register(
        "charred_wood",
        RotatedPillarBlock(
            Properties.of().mapColor(charredLogColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sound(SoundType.WOOD)
        )
    )
    val STRIPPED_CHARRED_WOOD = register(
        "stripped_charred_wood",
        RotatedPillarBlock(
            Properties.of().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f)
                .sound(SoundType.WOOD)
        )
    )
    val CHARRED_PLANKS = register(
        "charred_planks",
        Block(
            Properties.of().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sound(SoundType.WOOD)
        )
    )
    val CHARRED_STAIRS = register("charred_stairs", legacyStair(CHARRED_PLANKS))
    val CHARRED_SLAB = register(
        "charred_slab",
        SlabBlock(
            Properties.of().mapColor(charredPlanksColor).instrument(NoteBlockInstrument.BASS)
                .strength(2.0f, 3.0f).sound(SoundType.WOOD)
        )
    )
    val CHARRED_DOOR = registerDoor(
        "charred_door",
        DoorBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            Properties.of().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(3.0f).noOcclusion().pushReaction(PushReaction.DESTROY)
        )
    ).cutout()
    val CHARRED_TRAPDOOR = register(
        "charred_trapdoor", TrapDoorBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            Properties.of().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(3.0f).noOcclusion().isValidSpawn(Blocks::never)
        )
    ).cutout()
    val CHARRED_SIGN = registerNoItem(
        "charred_sign",
        VoidSignBlock(
            charredSignId,
            DuskBlockSetType.CHARRED_WOOD_TYPE,
            Properties.of().mapColor(charredPlanksColor).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                .noCollission().strength(1.0f)
        )
    )
    val CHARRED_WALL_SIGN = registerNoItem(
        "charred_wall_sign",
        VoidWallSignBlock(
            charredSignId,
            DuskBlockSetType.CHARRED_WOOD_TYPE,
            Properties.of().mapColor(charredPlanksColor).forceSolidOn().instrument(NoteBlockInstrument.BASS)
                .noCollission().strength(1.0f).dropsLike(CHARRED_SIGN)
        )
    )
    val CHARRED_HANGING_SIGN = registerNoItem(
        "charred_hanging_sign",
        VoidCeilingHangingSignBlock(
            charredHangingSignId,
            DuskBlockSetType.CHARRED_WOOD_TYPE,
            Properties.of().mapColor(charredLogColor).forceSolidOn()
                .instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0f)
        )
    )
    val CHARRED_WALL_HANGING_SIGN = registerNoItem(
        "charred_wall_hanging_sign",
        VoidWallHangingSignBlock(
            charredHangingSignId,
            DuskBlockSetType.CHARRED_WOOD_TYPE,
            Properties.of().mapColor(charredLogColor).forceSolidOn()
                .instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0f).ignitedByLava()
                .dropsLike(OAK_HANGING_SIGN)
        )
    )
    val CHARRED_BUTTON = register("charred_button", woodenButton(DuskBlockSetType.CHARRED_BLOCK_SET_TYPE))
    val CHARRED_FENCE = register(
        "charred_fence",
        FenceBlock(
            Properties.of().mapColor(charredPlanksColor)
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).sound(SoundType.WOOD)
        )
    )
    val CHARRED_FENCE_GATE = register(
        "charred_fence_gate",
        FenceGateBlock(
            DuskBlockSetType.CHARRED_WOOD_TYPE,
            Properties.of().mapColor(charredPlanksColor).forceSolidOn()
                .instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f)
        )
    )
    val CHARRED_PRESSURE_PLATE = register(
        "charred_pressure_plate",
        PressurePlateBlock(
            DuskBlockSetType.CHARRED_BLOCK_SET_TYPE,
            Properties.of().mapColor(charredPlanksColor).forceSolidOn()
                .instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5f)
                .pushReaction(PushReaction.DESTROY)
        )
    )

    // region DnD
    val GALLERY_MAPLE_SAPLING = register(
        "gallery_maple_sapling", SaplingBlock(
            TreeGrower.AZALEA,
            Properties.of()
                .mapColor(MapColor.COLOR_RED).noCollission().randomTicks().instabreak().sound(SoundType.AZALEA)
                .pushReaction(PushReaction.DESTROY).lightLevel(light(1))
        ).cutout()
    )
    val POTTED_GALLERY_MAPLE_SAPLING =
        registerNoItem("potted_gallery_maple_sapling", flowerPot(GALLERY_MAPLE_SAPLING)).cutout()
    val GALLERY_MAPLE_LEAVES = register(
        "gallery_maple_leaves", LeavesBlock(
            Properties.of().strength(0.2f).randomTicks()
                .noOcclusion().isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(Blocks::never)
                .isViewBlocking(Blocks::never).pushReaction(PushReaction.DESTROY).isRedstoneConductor(Blocks::never)
                .sound(SoundType.GRASS).mapColor(MapColor.COLOR_RED)
        ).cutout()
    )
    val GALLERY_MAPLE_LOG = register("gallery_maple_log", log(MapColor.COLOR_GRAY, MapColor.COLOR_BROWN, SoundType.WOOD))
    val GALLERY_MAPLE_WOOD = register(
        "gallery_maple_wood", RotatedPillarBlock(
            Properties.of().mapColor(MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0f)
                .sound(SoundType.WOOD)
        )
    )
    val STRIPPED_GALLERY_MAPLE_LOG = register(
        "stripped_gallery_maple_log", log(MapColor.COLOR_GRAY, MapColor.COLOR_GRAY, SoundType.WOOD)
    )

    val STRIPPED_GALLERY_MAPLE_WOOD = register(
        "stripped_gallery_maple_wood", RotatedPillarBlock(ofFullCopy(GALLERY_MAPLE_WOOD).mapColor(MapColor.COLOR_GRAY))
    )
    val GALLERY_MAPLE_PLANKS = register(
        "gallery_maple_planks", Block(
            Properties.of()
                .mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
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
        register("gallery_maple_button", woodenButton(DuskBlockSetType.GALLERY_MAPLE_BLOCK_SET_TYPE))

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
            Properties.of()
                .mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.XYLOPHONE).strength(2.0F, 3.0F)
                .sound(bonewoodSound)
        )
    )
    val BONEWOOD_STAIRS =
        register("bonewood_stairs", stairsOf(BONEWOOD_PLANKS))
    val BONEWOOD_SLAB =
        register("bonewood_slab", slabOf(BONEWOOD_PLANKS))
    val BONEWOOD_FENCE =
        register("bonewood_fence", fenceOf(BONEWOOD_PLANKS))
    val BONEWOOD_FENCE_GATE = register(
        "bonewood_fence_gate", FenceGateBlock(DuskBlockSetType.BONEWOOD_WOOD_TYPE, ofFullCopy(BONEWOOD_PLANKS).forceSolidOn())
    )
    val BONEWOOD_DOOR = registerNoItem(
        "bonewood_door",
        DoorBlock(DuskBlockSetType.BONEWOOD_BLOCK_SET_TYPE, ofFullCopy(BONEWOOD_PLANKS).strength(3.0f).noOcclusion()).cutout()
    )
    val BONEWOOD_TRAPDOOR = register(
        "bonewood_trapdoor",
        TrapDoorBlock(
            DuskBlockSetType.BONEWOOD_BLOCK_SET_TYPE, ofFullCopy(BONEWOOD_DOOR).isValidSpawn(Blocks::never),
        ).cutout()
    )
    val WITHERING_BONEWOOD_PLANKS = register(
        "withering_bonewood_planks",
        Block(ofFullCopy(BONEWOOD_PLANKS).mapColor(MapColor.COLOR_BLACK).sound(witheringBonewoodSound))
    )
    val WITHERING_BONEWOOD_STAIRS =
        register("withering_bonewood_stairs", stairsOf(WITHERING_BONEWOOD_PLANKS))

    val WITHERING_BONEWOOD_SLAB = register("withering_bonewood_slab", slabOf(WITHERING_BONEWOOD_PLANKS))

    val WITHERING_BONEWOOD_FENCE =
        register("withering_bonewood_fence", fenceOf(WITHERING_BONEWOOD_PLANKS))

    val WITHERING_BONEWOOD_FENCE_GATE = register(
        "withering_bonewood_fence_gate",
        FenceGateBlock(DuskBlockSetType.WITHERING_BONEWOOD_WOOD_TYPE, ofFullCopy(WITHERING_BONEWOOD_PLANKS).forceSolidOn())
    )
    val WITHERING_BONEWOOD_DOOR = registerNoItem(
        "withering_bonewood_door",
        DoorBlock(
            DuskBlockSetType.WITHERING_BONEWOOD_BLOCK_SET_TYPE,
            ofFullCopy(WITHERING_BONEWOOD_PLANKS).strength(3.0f).noOcclusion(),
        ).cutout()
    )
    val WITHERING_BONEWOOD_TRAPDOOR = register(
        "withering_bonewood_trapdoor", TrapDoorBlock(
            DuskBlockSetType.WITHERING_BONEWOOD_BLOCK_SET_TYPE,
            ofFullCopy(WITHERING_BONEWOOD_DOOR).isValidSpawn(Blocks::never),
        ).cutout()
    )

    val PAINTED_ROSE = register("painted_rose", PaintedRoseBlock(DuskBlockSettings.PAINTED_ROSE).cutout())


    val BROWN_TREE_FUNGUS = register("brown_tree_fungus", HalfTransparentBlock(ofFullCopy(BROWN_MUSHROOM)).cutout())

    val SPIDERLILY = register(
        "spiderlily", SpiderlilyBlock(ofFullCopy(ROSE_BUSH).randomTicks())
    )
    val JOUNCESHROOM_BLOCK = register(
        "jounceshroom_block", MushroomLaunchBlock(
            ofFullCopy(BROWN_MUSHROOM_BLOCK).sound(SoundType.SHROOMLIGHT).mapColor(MapColor.TERRACOTTA_PURPLE)
        )
    )
    val WATER_FERN = registerNoItem("water_fern", WaterFernBlock(ofFullCopy(LILY_PAD)).cutout())

    val BUNNY_GRAVE = register("bunny_grave", BunnyGraveBlock(ofFullCopy(STONE_BRICK_WALL)))

    // celestal block
    /*  val BIG_CELESTAL_CHAIN = register(
          "big_celestal_chain", BigChainBlock(copy(CHAIN).sounds(BlockSoundGroup.BLOCK_VAULT_BREAK)).cutout()
      )*/
    val BIG_MOON_LANTERN = register(
        "big_moon_lantern",
        BigLanternWithSpiralBlock(
            0xE01638,
            0x8B3DB5,
            ofFullCopy(/*BIG_SOUL_LANTERN*/ LANTERN).sound(SoundType.TRIAL_SPAWNER)
        )
    )
    val BIG_EARTH_LANTERN = register(
        "big_earth_lantern", BigLanternWithSpiralBlock(0xE5AE16, 0xE5B816, ofFullCopy(BIG_MOON_LANTERN))
    )
    val BIG_COMET_LANTERN = register(
        "big_comet_lantern", BigLanternWithSpiralBlock(0xE57716, 0xCC6C28, ofFullCopy(BIG_MOON_LANTERN))
    )
    val BIG_SUN_LANTERN = register(
        "big_sun_lantern", BigLanternWithSpiralBlock(0x16E5E5, 0x1470CC, ofFullCopy(BIG_MOON_LANTERN))
    )
    val BIG_STAR_LANTERN = register(
        "big_star_lantern", BigLanternWithSpiralBlock(0x7E16E5, 0xE52DE5, ofFullCopy(BIG_MOON_LANTERN))
    )
    val BIG_NEBULAE_LANTERN = register(
        "big_nebulae_lantern", BigLanternWithSpiralBlock(0x24CADA, 0x52D973, ofFullCopy(BIG_MOON_LANTERN))
    )
    val BIG_ECLIPSE_LANTERN = register(
        "big_eclipse_lantern", BigLanternWithSpiralBlock(0xE5E5E5, 0xBFBFBF, ofFullCopy(BIG_MOON_LANTERN))
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


    val CELESTAL_BELL = register("celestal_bell", CelestalBellBlock(ofFullCopy(BELL)))

    val MOONCORE = register(
        "mooncore", CrytalClusterWithParticlesBlock(12.0f, 2.0f, DuskBlockSettings.MOONCORE).cutout()
    )
    val TALL_REDSTONE_CRYSTAL = register(
        "tall_redstone_crystal", TallRedstoneCrystalBlock(DuskBlockSettings.REDSTONE_CRYSTAL).cutout()
    )
    val POT_O_SCREAMS = register("pot_o_screams", PotOScreamsBlock(ofFullCopy(DECORATED_POT)))
    val CHEST_O_SOULS = register("chest_o_souls", ChestOSoulsBlock(ofFullCopy(CHEST)))

    val QUARTER_BLOCK_PILE = registerNoItem("quarter_block_pile", QuarterBlockPileBlock(Properties.of())).cutout()
    // endregion


    val STATUE = register("statue", StatueBlock(ofFullCopy(STONE)))

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
        DuskItems.register(id, BlockItem(regBlock, Item.Properties()))
        return regBlock
    }

    fun register(id: String, maxCount: Int, block: Block): Block {
        val regBlock = registerNoItem(id, block)
        DuskItems.register(id, BlockItem(regBlock, Item.Properties().stacksTo(maxCount)))
        return regBlock
    }

    fun registerStrongScaffolding(id: String, block: Block): Block {
        val regBlock = registerNoItem(id, block)
        DuskItems.register(id, StrongScaffoldingItem(regBlock, Item.Properties()))
        return regBlock
    }

    fun registerDoor(id: String, block: Block): Block {
        val regBlock = registerNoItem(id, block)
        DuskItems.register(id, DoubleHighBlockItem(regBlock, Item.Properties()))
        return regBlock
    }

    fun registerNoItem(id: String, block: Block): Block {
        val regBlock = Registry.register(BuiltInRegistries.BLOCK, id(id), block)
        BLOCKS.add(regBlock)
        return regBlock
    }


}