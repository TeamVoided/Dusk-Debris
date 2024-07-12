package org.teamvoided.dusk_debris.init

import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.datafixer.TypeReferences
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Util
import org.teamvoided.dusk_debris.block.entity.TreasureChestBlockEntity

object DuskBlockEntities {
    fun init() {}

    val TREASURE_CHEST: BlockEntityType<TreasureChestBlockEntity> = register(
        "treasure_chest", BlockEntityType.Builder.create(
            ::TreasureChestBlockEntity,
            DuskBlocks.FORGOTTEN_CHEST
        )
    )

    private fun <T : BlockEntity> register(id: String, builder: BlockEntityType.Builder<T>): BlockEntityType<T> {
        val type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id)
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, builder.build(type))
    }

}