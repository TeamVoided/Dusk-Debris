package org.teamvoided.dusk_debris.util

import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Predicate


fun inBlockTagPredicate(tag: TagKey<Block>): Predicate<BlockState> {
    return Predicate { state: BlockState -> state.`is`(tag) }
}

/*
    import net.minecraft.world.gen.GenerationStep.Feature.RAW_GENERATION as rg0
    import net.minecraft.world.gen.GenerationStep.Feature.LAKES as l1
    import net.minecraft.world.gen.GenerationStep.Feature.LOCAL_MODIFICATIONS as lm2
    import net.minecraft.world.gen.GenerationStep.Feature.UNDERGROUND_STRUCTURES as us3
    import net.minecraft.world.gen.GenerationStep.Feature.SURFACE_STRUCTURES as ss4
    import net.minecraft.world.gen.GenerationStep.Feature.STRONGHOLDS as s5
    import net.minecraft.world.gen.GenerationStep.Feature.UNDERGROUND_ORES as uo6
    import net.minecraft.world.gen.GenerationStep.Feature.UNDERGROUND_DECORATION as ud7
    import net.minecraft.world.gen.GenerationStep.Feature.FLUID_SPRINGS as fs8
    import net.minecraft.world.gen.GenerationStep.Feature.VEGETAL_DECORATION as vd9
    import net.minecraft.world.gen.GenerationStep.Feature.TOP_LAYER_MODIFICATION as tlm10
 */