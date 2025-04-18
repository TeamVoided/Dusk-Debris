package org.teamvoided.dusk_debris.data.gen.providers.models

import net.minecraft.block.Blocks
import net.minecraft.data.client.model.BlockStateModelGenerator
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.util.model
import org.teamvoided.dusk_debris.util.model_helper.*

object MinecraftModelProvider {
    fun BlockStateModelGenerator.generateAlternativeMinecraftModels() {
        this.addAxis(Blocks.MANGROVE_ROOTS)
        this.sculkCatalystRotate(Blocks.SCULK_CATALYST)
        this.sculkShriekerRotate(Blocks.SCULK_SHRIEKER)
        this.sculkSensorRotate(Blocks.SCULK_SENSOR)

        this.pixelAccurateSign(Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN, Blocks.OAK_PLANKS, Blocks.OAK_LOG)
        this.pixelAccurateSign(Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_LOG)
        this.pixelAccurateSign(Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.BIRCH_PLANKS, Blocks.BIRCH_LOG)
        this.pixelAccurateSign(Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_LOG)
        this.pixelAccurateSign(Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.ACACIA_PLANKS, Blocks.ACACIA_LOG)
        this.pixelAccurateSign(Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_LOG)
        //this.pixelAccurateSign(Blocks.PALE_OAK_SIGN, Blocks.PALE_OAK_WALL_SIGN, Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_LOG)
        this.pixelAccurateSign(Blocks.MANGROVE_SIGN, Blocks.MANGROVE_WALL_SIGN, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_LOG)
        this.pixelAccurateSign(Blocks.CHERRY_SIGN, Blocks.CHERRY_WALL_SIGN, Blocks.CHERRY_PLANKS, Blocks.CHERRY_LOG)
        this.pixelAccurateSign(Blocks.BAMBOO_SIGN, Blocks.BAMBOO_WALL_SIGN, id("block/bamboo_sign_planks"), id("block/bamboo_sign_log"))
        this.pixelAccurateSign(Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN, Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STEM)
        this.pixelAccurateSign(Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN, Blocks.WARPED_PLANKS, Blocks.WARPED_STEM)
    }
}