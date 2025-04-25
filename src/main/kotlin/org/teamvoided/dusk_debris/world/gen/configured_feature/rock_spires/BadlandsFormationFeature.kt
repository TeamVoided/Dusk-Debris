package org.teamvoided.dusk_debris.world.gen.configured_feature.rock_spires

import com.mojang.serialization.Codec
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.StructureWorldAccess
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.RockFormationFeatureConfig
import org.teamvoided.dusk_debris.world.gen.noise.FastNoise

class BadlandsFormationFeature<T:RockFormationFeatureConfig>(codec: Codec<T>) : RockFormationFeature<T>(codec) {
}