package org.teamvoided.dusk_debris.init

import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.teamvoided.dusk_debris.DuskDebris.id
import org.teamvoided.dusk_debris.block.entity.BunnyGraveBlockEntity
import org.teamvoided.dusk_debris.block.entity.DuskChestBlockEntity
import org.teamvoided.dusk_debris.block.entity.StatueBlockEntity
import org.teamvoided.dusk_debris.block.entity.TreasureChestBlockEntity
import org.teamvoided.dusk_debris.block.sot.entity.StackedChaliceBlockEntity
import org.teamvoided.dusks_and_dungeons.block.entity.*

object DuskBlockEntities {
    fun init() {}

    val TREASURE_CHEST: BlockEntityType<TreasureChestBlockEntity> = register(
        "treasure_chest", BlockEntityType.Builder.of(
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

    val STONE_CHEST = register(
        "stone_chest", ::DuskChestBlockEntity,
//            DuskBlocks.STONE_CHEST
    )

    // DnD
    val CELESTAL_BELL: BlockEntityType<CelestalBellBlockEntity> =
        register("celestal_bell", BlockEntityType.Builder.of(::CelestalBellBlockEntity, DuskBlocks.CELESTAL_BELL))

    val CHEST_O_SOULS: BlockEntityType<ChestOSoulsBlockEntity> =
        register("chest_o_souls", BlockEntityType.Builder.of(::ChestOSoulsBlockEntity, DuskBlocks.CHEST_O_SOULS))

    val QUARTER_BLOCK_PILE: BlockEntityType<QuarterBlockPileBlockEntity> = register(
        "quarter_block_pile",
        BlockEntityType.Builder.of(::QuarterBlockPileBlockEntity, DuskBlocks.QUARTER_BLOCK_PILE)
    )

    val BUNNY_GRAVE: BlockEntityType<BunnyGraveBlockEntity> =
        register("bunny_grave", BlockEntityType.Builder.of(::BunnyGraveBlockEntity, DuskBlocks.BUNNY_GRAVE))

    val HAUNTED_BLOCK: BlockEntityType<HauntedBlockEntity> = register(
        "hauted_block", BlockEntityType.Builder.of(
            ::HauntedBlockEntity,
        )
    )
    val HAUNTED_GRAVESTONE_BLOCK: BlockEntityType<HauntedGravestoneBlockEntity> = register(
        "haunted_gravestone_block", BlockEntityType.Builder.of(
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

    val STATUE = register("statue", ::StatueBlockEntity, DuskBlocks.STATUE)
    val STACKED_CHALICE = register("stacked_chalice", ::StackedChaliceBlockEntity, DuskBlocks.STACKED_CHALICE)

    private fun <T : BlockEntity> register(
        id: String, factory: (BlockPos, BlockState) -> T, vararg blocks: Block,
    ): BlockEntityType<T> {
        return register(id, BlockEntityType.Builder.of(factory, *blocks))
    }

    private fun <T : BlockEntity> register(id: String, builder: BlockEntityType.Builder<T>): BlockEntityType<T> {
        val type = Util.fetchChoiceType(References.BLOCK_ENTITY, id(id).toString())
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id(id), builder.build(type))
    }
}