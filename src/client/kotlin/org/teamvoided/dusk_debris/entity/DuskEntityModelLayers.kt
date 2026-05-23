package org.teamvoided.dusk_debris.entity

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.minecraft.client.model.HorseModel
import net.minecraft.client.model.HumanoidArmorModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.LayerDefinition
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.entity.bird.render.BirdEntityModel
import org.teamvoided.dusk_debris.entity.block.CelestalBellBlockEntityRenderer
import org.teamvoided.dusk_debris.entity.block.treasure_chest.TreasureChestBlockEntityModel
import org.teamvoided.dusk_debris.entity.chill_charge.render.ChillChargeEntityModel
import org.teamvoided.dusk_debris.entity.dice.render.DiceEntityModel
import org.teamvoided.dusk_debris.entity.dust_bunny.render.DustBunnyEntityModel
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.model.TinyEnemyJellyfishCoreModel
import org.teamvoided.dusk_debris.entity.jellyfish.tiny.model.TinyEnemyJellyfishModel
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraCoreModel
import org.teamvoided.dusk_debris.entity.jellyfish.volaphyra.model.VolaphyraMesogleaModel
import org.teamvoided.dusk_debris.entity.magic.vengeful_spirit.VengefulSpiritModel
import org.teamvoided.dusk_debris.entity.piffling.model.PifflingPumpkinModel
import org.teamvoided.dusk_debris.entity.skeleton.gloom.model.GloomEntityModel
import org.teamvoided.dusk_debris.entity.skeleton.wolf.render.SkeletonWolfEntityModel.Companion.texturedModelData
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemCloakModel
import org.teamvoided.dusk_debris.entity.tuff_golem.model.TuffGolemEntityModel

object DuskEntityModelLayers {
    val GLOOM: ModelLayerLocation = registerMain("gloomed")
    val GLOOM_EYES: ModelLayerLocation = registerMain("gloomed_eyes")
    val GLOOM_OUTER: ModelLayerLocation = register("gloomed", "outer")
    val GLOOM_INNER_ARMOR: ModelLayerLocation = createInnerArmor("gloomed")
    val GLOOM_OUTER_ARMOR: ModelLayerLocation = createOuterArmor("gloomed")
    val SKELETON_WOLF: ModelLayerLocation = registerMain("skeleton_wolf")
    val WITHER_SKELETON_WOLF: ModelLayerLocation = registerMain("wither_skeleton_wolf")
    val WITHER_SKELETON_HORSE: ModelLayerLocation = registerMain("wither_skeleton_horse")

    val TUFF_GOLEM: ModelLayerLocation = registerMain("tuff_golem")
    val TUFF_GOLEM_ROBE: ModelLayerLocation = register("tuff_golem", "robe")

    val VOLAPHYRA: ModelLayerLocation = registerMain("volaphyra")
    val VOLAPHYRA_MESOGLEA: ModelLayerLocation = register("volaphyra", "mesoglea")
    val VOLAPHYRA_CORE: ModelLayerLocation = registerMain("volaphyra_core")

    val TINY_ENEMY_JELLYFISH: ModelLayerLocation = registerMain("tiny_enemy_jellyfish")
    val TINY_ENEMY_JELLYFISH_MESOGLEA: ModelLayerLocation = register("tiny_enemy_jellyfish", "mesoglea")

    val VENGEFUL_SPIRIT: ModelLayerLocation = registerMain("vengeful_spirit")

    val TREASURE_CHEST: ModelLayerLocation = registerMain("treasure_chest")
    val TREASURE_CHEST_LEFT: ModelLayerLocation = registerMain("treasure_chest_left")
    val TREASURE_CHEST_RIGHT: ModelLayerLocation = registerMain("treasure_chest_right")


    val CHILL_CHARGE: ModelLayerLocation = registerMain("chill_charge")
    val BIRD: ModelLayerLocation = registerMain("bird")
    val DICE: ModelLayerLocation = registerMain("dice")
    val DUST_BUNNY: ModelLayerLocation = registerMain("dust_bunny")
    val PIFFLING_PUMPKIN: ModelLayerLocation = registerMain("piffling_pumpkin")

    val CELESTAL_BELL = registerMain("celestal_bell")

    fun init() {
        EntityModelLayerRegistry.registerModelLayer(GLOOM, GloomEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_EYES, GloomEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_INNER_ARMOR, ::createInnerArmor)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_OUTER_ARMOR, ::createOuterArmor)
        EntityModelLayerRegistry.registerModelLayer(GLOOM_OUTER, ::createSkeletonOuterLayer)
        EntityModelLayerRegistry.registerModelLayer(SKELETON_WOLF, ::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(WITHER_SKELETON_WOLF, ::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(WITHER_SKELETON_HORSE, ::createHorseLayer)

        EntityModelLayerRegistry.registerModelLayer(TUFF_GOLEM, TuffGolemEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(TUFF_GOLEM_ROBE, TuffGolemCloakModel::texturedModelData)

        EntityModelLayerRegistry.registerModelLayer(VOLAPHYRA, VolaphyraCoreModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(VOLAPHYRA_MESOGLEA, VolaphyraMesogleaModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(VOLAPHYRA_CORE, VolaphyraCoreModel::texturedModelData)

        EntityModelLayerRegistry.registerModelLayer(TINY_ENEMY_JELLYFISH, TinyEnemyJellyfishCoreModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(TINY_ENEMY_JELLYFISH_MESOGLEA, TinyEnemyJellyfishModel::texturedModelData)

        EntityModelLayerRegistry.registerModelLayer(VENGEFUL_SPIRIT, VengefulSpiritModel::texturedModelData)

        EntityModelLayerRegistry.registerModelLayer(TREASURE_CHEST, TreasureChestBlockEntityModel::singleTexturedModelData)
        EntityModelLayerRegistry.registerModelLayer(TREASURE_CHEST_LEFT, TreasureChestBlockEntityModel::leftDoubleTexturedModelData)
        EntityModelLayerRegistry.registerModelLayer(TREASURE_CHEST_RIGHT, TreasureChestBlockEntityModel::rightDoubleTexturedModelData)

        // DnD
        EntityModelLayerRegistry.registerModelLayer(BIRD, BirdEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(DICE, DiceEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(DUST_BUNNY, DustBunnyEntityModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(PIFFLING_PUMPKIN, PifflingPumpkinModel::texturedModelData)
        EntityModelLayerRegistry.registerModelLayer(CELESTAL_BELL, CelestalBellBlockEntityRenderer::getTexturedModelData)
        EntityModelLayerRegistry.registerModelLayer(CHILL_CHARGE, ChillChargeEntityModel::texturedModelData)
    }

    private fun createInnerArmor(): LayerDefinition =
        LayerDefinition.create(HumanoidArmorModel.createBodyLayer(CubeDeformation(0.5F)), 64, 32)

    private fun createOuterArmor(): LayerDefinition =
        LayerDefinition.create(HumanoidArmorModel.createBodyLayer(CubeDeformation(1.0F)), 64, 32)

    private fun createSkeletonOuterLayer(): LayerDefinition =
        LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation(0.25f), 0.0f), 64, 32)

    private fun createHorseLayer(): LayerDefinition =
        LayerDefinition.create(HorseModel.createBodyMesh(CubeDeformation.NONE), 64, 64)

    private fun registerMain(id: String): ModelLayerLocation {
        return register(id, "main")
    }

    private fun createInnerArmor(id: String): ModelLayerLocation {
        return register(id, "inner_armor")
    }

    private fun createOuterArmor(id: String): ModelLayerLocation {
        return register(id, "outer_armor")
    }

    private fun register(id: String, layer: String): ModelLayerLocation {
        val entityModelLayer = create(id, layer)
        return entityModelLayer
    }

    private fun create(id: String, layer: String): ModelLayerLocation {
        return ModelLayerLocation(id(id), layer)
    }
}