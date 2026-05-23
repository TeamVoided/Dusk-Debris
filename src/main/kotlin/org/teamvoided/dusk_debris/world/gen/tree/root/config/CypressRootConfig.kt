package org.teamvoided.dusk_debris.world.gen.tree.root.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

@JvmRecord
data class CypressRootConfig(
    val canGrowThrough: HolderSet<Block>,
    val muddyRootsIn: HolderSet<Block>,
    val muddyRootsProvider: BlockStateProvider,
    val maxRootWidth: Int,
    val maxRootLength: Int,
    val randomSkewChance: Float
) {
    companion object {
        val CODEC: Codec<CypressRootConfig> = RecordCodecBuilder.create {
            it.group(
                RegistryCodecs.homogeneousList(Registries.BLOCK)
                    .fieldOf("can_grow_through").forGetter(CypressRootConfig::canGrowThrough),
                RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("muddy_roots_in")
                    .forGetter { placement: CypressRootConfig -> placement.muddyRootsIn },
                BlockStateProvider.CODEC.fieldOf("muddy_roots_provider")
                    .forGetter(CypressRootConfig::muddyRootsProvider),
                Codec.intRange(1, 12).fieldOf("max_root_width").forGetter(CypressRootConfig::maxRootWidth),
                Codec.intRange(1, 64).fieldOf("max_root_length").forGetter(CypressRootConfig::maxRootLength),
                Codec.floatRange(0.0f, 1.0f).fieldOf("random_skew_chance")
                    .forGetter(CypressRootConfig::randomSkewChance)
            ).apply(it, ::CypressRootConfig)
        }
    }
}
