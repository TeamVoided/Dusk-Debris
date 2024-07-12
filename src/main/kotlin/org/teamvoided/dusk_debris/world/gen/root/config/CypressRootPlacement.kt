package org.teamvoided.dusk_debris.world.gen.root.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.Block
import net.minecraft.registry.HolderSet
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.gen.stateprovider.BlockStateProvider

@JvmRecord
data class CypressRootPlacement(
    val canGrowThrough: HolderSet<Block>,
    val muddyRootsIn: HolderSet<Block>,
    val muddyRootsProvider: BlockStateProvider,
    val maxRootWidth: Int,
    val maxRootLength: Int,
    val randomSkewChance: Float
) {
    fun canGrowThrough(): HolderSet<Block> {
        return this.canGrowThrough
    }

    fun muddyRootsIn(): HolderSet<Block> {
        return this.muddyRootsIn
    }

    fun muddyRootsProvider(): BlockStateProvider {
        return this.muddyRootsProvider
    }

    fun maxRootWidth(): Int {
        return this.maxRootWidth
    }

    fun maxRootLength(): Int {
        return this.maxRootLength
    }

    fun randomSkewChance(): Float {
        return this.randomSkewChance
    }

    companion object {
        val CODEC: Codec<CypressRootPlacement> =
            RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<CypressRootPlacement> ->
                instance.group(
                    RegistryCodecs.homogeneousList(RegistryKeys.BLOCK)
                        .fieldOf("can_grow_through")
                        .forGetter { placement: CypressRootPlacement -> placement.canGrowThrough },
                    RegistryCodecs.homogeneousList(RegistryKeys.BLOCK).fieldOf("muddy_roots_in")
                        .forGetter { placement: CypressRootPlacement -> placement.muddyRootsIn },
                    BlockStateProvider.TYPE_CODEC.fieldOf("muddy_roots_provider")
                        .forGetter { placement: CypressRootPlacement -> placement.muddyRootsProvider },
                    Codec.intRange(1, 12).fieldOf("max_root_width")
                        .forGetter { placement: CypressRootPlacement -> placement.maxRootWidth },
                    Codec.intRange(1, 64).fieldOf("max_root_length")
                        .forGetter { placement: CypressRootPlacement -> placement.maxRootLength },
                    Codec.floatRange(0.0f, 1.0f).fieldOf("random_skew_chance")
                        .forGetter { placement: CypressRootPlacement -> placement.randomSkewChance }
                ).apply(
                    instance
                ) { canGrowThrough: HolderSet<Block>, muddyRootsIn: HolderSet<Block>, muddyRootsProvider: BlockStateProvider, maxRootWidth: Int, maxRootLength: Int, randomSkewChance: Float ->
                    CypressRootPlacement(
                        canGrowThrough,
                        muddyRootsIn,
                        muddyRootsProvider,
                        maxRootWidth,
                        maxRootLength,
                        randomSkewChance
                    )
                }
            }
    }
}