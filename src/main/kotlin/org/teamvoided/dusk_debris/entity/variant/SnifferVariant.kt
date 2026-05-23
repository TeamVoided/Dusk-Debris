package org.teamvoided.dusk_debris.entity.variant

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.Biome
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class SnifferVariant(
    val biomes: HolderSet<Biome>,
    val color: Int? = null,
    val biomeColor: Holder<Biome>? = null,
    val overlayTexture: ResourceLocation? = null
) {
//    constructor(biomes: HolderSet<Biome>, overlayTexture: Identifier) :
//            this(biomes, null, null, overlayTexture)
//
//    constructor(biomes: HolderSet<Biome>, color: Int, overlayTexture: Identifier) :
//            this(biomes, color, null, overlayTexture)
//
//    constructor(biomes: HolderSet<Biome>, biomeColor: RegistryKey<Biome>, overlayTexture: Identifier) :
//            this(biomes, null, biomeColor.value, overlayTexture)

    val overlayTextureFull = getFullTextureId(overlayTexture)

    companion object {
        val CODEC: Codec<SnifferVariant> = RecordCodecBuilder.create { instance ->
            instance.group(
                RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter { it.biomes },
                Codec.INT.optionalFieldOf("color").forGetter { Optional.ofNullable(it.color) },
                Biome.CODEC.optionalFieldOf("biome_color").forGetter { Optional.ofNullable(it.biomeColor) },
                ResourceLocation.CODEC.optionalFieldOf("overlay_texture")
                    .forGetter { Optional.ofNullable(it.overlayTexture) })
                .apply(instance) { biomes: HolderSet<Biome>, color: Optional<Int>, biomeColor: Optional<Holder<Biome>>, overlayTexture: Optional<ResourceLocation> ->
                    SnifferVariant(biomes, color.getOrNull(), biomeColor.getOrNull(), overlayTexture.getOrNull())
                }
        }
//        val DIRECT_PACKET_CODEC: PacketCodec<RegistryByteBuf, SnifferVariant> =
//            PacketCodec.tuple(
//                PacketCodecs.holderSet(RegistryKeys.BIOME),
//                { it.biomes },
//                PacketCodecs.INT,
//                { it.color },
//                PacketCodecs.NETWORK_CODEC,
//                { it.biomeColor },
//                Identifier.PACKET_CODEC,
//                { it.overlayTexture },
//                ::SnifferVariant
//            )
//        val HOLDER_CODEC: Codec<Holder<SnifferVariant>> = RegistryElementCodec.of(SNIFFER_VARIANT, CODEC)
//        val PACKET_CODEC: PacketCodec<RegistryByteBuf, Holder<SnifferVariant>> =
//            PacketCodecs.holder(SNIFFER_VARIANT, DIRECT_PACKET_CODEC)

        private fun getFullTextureId(texture: ResourceLocation?): ResourceLocation? {
            return texture?.withPath { "textures/$it.png" }
        }
    }
}