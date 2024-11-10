package org.teamvoided.dusk_debris.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.tag.TagKey
import java.util.function.Predicate


fun inBlockTagPredicate(tag: TagKey<Block>): Predicate<BlockState> {
    return Predicate { state: BlockState -> state.isIn(tag) }
}