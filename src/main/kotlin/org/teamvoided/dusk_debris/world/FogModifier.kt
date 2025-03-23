package org.teamvoided.dusk_debris.world

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.HolderSet
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class FogModifier(
    val biomes: HolderSet<Biome>,
    val priority: Int,
    val start: Double,
    val end: Double,
    val modifier: Identifier? = null
) {
    companion object {
        val CODEC: Codec<FogModifier> = RecordCodecBuilder.create { instance ->
            instance.group(
                RegistryCodecs.homogeneousList(RegistryKeys.BIOME).fieldOf("biomes").forGetter { it.biomes },
                Codec.INT.fieldOf("priority").orElse(10).forGetter { it.priority },
                Codec.DOUBLE.fieldOf("start").orElse(1.0).forGetter { it.start },
                Codec.DOUBLE.fieldOf("end").orElse(1.0).forGetter { it.end },
                Identifier.CODEC.optionalFieldOf("modifier").forGetter { Optional.ofNullable(it.modifier) }
            )
                .apply(instance) { biomes: HolderSet<Biome>, priority: Int, start: Double, end: Double, modifier: Optional<Identifier> ->
                    FogModifier(biomes, priority, start, end, modifier.getOrNull())
                }
        }
    }
}