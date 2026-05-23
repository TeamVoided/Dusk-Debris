package org.teamvoided.dusk_debris.world

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biome
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class FogModifier(
    val biomes: HolderSet<Biome>,
    val priority: Int,
    val start: Double,
    val end: Double,
    val modifier: ResourceLocation? = null
) {
    companion object {
        val CODEC: Codec<FogModifier> = RecordCodecBuilder.create { instance ->
            instance.group(
                RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter { it.biomes },
                Codec.INT.fieldOf("priority").orElse(10).forGetter { it.priority },
                Codec.DOUBLE.fieldOf("start").orElse(1.0).forGetter { it.start },
                Codec.DOUBLE.fieldOf("end").orElse(1.0).forGetter { it.end },
                ResourceLocation.CODEC.optionalFieldOf("modifier").forGetter { Optional.ofNullable(it.modifier) }
            )
                .apply(instance) { biomes: HolderSet<Biome>, priority: Int, start: Double, end: Double, modifier: Optional<ResourceLocation> ->
                    FogModifier(biomes, priority, start, end, modifier.getOrNull())
                }
        }
    }
}