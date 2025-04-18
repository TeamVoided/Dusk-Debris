package org.teamvoided.dusk_debris.init

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.datafixer.TypeReferences
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Util
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.entity.BunnyGraveBlockEntity
import org.teamvoided.dusk_debris.block.entity.DuskChestBlockEntity
import org.teamvoided.dusk_debris.block.entity.TreasureChestBlockEntity
import org.teamvoided.dusks_and_dungeons.block.entity.*

object DuskBlockEntities {
    fun init() {}

    val TREASURE_CHEST: BlockEntityType<TreasureChestBlockEntity> = register(
        "treasure_chest", BlockEntityType.Builder.create(
            ::TreasureChestBlockEntity,
            DuskBlocks.FORGOTTEN_CHEST
        )
    )

//    val STONE_CHEST_OLD: BlockEntityType<StoneChestBlockEntity> = register(
//        "stone_chest_old", BlockEntityType.Builder.create(
//            ::StoneChestBlockEntity,
//            DuskBlocks.STONE_CHEST
//        )
//    )

    val STONE_CHEST: BlockEntityType<DuskChestBlockEntity> = register(
        "stone_chest", BlockEntityType.Builder.create(
            ::DuskChestBlockEntity,
            //DuskBlocks.STONE_CHEST
        )
    )
    
    // DnD
    val CELESTAL_BELL: BlockEntityType<CelestalBellBlockEntity> =
        register("celestal_bell", BlockEntityType.Builder.create(::CelestalBellBlockEntity, DuskBlocks.CELESTAL_BELL))

    val CHEST_O_SOULS: BlockEntityType<ChestOSoulsBlockEntity> =
        register("chest_o_souls", BlockEntityType.Builder.create(::ChestOSoulsBlockEntity, DuskBlocks.CHEST_O_SOULS))

    val QUARTER_BLOCK_PILE: BlockEntityType<QuarterBlockPileBlockEntity> = register(
        "quarter_block_pile",
        BlockEntityType.Builder.create(::QuarterBlockPileBlockEntity, DuskBlocks.QUARTER_BLOCK_PILE)
    )

    val BUNNY_GRAVE: BlockEntityType<BunnyGraveBlockEntity> =
        register("bunny_grave", BlockEntityType.Builder.create(::BunnyGraveBlockEntity, DuskBlocks.BUNNY_GRAVE))

    val HAUNTED_BLOCK: BlockEntityType<HauntedBlockEntity> = register(
        "hauted_block", BlockEntityType.Builder.create(
            ::HauntedBlockEntity,
        )
    )
    val HAUNTED_GRAVESTONE_BLOCK: BlockEntityType<HauntedGravestoneBlockEntity> = register(
        "haunted_gravestone_block", BlockEntityType.Builder.create(
            ::HauntedGravestoneBlockEntity,
            DuskBlocks.HAUNTED_GRAVESTONE,
            DuskBlocks.SMALL_HAUNTED_GRAVESTONE,
            DuskBlocks.HAUNTED_DEEPSLATE_GRAVESTONE,
            DuskBlocks.SMALL_HAUNTED_DEEPSLATE_GRAVESTONE,
            DuskBlocks.HAUNTED_TUFF_GRAVESTONE,
            DuskBlocks.SMALL_HAUNTED_TUFF_GRAVESTONE,
            DuskBlocks.HAUNTED_BLACKSTONE_GRAVESTONE,
            DuskBlocks.SMALL_HAUNTED_BLACKSTONE_GRAVESTONE
        )
    )


    private fun <T : BlockEntity> register(id: String, builder: BlockEntityType.Builder<T>): BlockEntityType<T> {
        val type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id(id).toString())
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id(id), builder.build(type))
    }
}