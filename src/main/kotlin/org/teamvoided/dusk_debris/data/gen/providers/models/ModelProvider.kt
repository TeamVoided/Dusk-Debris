package org.teamvoided.dusk_debris.data.gen.providers.models


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.BlockModelGenerators.TintState
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.*
import net.minecraft.world.level.block.Blocks
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.DuskDebris.mc
import org.teamvoided.dusk_debris.block.DuskBlockFamilies
import org.teamvoided.dusk_debris.block.DuskBlockLists
import org.teamvoided.dusk_debris.data.gen.providers.models.MinecraftModelProvider.generateAlternativeMinecraftModels
import org.teamvoided.dusk_debris.data.gen.providers.models.StoneModelProvider.generateStoneModels
import org.teamvoided.dusk_debris.data.gen.providers.models.WoodModelProvider.generateWoodModels
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.item.DuskItemLists
import org.teamvoided.dusk_debris.util.*
import org.teamvoided.dusk_debris.util.model_helper.*
import java.util.*

class ModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {
    private val minecraft = false

    override fun generateBlockStateModels(gen: BlockModelGenerators) {
        gen.texturedModels = mapOf(
            DuskBlocks.VOLCANIC_SANDSTONE to TexturedModel.TOP_BOTTOM_WITH_WALL[DuskBlocks.VOLCANIC_SANDSTONE],
            DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE to TexturedModel.createAllSame(
                TextureMapping.getBlockTexture(DuskBlocks.VOLCANIC_SANDSTONE, "_top")
            ),
            DuskBlocks.CUT_VOLCANIC_SANDSTONE to TexturedModel.COLUMN[DuskBlocks.VOLCANIC_SANDSTONE].updateTextures { texture: TextureMapping ->
                texture.put(
                    TextureSlot.SIDE,
                    TextureMapping.getBlockTexture(DuskBlocks.CUT_VOLCANIC_SANDSTONE)
                )
            },
            DuskBlocks.CHISELED_VOLCANIC_SANDSTONE to TexturedModel.COLUMN[DuskBlocks.CHISELED_VOLCANIC_SANDSTONE].updateTextures { texture: TextureMapping ->
                texture.put(TextureSlot.END, TextureMapping.getBlockTexture(DuskBlocks.VOLCANIC_SANDSTONE, "_top"))
                texture.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(DuskBlocks.CHISELED_VOLCANIC_SANDSTONE))
            }
        )
        DuskBlockFamilies.blockFamilies.forEach {
            gen.family(it.baseBlock).generateFor(it)
        }

        if (minecraft) {
            gen.generateAlternativeMinecraftModels()
        }
        gen.generateWoodModels()
        gen.generateStoneModels()
        gen.fogCanyonModels()

        DuskItemLists.SPAWN_EGGS_ITEM_LIST.forEach {
            gen.delegateItemModel(it, mc("item/template_spawn_egg"))
        }
        DuskBlockLists.RIBBON_BLOCKS_LIST.forEach {
            gen.registerRibbon(it)
        }
        DuskBlockLists.THROWABLE_BOMB_BLOCK_LIST.forEach {
            gen.throwableBlock(it)
        }
        DuskBlockLists.GUNPOWDER_BARREL_BLOCK_LIST.forEach {
            gen.gunpowderBarrelBlock(it)
        }

        gen.carpetStairs(DuskBlocks.RED_CARPET_STAIRS, Blocks.RED_WOOL)


        gen.createSimpleFlatItemModel(DuskItems.MACE_BLAZE)


        gen.createTrivialCube(DuskBlocks.BRONZE_BLOCK)
        gen.wallOffset(DuskBlocks.CUT_BRONZE_WALL, DuskBlocks.CUT_BRONZE)
        gen.createTrapdoor(DuskBlocks.BRONZE_TRAPDOOR)
        gen.godhomeShiftBlock(DuskBlocks.BRONZE_SHIFT_BLOCK)
        gen.vesselLantern(DuskBlocks.PALE_SOUL_VESSEL)
        gen.createLantern(DuskBlocks.PALE_SOUL_LANTERN)

        gen.registerDustBlockFromRedstone(DuskBlocks.GUNPOWDER)

        gen.createSimpleFlatItemModel(DuskBlocks.GUNPOWDER)
//        gen.registerBuiltin(ModelIds.getMinecraftNamespacedBlock("skull"), Blocks.SOUL_SAND)
//            .includeWithItem(
//                DuskBlocks.STRAY_SKULL,
//                DuskBlocks.BOGGED_SKULL,
//                DuskBlocks.GLOOM_SKULL
//            )
//            .includeWithoutItem(
//                DuskBlocks.STRAY_WALL_SKULL,
//                DuskBlocks.BOGGED_WALL_SKULL,
//                DuskBlocks.GLOOM_WALL_SKULL
//            )

        gen.createTrivialCube(DuskBlocks.TREACHEROUS_GOLD_BLOCK)
        gen.createTrivialCube(DuskBlocks.TARNISHED_GOLD_BLOCK)
        gen.createTrivialCube(DuskBlocks.LOST_SILVER_BLOCK)
        gen.createTrivialCube(DuskBlocks.SUNKEN_BRONZE_BLOCK)
        DuskBlockLists.COIN_STACK_BLOCK_LIST.forEach {
            gen.registerCoinStack(it)
        }
        gen.registerGoldPileBlock(
            DuskBlocks.TREACHEROUS_GOLD_COIN_PILE,
            TextureMapping.getBlockTexture(DuskBlocks.TREACHEROUS_GOLD_COIN_STACK, "_side")
        )
        gen.registerGoldPileBlock(
            DuskBlocks.TARNISHED_GOLD_COIN_PILE,
            TextureMapping.getBlockTexture(DuskBlocks.TARNISHED_GOLD_COIN_STACK, "_side")
        )
        gen.registerGoldPileBlock(
            DuskBlocks.LOST_SILVER_COIN_PILE,
            TextureMapping.getBlockTexture(DuskBlocks.LOST_SILVER_COIN_STACK, "_side")
        )
        gen.registerGoldPileBlock(
            DuskBlocks.SUNKEN_BRONZE_COIN_PILE,
            TextureMapping.getBlockTexture(DuskBlocks.SUNKEN_BRONZE_COIN_STACK, "_side")
        )
        DuskBlockLists.VESSEL_BLOCK_LIST.forEach {
            gen.registerDecorativeGoldBlock(it, "parent/mysterious_vessel", true)
        }
        DuskBlockLists.RELIC_BLOCK_LIST.forEach {
            gen.registerDecorativeGoldBlock(it, "parent/peculiar_relic", true)
        }
        DuskBlockLists.CHALICE_BLOCK_LIST.forEach {
            gen.registerChalice(it)
        }
        DuskBlockLists.CROWN_BLOCK_LIST.forEach {
            gen.registerDecorativeGoldBlock(it, "parent/royal_crown", false)
        }
        gen.registerDecorativeGoldBlock(DuskBlocks.LEGENDARY_CRYSTAL_CROWN, "parent/legendary_crown", false)

        gen.registerNethershroom(DuskBlocks.BLUE_NETHERSHROOM)
        gen.registerNethershroom(DuskBlocks.PURPLE_NETHERSHROOM)
        gen.registerNethershroomBlock(DuskBlocks.BLUE_NETHERSHROOM_BLOCK)
        gen.registerNethershroomBlock(DuskBlocks.PURPLE_NETHERSHROOM_BLOCK)
        gen.registerNethershroomBlock(DuskBlocks.NETHERSHROOM_STEM)

        gen.createTrivialCube(DuskBlocks.PAPER_BLOCK)

        gen.createNonTemplateModelBlock(DuskBlocks.BOG_MUD)
        gen.createSimpleFlatItemModel(DuskItems.BOG_MUD_BUCKET)


        gen.createTrivialCube(DuskBlocks.CRYSTAL_BLOCK)
        gen.createRotatedPillarWithHorizontalVariant(
            DuskBlocks.CRYSTAL_PILLAR_BLOCK,
            TexturedModel.COLUMN_ALT,
            TexturedModel.COLUMN_HORIZONTAL_ALT
        )


        gen.registerExhaust(DuskBlocks.EXHAUST_BLOCK)
        gen.registerCopperFan(DuskBlocks.COPPER_FAN, DuskBlocks.WAXED_COPPER_FAN)
        gen.registerCopperFan(DuskBlocks.EXPOSED_COPPER_FAN, DuskBlocks.WAXED_EXPOSED_COPPER_FAN)
        gen.registerCopperFan(DuskBlocks.WEATHERED_COPPER_FAN, DuskBlocks.WAXED_WEATHERED_COPPER_FAN)
        gen.registerCopperFan(DuskBlocks.OXIDIZED_COPPER_FAN, DuskBlocks.WAXED_OXIDIZED_COPPER_FAN)

        gen.registerGeyser(DuskBlocks.ROARING_GEYSER)
        gen.createRotatedVariantBlock(DuskBlocks.VOLCANIC_SAND)
        gen.createBrushableBlock(DuskBlocks.SUSPICIOUS_VOLCANIC_SAND)
//        gen.registerSingleton(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE) {
//            TexturedModel.getCubeAll(Texture.getSubId(DuskBlocks.VOLCANIC_SANDSTONE, "_top"))
//        }


        // DnD
        gen.registerGalleryRose(DuskBlocks.PAINTED_ROSE, TintState.NOT_TINTED)
        gen.registerSpiderlilly(DuskBlocks.SPIDERLILY, TintState.NOT_TINTED)

//        gen.registerBigChain(DuskBlocks.BIG_CELESTAL_CHAIN)
        val bottomModel = id("block/big_celestal_lantern_bottom")
        gen.registerBigLantern(DuskBlocks.BIG_MOON_LANTERN, bottomModel)
        gen.registerBigLantern(DuskBlocks.BIG_EARTH_LANTERN, bottomModel)
        gen.registerBigLantern(DuskBlocks.BIG_COMET_LANTERN, bottomModel)
        gen.registerBigLantern(DuskBlocks.BIG_SUN_LANTERN, bottomModel)
        gen.registerBigLantern(DuskBlocks.BIG_STAR_LANTERN, bottomModel)
        gen.registerBigLantern(DuskBlocks.BIG_NEBULAE_LANTERN, bottomModel)
        gen.registerBigLantern(DuskBlocks.BIG_ECLIPSE_LANTERN, bottomModel)

        gen.registerBell(DuskBlocks.CELESTAL_BELL)
        gen.createTrivialBlock(
            DuskBlocks.JOUNCESHROOM_BLOCK, TexturedModel.createDefault(TextureMapping::column, ModelTemplates.CUBE_COLUMN)
        )

        gen.createAmethystCluster(DuskBlocks.MOONCORE)
        gen.registerTallCrystal(DuskBlocks.TALL_REDSTONE_CRYSTAL)
        gen.blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("decorated_pot"), Blocks.TERRACOTTA)
            .createWithoutBlockItem(DuskBlocks.POT_O_SCREAMS)
        gen.blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("chest"), Blocks.OAK_PLANKS)
            .createWithoutBlockItem(DuskBlocks.CHEST_O_SOULS)

        gen.registerBunnyGrave(DuskBlocks.BUNNY_GRAVE, Blocks.SMOOTH_STONE, Blocks.STONE)

    }

    private fun BlockModelGenerators.fogCanyonModels() {
        this.bubbleBlock(DuskBlocks.FOG_BUBBLE)
        this.bubbleBlossomBlock(DuskBlocks.PURPLE_BUBBLE_BLOSSOM)
    }

    private val single = listOf(DuskItems.DEBUG_SPELL_ITEM, DuskItems.DIE_ITEM, DuskItems.CHILL_CHARGE)
    override fun generateItemModels(gen: ItemModelGenerators) {
        single.forEach { gen.generateFlatItem(it, ModelTemplates.FLAT_ITEM) }
        gen.generateFlatItem(DuskItems.FREEZE_ROD, ModelTemplates.FLAT_HANDHELD_ROD_ITEM)
        gen.generateFlatItem(DuskItems.HARVESTER_SCYTHE, item("parent/handheld_32", TextureSlot.LAYER0))

        val webWeaver = item("web_weaver", TextureSlot.LAYER0)
        gen.generateFlatItem(DuskItems.WEB_WEAVER, "_0", webWeaver)
        gen.generateFlatItem(DuskItems.WEB_WEAVER, "_1", webWeaver)
        gen.generateFlatItem(DuskItems.WEB_WEAVER, "_2", webWeaver)

//        gen.register(DuskItems.STRAY_SKULL, parentedItemModel(mc("template_skull")))
//        gen.register(DuskItems.BOGGED_SKULL, parentedItemModel(mc("template_skull")))
//        gen.register(DuskItems.GLOOM_SKULL, parentedItemModel(mc("template_skull")))
    }

    // TODO replace with voidlib
    private fun item(parent: String, vararg requiredTextures: TextureSlot): ModelTemplate =
        ModelTemplate(Optional.of(id("item/$parent")), Optional.empty(), *requiredTextures)
}
