package org.teamvoided.dusk_debris.entity.sniffer

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.*
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.RegistryElementCodec
import net.minecraft.world.biome.Biome
import org.teamvoided.dusk_debris.init.DuskRegistries.SNIFFER_VARIANT
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class SnifferVariant(
    val biomes: HolderSet<Biome>,
    val color: Int? = null,
    val biomeColor: Holder<Biome>? = null,
    val overlayTexture: Identifier? = null
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
                RegistryCodecs.homogeneousList(RegistryKeys.BIOME).fieldOf("biomes").forGetter { it.biomes },
                Codec.INT.optionalFieldOf("color").forGetter { Optional.ofNullable(it.color) },
                Biome.REGISTRY_CODEC.optionalFieldOf("biome_color").forGetter { Optional.ofNullable(it.biomeColor) },
                Identifier.CODEC.optionalFieldOf("overlay_texture")
                    .forGetter { Optional.ofNullable(it.overlayTexture) })
                .apply(instance) { biomes: HolderSet<Biome>, color: Optional<Int>, biomeColor: Optional<Holder<Biome>>, overlayTexture: Optional<Identifier> ->
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

        private fun getFullTextureId(texture: Identifier?): Identifier? {
            return texture?.withPath { "textures/$it.png" }
        }
    }
}