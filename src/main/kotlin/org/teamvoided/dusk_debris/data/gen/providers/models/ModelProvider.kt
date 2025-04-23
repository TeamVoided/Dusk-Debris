package org.teamvoided.dusk_debris.data.gen.providers.models


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Blocks
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.*
import net.minecraft.data.client.model.BlockStateModelGenerator.TintType
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
import org.teamvoided.dusk_debris.util.model_helper.bubbleBlock
import org.teamvoided.dusk_debris.util.model_helper.bubbleBlossomBlock
import org.teamvoided.dusk_debris.util.model_helper.carpetStairs
import java.util.*

class ModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {
    private val minecraft = false

    override fun generateBlockStateModels(gen: BlockStateModelGenerator) {
        gen.sandstoneModels = mapOf(
            DuskBlocks.VOLCANIC_SANDSTONE to TexturedModel.SIDE_TOP_BOTTOM_WALL[DuskBlocks.VOLCANIC_SANDSTONE],
            DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE to TexturedModel.getCubeAll(
                Texture.getSubId(DuskBlocks.VOLCANIC_SANDSTONE, "_top")
            ),
            DuskBlocks.CUT_VOLCANIC_SANDSTONE to TexturedModel.CUBE_COLUMN[DuskBlocks.VOLCANIC_SANDSTONE].texture { texture: Texture ->
                texture.put(
                    TextureKey.SIDE,
                    Texture.getId(DuskBlocks.CUT_VOLCANIC_SANDSTONE)
                )
            },
            DuskBlocks.CHISELED_VOLCANIC_SANDSTONE to TexturedModel.CUBE_COLUMN[DuskBlocks.CHISELED_VOLCANIC_SANDSTONE].texture { texture: Texture ->
                texture.put(TextureKey.END, Texture.getSubId(DuskBlocks.VOLCANIC_SANDSTONE, "_top"))
                texture.put(TextureKey.SIDE, Texture.getId(DuskBlocks.CHISELED_VOLCANIC_SANDSTONE))
            }
        )
        DuskBlockFamilies.blockFamilies.forEach {
            gen.registerCubeAllModelTexturePool(it.baseBlock).family(it)
        }

        if (minecraft) {
            gen.generateAlternativeMinecraftModels()
        }
        gen.generateWoodModels()
        gen.generateStoneModels()
        gen.fogCanyonModels()

        DuskItemLists.SPAWN_EGGS_ITEM_LIST.forEach {
            gen.registerParentedItemModel(it, mc("item/template_spawn_egg"))
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


        gen.registerItemModel(DuskItems.MACE_BLAZE)


        gen.registerSimpleCubeAll(DuskBlocks.BRONZE_BLOCK)
        gen.wallOffset(DuskBlocks.CUT_BRONZE_WALL, DuskBlocks.CUT_BRONZE)
        gen.registerTrapdoor(DuskBlocks.BRONZE_TRAPDOOR)
        gen.godhomeShiftBlock(DuskBlocks.BRONZE_SHIFT_BLOCK)
        gen.vesselLantern(DuskBlocks.PALE_SOUL_VESSEL)
        gen.registerLantern(DuskBlocks.PALE_SOUL_LANTERN)

        gen.registerDustBlockFromRedstone(DuskBlocks.GUNPOWDER)

        gen.registerItemModel(DuskBlocks.GUNPOWDER)
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

        gen.registerSimpleCubeAll(DuskBlocks.TREACHEROUS_GOLD_BLOCK)
        gen.registerSimpleCubeAll(DuskBlocks.TARNISHED_GOLD_BLOCK)
        gen.registerSimpleCubeAll(DuskBlocks.LOST_SILVER_BLOCK)
        gen.registerSimpleCubeAll(DuskBlocks.SUNKEN_BRONZE_BLOCK)
        DuskBlockLists.COIN_STACK_BLOCK_LIST.forEach {
            gen.registerCoinStack(it)
        }
        gen.registerGoldPileBlock(
            DuskBlocks.TREACHEROUS_GOLD_COIN_PILE,
            Texture.getSubId(DuskBlocks.TREACHEROUS_GOLD_COIN_STACK, "_side")
        )
        gen.registerGoldPileBlock(
            DuskBlocks.TARNISHED_GOLD_COIN_PILE,
            Texture.getSubId(DuskBlocks.TARNISHED_GOLD_COIN_STACK, "_side")
        )
        gen.registerGoldPileBlock(
            DuskBlocks.LOST_SILVER_COIN_PILE,
            Texture.getSubId(DuskBlocks.LOST_SILVER_COIN_STACK, "_side")
        )
        gen.registerGoldPileBlock(
            DuskBlocks.SUNKEN_BRONZE_COIN_PILE,
            Texture.getSubId(DuskBlocks.SUNKEN_BRONZE_COIN_STACK, "_side")
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

        gen.registerSimpleCubeAll(DuskBlocks.PAPER_BLOCK)

        gen.registerSimpleState(DuskBlocks.BOG_MUD)
        gen.registerItemModel(DuskItems.BOG_MUD_BUCKET)


        gen.registerSimpleCubeAll(DuskBlocks.CRYSTAL_BLOCK)
        gen.registerAxisRotated(
            DuskBlocks.CRYSTAL_PILLAR_BLOCK,
            TexturedModel.END_FOR_TOP_CUBE_COLUMN,
            TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL
        )


        gen.registerCopperFan(DuskBlocks.COPPER_FAN, DuskBlocks.WAXED_COPPER_FAN)
        gen.registerCopperFan(DuskBlocks.EXPOSED_COPPER_FAN, DuskBlocks.WAXED_EXPOSED_COPPER_FAN)
        gen.registerCopperFan(DuskBlocks.WEATHERED_COPPER_FAN, DuskBlocks.WAXED_WEATHERED_COPPER_FAN)
        gen.registerCopperFan(DuskBlocks.OXIDIZED_COPPER_FAN, DuskBlocks.WAXED_OXIDIZED_COPPER_FAN)

        gen.registerGeyser(DuskBlocks.ROARING_GEYSER)
        gen.registerRotatable(DuskBlocks.VOLCANIC_SAND)
        gen.registerDustable(DuskBlocks.SUSPICIOUS_VOLCANIC_SAND)
//        gen.registerSingleton(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE) {
//            TexturedModel.getCubeAll(Texture.getSubId(DuskBlocks.VOLCANIC_SANDSTONE, "_top"))
//        }


        // DnD
        gen.registerGalleryRose(DuskBlocks.PAINTED_ROSE, TintType.NOT_TINTED)
        gen.registerSpiderlilly(DuskBlocks.SPIDERLILY, TintType.NOT_TINTED)

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
        gen.registerSingleton(
            DuskBlocks.JOUNCESHROOM_BLOCK, TexturedModel.makeFactory(Texture::sideEnd, Models.CUBE_COLUMN)
        )

        gen.registerAmethyst(DuskBlocks.MOONCORE)
        gen.registerTallCrystal(DuskBlocks.TALL_REDSTONE_CRYSTAL)
        gen.registerBuiltin(ModelIds.getMinecraftNamespacedBlock("decorated_pot"), Blocks.TERRACOTTA)
            .includeWithoutItem(DuskBlocks.POT_O_SCREAMS)
        gen.registerBuiltin(ModelIds.getMinecraftNamespacedBlock("chest"), Blocks.OAK_PLANKS)
            .includeWithoutItem(DuskBlocks.CHEST_O_SOULS)

        gen.registerBunnyGrave(DuskBlocks.BUNNY_GRAVE, Blocks.SMOOTH_STONE, Blocks.STONE)

    }

    private fun BlockStateModelGenerator.fogCanyonModels() {
        this.bubbleBlock(DuskBlocks.FOG_BUBBLE)
        this.bubbleBlossomBlock(DuskBlocks.PURPLE_BUBBLE_BLOSSOM)
    }

    private val single = listOf(DuskItems.DIE_ITEM, DuskItems.CHILL_CHARGE)
    override fun generateItemModels(gen: ItemModelGenerator) {
        single.forEach { gen.register(it, Models.SINGLE_LAYER_ITEM) }
        gen.register(DuskItems.FREEZE_ROD, Models.HANDHELD_ROD)
        gen.register(DuskItems.HARVESTER_SCYTHE, item("parent/handheld_32", TextureKey.LAYER0))

        val webWeaver = item("web_weaver", TextureKey.LAYER0)
        gen.register(DuskItems.WEB_WEAVER, "_0", webWeaver)
        gen.register(DuskItems.WEB_WEAVER, "_1", webWeaver)
        gen.register(DuskItems.WEB_WEAVER, "_2", webWeaver)

//        gen.register(DuskItems.STRAY_SKULL, parentedItemModel(mc("template_skull")))
//        gen.register(DuskItems.BOGGED_SKULL, parentedItemModel(mc("template_skull")))
//        gen.register(DuskItems.GLOOM_SKULL, parentedItemModel(mc("template_skull")))
    }

    // TODO replace with voidlib
    private fun item(parent: String, vararg requiredTextures: TextureKey): Model =
        Model(Optional.of(id("item/$parent")), Optional.empty(), *requiredTextures)
}
