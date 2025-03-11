package org.teamvoided.dusk_debris.init

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import org.teamvoided.dusk_debris.block.entity.TreasureChestBlockEntity
import org.teamvoided.dusk_debris.block.entity.DuskChestBlockEntity

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
            DuskBlocks.STONE_CHEST
        )
    )


    private fun <T : BlockEntity> register(id: String, builder: BlockEntityType.Builder<T>): BlockEntityType<T> {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, builder.build(null))
    }
}