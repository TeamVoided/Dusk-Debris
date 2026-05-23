package org.teamvoided.dusk_debris.world.gen.configured_feature.rock_spires

import com.mojang.serialization.Codec
import org.teamvoided.dusk_debris.world.gen.configured_feature.config.rock_spires.RockFormationFeatureConfig

class BadlandsFormationFeature<T:RockFormationFeatureConfig>(codec: Codec<T>) : RockFormationFeature<T>(codec) {
}