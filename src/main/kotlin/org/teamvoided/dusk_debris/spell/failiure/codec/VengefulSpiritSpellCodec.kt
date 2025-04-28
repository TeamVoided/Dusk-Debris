package org.teamvoided.dusk_debris.spell.failiure.codec//package org.teamvoided.dusk_debris.spell.codec
//
//import com.mojang.serialization.Codec
//import com.mojang.serialization.codecs.RecordCodecBuilder
//import net.minecraft.util.StringIdentifiable
//
//class VengefulSpiritSpellCodec(val coolup: Int = 8) : SpellConfig {
//
//    companion object {
//        val CODEC: Codec<VengefulSpiritSpellCodec> =
//            RecordCodecBuilder.create { instance ->
//                instance.group(
//                    StringIdentifiable.Codec.INT
//                        .fieldOf("coolup")
//                        .orElse(8)
//                        .forGetter { it.coolup },
//                ).apply(instance, ::VengefulSpiritSpellCodec)
//            }
//    }
//}