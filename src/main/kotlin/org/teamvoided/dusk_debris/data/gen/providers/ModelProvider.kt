package org.teamvoided.dusk_debris.data.gen.providers


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Blocks
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.*
import net.minecraft.util.Identifier
import org.teamvoided.dusk_debris.DuskDebris.mc
import org.teamvoided.dusk_debris.block.DuskBlockLists
import org.teamvoided.dusk_debris.init.DuskBlocks
import org.teamvoided.dusk_debris.init.DuskItems
import org.teamvoided.dusk_debris.item.DuskItemLists
import org.teamvoided.dusk_debris.util.*

class ModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {

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
        DuskBlockLists.blockFamily.forEach {
            gen.registerCubeAllModelTexturePool(it.baseBlock).family(it)
        }

        DuskItemLists.SPAWN_EGGS_ITEM_LIST.forEach {
            gen.registerParentedItemModel(it, Identifier.ofDefault("item/template_spawn_egg"))
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
        gen.registerDustBlockFromRedstone(DuskBlocks.GUNPOWDER)

        gen.registerItemModel(DuskItems.DEVELOPER_GUNPOWDER_ITEM)
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

        DuskBlockLists.VESSEL_BLOCK_LIST.forEach {
            gen.registerDecorativeGoldBlock(it,"parent/mysterious_vessel", true)
        }
        DuskBlockLists.RELIC_BLOCK_LIST.forEach {
            gen.registerDecorativeGoldBlock(it,"parent/peculiar_relic", true)
        }
        DuskBlockLists.CHALICE_BLOCK_LIST.forEach {
            gen.registerChalice(it)
        }
        DuskBlockLists.CROWN_BLOCK_LIST.forEach {
            gen.registerDecorativeGoldBlock(it, "parent/royal_crown", false)
        }

        gen.registerNethershroom(DuskBlocks.BLUE_NETHERSHROOM)
        gen.registerNethershroom(DuskBlocks.PURPLE_NETHERSHROOM)
        gen.registerNethershroomBlock(DuskBlocks.BLUE_NETHERSHROOM_BLOCK)
        gen.registerNethershroomBlock(DuskBlocks.PURPLE_NETHERSHROOM_BLOCK)
        gen.registerNethershroomBlock(DuskBlocks.NETHERSHROOM_STEM)
        gen.registerHandheldItem(DuskItems.BLACKSTONE_SWORD)
        gen.registerHandheldItem(DuskItems.BLACKSTONE_PICKAXE)
        gen.registerHandheldItem(DuskItems.BLACKSTONE_AXE)
        gen.registerHandheldItem(DuskItems.BLACKSTONE_SHOVEL)
        gen.registerHandheldItem(DuskItems.BLACKSTONE_HOE)

        gen.registerSimpleCubeAll(DuskBlocks.PAPER_BLOCK)

        gen.registerSimpleState(DuskBlocks.BOG_MUD)
        gen.registerItemModel(DuskItems.BOG_MUD_BUCKET)
        gen.registerLog(DuskBlocks.CYPRESS_LOG)
            .log(DuskBlocks.CYPRESS_LOG)
            .wood(DuskBlocks.CYPRESS_WOOD)
        gen.registerLog(DuskBlocks.STRIPPED_CYPRESS_LOG)
            .log(DuskBlocks.STRIPPED_CYPRESS_LOG)
            .wood(DuskBlocks.STRIPPED_CYPRESS_WOOD)

        gen.registerSimpleCubeAll(DuskBlocks.VOLCANIC_SAND)
        gen.registerDustable(DuskBlocks.SUSPICIOUS_VOLCANIC_SAND)
        gen.registerLog(DuskBlocks.CHARRED_LOG)
            .log(DuskBlocks.CHARRED_LOG)
            .wood(DuskBlocks.CHARRED_WOOD)
        gen.registerLog(DuskBlocks.STRIPPED_CHARRED_LOG)
            .log(DuskBlocks.STRIPPED_CHARRED_LOG)
            .wood(DuskBlocks.STRIPPED_CHARRED_WOOD)
        gen.registerHangingSign(
            DuskBlocks.STRIPPED_CHARRED_LOG,
            DuskBlocks.CHARRED_HANGING_SIGN,
            DuskBlocks.CHARRED_WALL_HANGING_SIGN
        )
        gen.registerHangingSign(
            DuskBlocks.STRIPPED_CYPRESS_LOG,
            DuskBlocks.CYPRESS_HANGING_SIGN,
            DuskBlocks.CYPRESS_WALL_HANGING_SIGN
        )
//        gen.registerSingleton(DuskBlocks.SMOOTH_VOLCANIC_SANDSTONE) {
//            TexturedModel.getCubeAll(Texture.getSubId(DuskBlocks.VOLCANIC_SANDSTONE, "_top"))
//        }
    }

    override fun generateItemModels(gen: ItemModelGenerator) {
//        gen.register(DuskItems.STRAY_SKULL, parentedItemModel(mc("template_skull")))
//        gen.register(DuskItems.BOGGED_SKULL, parentedItemModel(mc("template_skull")))
//        gen.register(DuskItems.GLOOM_SKULL, parentedItemModel(mc("template_skull")))
    }
}
